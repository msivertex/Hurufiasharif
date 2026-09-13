package com.example.islamic

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Qibla Coordinates and Calculations
 */
object QiblaConstants {
  const val KAABA_LATITUDE = 21.422487
  const val KAABA_LONGITUDE = 39.826206
  const val EARTH_RADIUS_KM = 6371.0
}

/**
 * Result of Qibla Calculation
 */
data class QiblaInfo(
  val bearingDegrees: Float,
  val distanceKm: Double,
  val deviceAzimuth: Float,
  val needleAngle: Float,
  val isFacingKaaba: Boolean
)

/**
 * Service to manage Sensor compass and calculate live Qibla bearing
 */
class QiblaCompassManager(private val context: Context) : SensorEventListener {

  private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
  private var rotationSensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
  private var accelerometer: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
  private var magnetometer: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

  private var hasRotSensor = rotationSensor != null

  private val gravity = FloatArray(3)
  private val geomagnetic = FloatArray(3)
  private val rotationMatrix = FloatArray(9)
  private val orientation = FloatArray(3)

  private var currentAzimuth = 0f
  private var smoothedAzimuth = 0f
  private var wasFacingKaaba = false

  var onCompassUpdated: ((QiblaInfo) -> Unit)? = null

  var userLatitude: Double = 23.8103 // Default Dhaka
  var userLongitude: Double = 90.4125

  fun startListening(lat: Double, lng: Double) {
    userLatitude = lat
    userLongitude = lng
    wasFacingKaaba = false

    if (hasRotSensor) {
      sensorManager?.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_UI)
    } else {
      sensorManager?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
      sensorManager?.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI)
    }
  }

  fun stopListening() {
    sensorManager?.unregisterListener(this)
  }

  override fun onSensorChanged(event: SensorEvent?) {
    if (event == null) return

    if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
      SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
      SensorManager.getOrientation(rotationMatrix, orientation)
      var azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
      if (azimuth < 0) azimuth += 360f
      processAzimuth(azimuth)
    } else if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
      // Low pass filter
      gravity[0] = 0.8f * gravity[0] + 0.2f * event.values[0]
      gravity[1] = 0.8f * gravity[1] + 0.2f * event.values[1]
      gravity[2] = 0.8f * gravity[2] + 0.2f * event.values[2]
      computeOrientation()
    } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
      geomagnetic[0] = 0.8f * geomagnetic[0] + 0.2f * event.values[0]
      geomagnetic[1] = 0.8f * geomagnetic[1] + 0.2f * event.values[1]
      geomagnetic[2] = 0.8f * geomagnetic[2] + 0.2f * event.values[2]
      computeOrientation()
    }
  }

  private fun computeOrientation() {
    val r = FloatArray(9)
    val i = FloatArray(9)
    val success = SensorManager.getRotationMatrix(r, i, gravity, geomagnetic)
    if (success) {
      SensorManager.getOrientation(r, orientation)
      var azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
      if (azimuth < 0) azimuth += 360f
      processAzimuth(azimuth)
    }
  }

  private fun processAzimuth(azimuth: Float) {
    // Smooth angle interpolation
    var diff = azimuth - smoothedAzimuth
    while (diff < -180f) diff += 360f
    while (diff > 180f) diff -= 360f
    smoothedAzimuth = (smoothedAzimuth + 0.2f * diff + 360f) % 360f
    currentAzimuth = smoothedAzimuth

    val qiblaBearing = calculateQiblaBearing(userLatitude, userLongitude)
    val distanceKm = calculateDistanceToKaabaKm(userLatitude, userLongitude)

    // Needle points towards Qibla relative to device heading
    var needleAngle = (qiblaBearing - currentAzimuth + 360f) % 360f
    val isFacing = needleAngle < 4.0f || needleAngle > 356.0f

    if (isFacing && !wasFacingKaaba) {
      triggerVibrate()
    }
    wasFacingKaaba = isFacing

    val info = QiblaInfo(
      bearingDegrees = qiblaBearing,
      distanceKm = distanceKm,
      deviceAzimuth = currentAzimuth,
      needleAngle = needleAngle,
      isFacingKaaba = isFacing
    )

    onCompassUpdated?.invoke(info)
  }

  private fun triggerVibrate() {
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator?.vibrate(
          VibrationEffect.createOneShot(80, VibrationEffect.DEFAULT_AMPLITUDE)
        )
      } else {
        @Suppress("DEPRECATION")
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        @Suppress("DEPRECATION")
        vibrator?.vibrate(80)
      }
    } catch (_: Exception) {}
  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

  companion object {
    /**
     * Calculate Great-Circle Forward Azimuth towards Kaaba
     */
    fun calculateQiblaBearing(lat: Double, lng: Double): Float {
      val phi1 = Math.toRadians(lat)
      val phi2 = Math.toRadians(QiblaConstants.KAABA_LATITUDE)
      val deltaLambda = Math.toRadians(QiblaConstants.KAABA_LONGITUDE - lng)

      val y = sin(deltaLambda) * cos(phi2)
      val x = cos(phi1) * sin(phi2) - sin(phi1) * cos(phi2) * cos(deltaLambda)

      var bearing = Math.toDegrees(atan2(y, x)).toFloat()
      if (bearing < 0) bearing += 360f
      return bearing
    }

    /**
     * Calculate Great-Circle Distance to Kaaba in kilometers (Haversine formula)
     */
    fun calculateDistanceToKaabaKm(lat: Double, lng: Double): Double {
      val phi1 = Math.toRadians(lat)
      val phi2 = Math.toRadians(QiblaConstants.KAABA_LATITUDE)
      val deltaPhi = Math.toRadians(QiblaConstants.KAABA_LATITUDE - lat)
      val deltaLambda = Math.toRadians(QiblaConstants.KAABA_LONGITUDE - lng)

      val a = sin(deltaPhi / 2) * sin(deltaPhi / 2) +
        cos(phi1) * cos(phi2) * sin(deltaLambda / 2) * sin(deltaLambda / 2)
      val c = 2 * asin(sqrt(a))
      return QiblaConstants.EARTH_RADIUS_KM * c
    }
  }
}

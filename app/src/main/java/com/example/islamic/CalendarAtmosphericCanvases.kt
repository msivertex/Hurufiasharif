package com.example.islamic

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * 3D Page-Flip Animation on Date Numbers
 * Flips smoothly along the X-axis when date text updates.
 */
@Composable
fun FlipDateCardNumber(
  dateText: String,
  modifier: Modifier = Modifier,
  textStyle: TextStyle = TextStyle(
    fontSize = 62.sp,
    fontWeight = FontWeight.ExtraBold,
    color = Color.White,
    letterSpacing = (-1).sp
  )
) {
  var displayedText by remember { mutableStateOf(dateText) }
  var pendingText by remember { mutableStateOf(dateText) }
  val flipProgress = remember { Animatable(0f) }

  LaunchedEffect(dateText) {
    if (dateText != displayedText) {
      pendingText = dateText
      flipProgress.snapTo(0f)
      flipProgress.animateTo(
        targetValue = 180f,
        animationSpec = tween(durationMillis = 480, easing = FastOutSlowInEasing)
      )
      displayedText = dateText
      flipProgress.snapTo(0f)
    }
  }

  val angle = flipProgress.value
  val currentText = if (angle >= 90f) pendingText else displayedText
  val rotationXAngle = if (angle < 90f) -angle else 180f - angle

  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier.graphicsLayer {
      rotationX = rotationXAngle
      cameraDistance = 14f * density
    }
  ) {
    Text(
      text = currentText,
      style = textStyle
    )
  }
}

/**
 * 1. DYNAMIC BANGLA SEASONAL CANVAS (6 SEASONS LOGIC):
 * - Sarat (Autumn - Ashvin/Bhadra): Soft white drifting clouds and floating Kashful feathery particles.
 * - Barsha (Monsoon - Ashar/Sravan): Subtle rain-drop keyframe animations on blue-grey canvas.
 * - Sheet (Winter - Poush/Magh): Soft misty/foggy ambient overlay with cool blue tones.
 * - Basanta (Spring - Falgun/Chaitra): Gently floating flower petal particles.
 * - Grishma (Summer - Baishakh/Jyaishtha): Warm golden sunbeams and shimmer.
 * - Hemanta (Late Autumn - Kartik/Agrahayan): Gentle harvest breeze with amber pollen/grain particles.
 */
@Composable
fun BanglaSeasonalCanvas(
  seasonNameBn: String,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "bangla_season_anim")

  val driftProgress by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(18000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "drift"
  )

  val wavePhase by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = (2f * PI).toFloat(),
    animationSpec = infiniteRepeatable(
      animation = tween(4000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "wave"
  )

  val rainProgress by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "rain"
  )

  Canvas(modifier = modifier.fillMaxSize()) {
    val w = size.width
    val h = size.height

    when {
      // 1. Sarat (Autumn / শরৎকাল) -> Floating soft white clouds & drifting Kashful particles
      seasonNameBn.contains("শরৎ") -> {
        // Soft floating clouds
        val cloud1X = ((driftProgress * (w + 160f)) % (w + 160f)) - 80f
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color.White.copy(alpha = 0.20f), Color.Transparent),
            center = Offset(cloud1X, h * 0.25f),
            radius = 55.dp.toPx()
          ),
          radius = 55.dp.toPx(),
          center = Offset(cloud1X, h * 0.25f)
        )
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color.White.copy(alpha = 0.16f), Color.Transparent),
            center = Offset(cloud1X + 45.dp.toPx(), h * 0.22f),
            radius = 42.dp.toPx()
          ),
          radius = 42.dp.toPx(),
          center = Offset(cloud1X + 45.dp.toPx(), h * 0.22f)
        )

        // Drifting Kashful particles (soft feathery white oval specks)
        val kashfulCount = 14
        for (i in 0 until kashfulCount) {
          val pSeed = (i * 0.071f)
          val progress = (driftProgress * 1.5f + pSeed) % 1f
          val px = (progress * (w + 40f)) - 20f
          val py = (h * (0.15f + 0.70f * ((i * 37) % 100 / 100f))) + sin(wavePhase + i) * 12.dp.toPx()
          val alpha = (sin(progress * PI.toFloat()) * 0.65f).coerceIn(0f, 1f)

          drawOval(
            brush = Brush.linearGradient(
              colors = listOf(Color.White.copy(alpha = alpha), Color(0xFFF1F5F9).copy(alpha = alpha * 0.3f)),
              start = Offset(px - 5.dp.toPx(), py - 2.dp.toPx()),
              end = Offset(px + 5.dp.toPx(), py + 2.dp.toPx())
            ),
            topLeft = Offset(px - 6.dp.toPx(), py - 2.5.dp.toPx()),
            size = androidx.compose.ui.geometry.Size(12.dp.toPx(), 5.dp.toPx())
          )
        }
      }

      // 2. Barsha (Monsoon / বর্ষাকাল) -> Subtle raindrops falling on blue-grey canvas
      seasonNameBn.contains("বর্ষা") -> {
        val dropCount = 20
        for (i in 0 until dropCount) {
          val xRatio = ((i * 47) % 100) / 100f
          val startOffset = ((i * 61) % 100) / 100f
          val dropProg = (rainProgress + startOffset) % 1f
          val dx = w * xRatio + (dropProg * 14.dp.toPx())
          val dy = dropProg * (h + 30.dp.toPx()) - 20.dp.toPx()
          val dropLen = 14.dp.toPx()

          drawLine(
            brush = Brush.verticalGradient(
              colors = listOf(Color.Transparent, Color(0xFFBAE6FD).copy(alpha = 0.50f), Color.White.copy(alpha = 0.75f)),
              startY = dy - dropLen,
              endY = dy
            ),
            start = Offset(dx - 3.dp.toPx(), dy - dropLen),
            end = Offset(dx, dy),
            strokeWidth = 1.2.dp.toPx(),
            cap = StrokeCap.Round
          )
        }
      }

      // 3. Sheet (Winter / শীতকাল) -> Soft misty/foggy ambient overlay with cool blue tones
      seasonNameBn.contains("শীত") -> {
        // Horizontal misty vapor bands
        val mistY1 = h * 0.35f + sin(wavePhase) * 10.dp.toPx()
        val mistY2 = h * 0.65f + cos(wavePhase) * 12.dp.toPx()

        drawRect(
          brush = Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.14f), Color.Transparent),
            startY = mistY1 - 25.dp.toPx(),
            endY = mistY1 + 25.dp.toPx()
          )
        )
        drawRect(
          brush = Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color(0xFFE0F2FE).copy(alpha = 0.16f), Color.Transparent),
            startY = mistY2 - 30.dp.toPx(),
            endY = mistY2 + 30.dp.toPx()
          )
        )

        // Subtle floating frost crystals
        for (i in 0..10) {
          val fx = ((driftProgress + i * 0.09f) % 1f) * w
          val fy = h * (0.2f + 0.6f * ((i * 29 % 100) / 100f))
          val fAlpha = (0.25f + 0.35f * sin(wavePhase + i)).coerceIn(0.1f, 0.7f)
          drawCircle(
            color = Color.White.copy(alpha = fAlpha),
            radius = 1.8.dp.toPx(),
            center = Offset(fx, fy)
          )
        }
      }

      // 4. Basanta (Spring / বসন্তকাল) -> Gently floating flower petal particles
      seasonNameBn.contains("বসন্ত") -> {
        val petalCount = 12
        for (i in 0 until petalCount) {
          val pSeed = (i * 0.083f)
          val progress = (driftProgress * 1.3f + pSeed) % 1f
          val px = (progress * (w + 40f)) - 20f
          val py = (h * (0.20f + 0.65f * ((i * 53) % 100 / 100f))) + sin(wavePhase * 1.2f + i) * 14.dp.toPx()
          val alpha = (sin(progress * PI.toFloat()) * 0.70f).coerceIn(0f, 1f)

          drawOval(
            brush = Brush.radialGradient(
              colors = listOf(Color(0xFFFDE047).copy(alpha = alpha), Color(0xFFF43F5E).copy(alpha = alpha * 0.75f)),
              center = Offset(px, py),
              radius = 5.dp.toPx()
            ),
            topLeft = Offset(px - 4.5.dp.toPx(), py - 3.dp.toPx()),
            size = androidx.compose.ui.geometry.Size(9.dp.toPx(), 6.dp.toPx())
          )
        }
      }

      // 5. Grishma (Summer / গ্রীষ্মকাল) -> Warm golden sunbeams & shimmer
      seasonNameBn.contains("গ্রীষ্ম") -> {
        val rayAlpha = (0.12f + 0.12f * sin(wavePhase)).coerceIn(0.08f, 0.25f)
        val beamPath = Path().apply {
          moveTo(w * 0.15f, 0f)
          lineTo(w * 0.45f, 0f)
          lineTo(w * 0.75f, h)
          lineTo(w * 0.35f, h)
          close()
        }
        drawPath(
          path = beamPath,
          brush = Brush.verticalGradient(
            colors = listOf(Color(0xFFFEF08A).copy(alpha = rayAlpha), Color.Transparent)
          )
        )
      }

      // 6. Hemanta (Late Autumn / হেমন্তকাল) -> Golden harvest aura & drifting golden grain specks
      else -> {
        val grainCount = 10
        for (i in 0 until grainCount) {
          val pSeed = (i * 0.1f)
          val progress = (driftProgress + pSeed) % 1f
          val gx = (progress * (w + 30f)) - 15f
          val gy = (h * (0.25f + 0.60f * ((i * 41) % 100 / 100f))) + sin(wavePhase + i) * 8.dp.toPx()
          val alpha = (sin(progress * PI.toFloat()) * 0.55f).coerceIn(0f, 1f)

          drawCircle(
            color = Color(0xFFFDE68A).copy(alpha = alpha),
            radius = 2.2.dp.toPx(),
            center = Offset(gx, gy)
          )
        }
      }
    }
  }
}

/**
 * 2. DYNAMIC HIJRI SPECIAL MONTH CANVAS:
 * - Starry night canvas with Hijri-day computed Moon phases (1-30 days) with soft moonlight glow.
 * - Ramadan Special (Month 9): Subtle glowing Arabic lanterns (Fanous) gently swaying at top edges.
 * - Dhul Hijjah Special (Month 12): Serene golden geometric Islamic star aura background pattern.
 * - General Islamic Months: Deep Emerald starlight atmosphere with shimmering stars.
 */
@Composable
fun HijriMonthAtmosphericCanvas(
  hijriMonth: Int,
  hijriDay: Int,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "hijri_season_anim")

  val starPulse by infiniteTransition.animateFloat(
    initialValue = 0.25f,
    targetValue = 0.95f,
    animationSpec = infiniteRepeatable(
      animation = tween(2400, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "star_pulse"
  )

  val fanousSway by infiniteTransition.animateFloat(
    initialValue = -8f,
    targetValue = 8f,
    animationSpec = infiniteRepeatable(
      animation = tween(3200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "fanous_sway"
  )

  val auraPulse by infiniteTransition.animateFloat(
    initialValue = 0.08f,
    targetValue = 0.22f,
    animationSpec = infiniteRepeatable(
      animation = tween(4000, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "aura_pulse"
  )

  Canvas(modifier = modifier.fillMaxSize()) {
    val w = size.width
    val h = size.height

    // 1. Shimmering particle stars across deep emerald/midnight backdrop
    val starPositions = listOf(
      Offset(w * 0.10f, h * 0.16f),
      Offset(w * 0.24f, h * 0.10f),
      Offset(w * 0.38f, h * 0.20f),
      Offset(w * 0.58f, h * 0.12f),
      Offset(w * 0.72f, h * 0.16f),
      Offset(w * 0.92f, h * 0.22f),
      Offset(w * 0.14f, h * 0.42f),
      Offset(w * 0.68f, h * 0.45f),
      Offset(w * 0.32f, h * 0.56f),
      Offset(w * 0.88f, h * 0.54f),
      Offset(w * 0.20f, h * 0.68f),
      Offset(w * 0.78f, h * 0.72f)
    )

    starPositions.forEachIndexed { i, pos ->
      val alphaMultiplier = if (i % 2 == 0) starPulse else (1.2f - starPulse)
      val r = if (i % 3 == 0) 2.2.dp.toPx() else 1.4.dp.toPx()
      drawCircle(
        color = Color(0xFF6EE7B7).copy(alpha = (0.55f * alphaMultiplier).coerceIn(0.12f, 0.92f)),
        radius = r,
        center = pos
      )
    }

    // 2. Computed Hijri Moon Phase (1..30 days) with soft moonlight aura
    val moonCenter = Offset(w * 0.82f, h * 0.25f)
    val moonRadius = 16.dp.toPx()
    drawCalendarMoonPhase(
      hijriDay = hijriDay,
      center = moonCenter,
      radius = moonRadius
    )

    // 3. RAMADAN SPECIAL (Month 9): Glowing Arabic Lanterns (Fanous) gently swaying at top corners
    if (hijriMonth == 9) {
      drawFanousLantern(
        anchor = Offset(w * 0.14f, 0f),
        swayAngleDeg = fanousSway,
        scale = 1.0f
      )
      drawFanousLantern(
        anchor = Offset(w * 0.86f, 0f),
        swayAngleDeg = -fanousSway * 0.85f,
        scale = 0.9f
      )
    }

    // 4. DHUL HIJJAH SPECIAL (Month 12): Golden Geometric Islamic Star Aura Pattern
    if (hijriMonth == 12) {
      val center = Offset(w * 0.5f, h * 0.45f)
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(Color(0xFFFDE047).copy(alpha = auraPulse), Color.Transparent),
          center = center,
          radius = w * 0.55f
        ),
        radius = w * 0.55f,
        center = center
      )

      // Sacred 8-Point Islamic Star geometric outline
      val numPoints = 8
      val outerR = 50.dp.toPx()
      val innerR = 30.dp.toPx()
      val starPath = Path()
      for (k in 0 until numPoints * 2) {
        val angle = (k * PI / numPoints).toFloat()
        val rad = if (k % 2 == 0) outerR else innerR
        val x = center.x + rad * cos(angle)
        val y = center.y + rad * sin(angle)
        if (k == 0) starPath.moveTo(x, y) else starPath.lineTo(x, y)
      }
      starPath.close()

      drawPath(
        path = starPath,
        color = Color(0xFFFEF08A).copy(alpha = auraPulse * 0.75f),
        style = Stroke(width = 1.2.dp.toPx())
      )
    }
  }
}

/**
 * Draws dynamic Hijri Moon phase computed for dayOfMonth 1-30
 */
fun DrawScope.drawCalendarMoonPhase(
  hijriDay: Int,
  center: Offset,
  radius: Float
) {
  val moonColor = Color(0xFFFFFBEB)
  val shadowColor = Color(0xFF022C22).copy(alpha = 0.94f)
  val earthshineColor = Color(0xFF0F172A).copy(alpha = 0.35f)
  val clampedDay = hijriDay.coerceIn(1, 30)

  // Outer moonlight aura glow
  if (clampedDay in 1..27) {
    val auraAlpha = when (clampedDay) {
      in 13..15 -> 0.42f
      in 10..18 -> 0.28f
      else -> 0.16f
    }
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(Color(0xFFFEF9C3).copy(alpha = auraAlpha), Color.Transparent),
        center = center,
        radius = radius * 2.0f
      ),
      radius = radius * 2.0f,
      center = center
    )
  }

  when (clampedDay) {
    1, 29, 30 -> {
      // New Moon / Amabashya (Faint silhouette with delicate starlight rim)
      drawCircle(
        color = earthshineColor,
        radius = radius,
        center = center
      )
      drawCircle(
        color = Color(0xFF94A3B8).copy(alpha = 0.30f),
        radius = radius,
        center = center,
        style = Stroke(width = 1.dp.toPx())
      )
    }
    in 2..3 -> {
      // Thin Waxing Crescent (Hilal) with soft earthshine
      drawCircle(color = earthshineColor, radius = radius, center = center)
      drawCircle(color = moonColor, radius = radius, center = center)
      drawCircle(
        color = shadowColor,
        radius = radius * 0.95f,
        center = Offset(center.x - radius * 0.55f, center.y)
      )
    }
    in 4..8 -> {
      // First Quarter Crescent
      drawCircle(color = earthshineColor, radius = radius, center = center)
      drawCircle(color = moonColor, radius = radius, center = center)
      drawCircle(
        color = shadowColor,
        radius = radius * 0.95f,
        center = Offset(center.x - radius * 0.35f, center.y)
      )
      // Maria crater shading
      drawCircle(
        color = Color(0xFFCBD5E1).copy(alpha = 0.22f),
        radius = radius * 0.25f,
        center = Offset(center.x + radius * 0.30f, center.y - radius * 0.12f)
      )
    }
    in 9..12 -> {
      // Waxing Gibbous
      drawCircle(color = moonColor, radius = radius, center = center)
      drawCircle(
        color = shadowColor,
        radius = radius * 0.95f,
        center = Offset(center.x - radius * 0.75f, center.y)
      )
      drawCircle(
        color = Color(0xFFCBD5E1).copy(alpha = 0.20f),
        radius = radius * 0.25f,
        center = Offset(center.x + radius * 0.15f, center.y - radius * 0.10f)
      )
    }
    in 13..15 -> {
      // Radiant Full Moon (Badr) with crater plains
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(Color(0xFFFFFFFF), Color(0xFFFEF3C7)),
          center = center,
          radius = radius
        ),
        radius = radius,
        center = center
      )
      drawCircle(
        color = Color(0xFFCBD5E1).copy(alpha = 0.30f),
        radius = radius * 0.30f,
        center = Offset(center.x - radius * 0.20f, center.y - radius * 0.15f)
      )
      drawCircle(
        color = Color(0xFFCBD5E1).copy(alpha = 0.25f),
        radius = radius * 0.22f,
        center = Offset(center.x + radius * 0.25f, center.y + radius * 0.15f)
      )
    }
    in 16..22 -> {
      // Waning Gibbous
      drawCircle(color = moonColor, radius = radius, center = center)
      drawCircle(
        color = shadowColor,
        radius = radius * 0.95f,
        center = Offset(center.x + radius * 0.75f, center.y)
      )
    }
    in 23..27 -> {
      // Thin Waning Crescent
      drawCircle(color = earthshineColor, radius = radius, center = center)
      drawCircle(color = moonColor, radius = radius, center = center)
      drawCircle(
        color = shadowColor,
        radius = radius * 0.92f,
        center = Offset(center.x + radius * 0.55f, center.y)
      )
    }
    else -> {
      drawCircle(color = moonColor, radius = radius, center = center)
    }
  }
}

/**
 * Dedicated visual Composable rendering dynamic Moon phase with halo and label
 */
@Composable
fun DynamicCalendarMoonPhaseView(
  hijriDay: Int,
  modifier: Modifier = Modifier
) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val center = Offset(size.width / 2f, size.height / 2f)
      val radius = kotlin.math.min(size.width, size.height) * 0.40f
      drawCalendarMoonPhase(
        hijriDay = hijriDay,
        center = center,
        radius = radius
      )
    }
  }
}

/**
 * Draws a subtle decorative glowing Arabic Lantern (Fanous) swaying from top anchor
 */
private fun DrawScope.drawFanousLantern(
  anchor: Offset,
  swayAngleDeg: Float,
  scale: Float = 1.0f
) {
  val rad = swayAngleDeg * (PI.toFloat() / 180f)
  val chainLen = 22.dp.toPx() * scale
  val lanternCenterX = anchor.x + chainLen * sin(rad)
  val lanternCenterY = anchor.y + chainLen * cos(rad)

  // 1. Suspension chain / string
  drawLine(
    color = Color(0xFFFEF3C7).copy(alpha = 0.65f),
    start = anchor,
    end = Offset(lanternCenterX, lanternCenterY),
    strokeWidth = 1.dp.toPx()
  )

  // 2. Candlelight warm aura glow
  drawCircle(
    brush = Brush.radialGradient(
      colors = listOf(Color(0xFFFDE047).copy(alpha = 0.50f), Color(0xFFEA580C).copy(alpha = 0.15f), Color.Transparent),
      center = Offset(lanternCenterX, lanternCenterY + 12.dp.toPx() * scale),
      radius = 24.dp.toPx() * scale
    ),
    radius = 24.dp.toPx() * scale,
    center = Offset(lanternCenterX, lanternCenterY + 12.dp.toPx() * scale)
  )

  // 3. Lantern body silhouette
  val lanternWidth = 14.dp.toPx() * scale
  val lanternHeight = 22.dp.toPx() * scale
  val bodyPath = Path().apply {
    moveTo(lanternCenterX, lanternCenterY)
    lineTo(lanternCenterX - lanternWidth * 0.4f, lanternCenterY + lanternHeight * 0.25f)
    lineTo(lanternCenterX - lanternWidth * 0.5f, lanternCenterY + lanternHeight * 0.70f)
    lineTo(lanternCenterX - lanternWidth * 0.25f, lanternCenterY + lanternHeight)
    lineTo(lanternCenterX + lanternWidth * 0.25f, lanternCenterY + lanternHeight)
    lineTo(lanternCenterX + lanternWidth * 0.5f, lanternCenterY + lanternHeight * 0.70f)
    lineTo(lanternCenterX + lanternWidth * 0.4f, lanternCenterY + lanternHeight * 0.25f)
    close()
  }

  drawPath(
    path = bodyPath,
    color = Color(0xFFFEF08A).copy(alpha = 0.85f),
    style = Stroke(width = 1.2.dp.toPx())
  )

  // Lantern glowing central glass
  drawCircle(
    color = Color(0xFFFFFBEB).copy(alpha = 0.75f),
    radius = 3.5.dp.toPx() * scale,
    center = Offset(lanternCenterX, lanternCenterY + lanternHeight * 0.55f)
  )
}

/**
 * 3. DYNAMIC GREGORIAN SEASONAL CANVAS:
 * - Dec, Jan, Feb (Winter): Gentle floating snowfall particles.
 * - Jun, Jul, Aug (Summer): Sunlight flare & radiating warm beams.
 * - Mar, Apr, May (Spring): Fresh breeze particles & gentle light.
 * - Sep, Oct, Nov (Autumn): Golden autumn leaf drift.
 */
@Composable
fun GregorianSeasonalCanvas(
  monthIndex: Int, // 0 = Jan .. 11 = Dec
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "greg_season_anim")

  val driftProgress by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(14000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "drift"
  )

  val pulseAnim by infiniteTransition.animateFloat(
    initialValue = 0.15f,
    targetValue = 0.35f,
    animationSpec = infiniteRepeatable(
      animation = tween(3500, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse"
  )

  Canvas(modifier = modifier.fillMaxSize()) {
    val w = size.width
    val h = size.height

    when (monthIndex) {
      // Winter (Dec = 11, Jan = 0, Feb = 1): Gentle Snowfall
      11, 0, 1 -> {
        val snowCount = 18
        for (i in 0 until snowCount) {
          val xRatio = ((i * 53) % 100) / 100f
          val startOffset = ((i * 37) % 100) / 100f
          val snowProg = (driftProgress * 1.5f + startOffset) % 1f
          val sx = w * xRatio + sin(driftProgress * 2f * PI.toFloat() + i) * 10.dp.toPx()
          val sy = snowProg * (h + 20.dp.toPx()) - 10.dp.toPx()
          val sRadius = if (i % 3 == 0) 2.5.dp.toPx() else 1.5.dp.toPx()
          val alpha = (sin(snowProg * PI.toFloat()) * 0.75f).coerceIn(0.1f, 0.85f)

          drawCircle(
            color = Color.White.copy(alpha = alpha),
            radius = sRadius,
            center = Offset(sx, sy)
          )
        }
      }

      // Summer (Jun = 5, Jul = 6, Aug = 7): Radiant sunlight lens flare
      5, 6, 7 -> {
        val sunCenter = Offset(w * 0.75f, h * 0.20f)
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color(0xFFFEF08A).copy(alpha = pulseAnim * 0.8f), Color.Transparent),
            center = sunCenter,
            radius = 65.dp.toPx()
          ),
          radius = 65.dp.toPx(),
          center = sunCenter
        )

        // Subtle diagonal sunbeam
        val beamPath = Path().apply {
          moveTo(w * 0.60f, 0f)
          lineTo(w * 0.90f, 0f)
          lineTo(w * 0.40f, h)
          lineTo(w * 0.10f, h)
          close()
        }
        drawPath(
          path = beamPath,
          brush = Brush.verticalGradient(
            colors = listOf(Color(0xFFFEF3C7).copy(alpha = pulseAnim * 0.45f), Color.Transparent)
          )
        )
      }

      // Spring (Mar = 2, Apr = 3, May = 4): Fresh spring breeze
      2, 3, 4 -> {
        for (i in 0..12) {
          val pSeed = (i * 0.08f)
          val prog = (driftProgress * 1.4f + pSeed) % 1f
          val bx = (prog * (w + 40f)) - 20f
          val by = h * (0.2f + 0.6f * ((i * 43 % 100) / 100f)) + sin(driftProgress * 2f * PI.toFloat() + i) * 8.dp.toPx()
          val alpha = (sin(prog * PI.toFloat()) * 0.60f).coerceIn(0f, 1f)

          drawCircle(
            color = Color(0xFF67E8F9).copy(alpha = alpha),
            radius = 1.8.dp.toPx(),
            center = Offset(bx, by)
          )
        }
      }

      // Autumn (Sep = 8, Oct = 9, Nov = 10): Gentle golden autumn leaf drift
      else -> {
        for (i in 0..10) {
          val pSeed = (i * 0.09f)
          val prog = (driftProgress * 1.2f + pSeed) % 1f
          val lx = (prog * (w + 40f)) - 20f
          val ly = h * (0.25f + 0.55f * ((i * 39 % 100) / 100f)) + sin(driftProgress * 2f * PI.toFloat() + i) * 10.dp.toPx()
          val alpha = (sin(prog * PI.toFloat()) * 0.65f).coerceIn(0f, 1f)

          drawOval(
            brush = Brush.radialGradient(
              colors = listOf(Color(0xFFFDE68A).copy(alpha = alpha), Color(0xFFF97316).copy(alpha = alpha * 0.7f)),
              center = Offset(lx, ly),
              radius = 5.dp.toPx()
            ),
            topLeft = Offset(lx - 4.dp.toPx(), ly - 2.5.dp.toPx()),
            size = androidx.compose.ui.geometry.Size(8.dp.toPx(), 5.dp.toPx())
          )
        }
      }
    }
  }
}

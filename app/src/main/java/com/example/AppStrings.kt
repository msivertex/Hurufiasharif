package com.example

class AppStrings {
    companion object {
        // ১. অ্যাপের নাম (ইংরেজি ক্যাপিটাল, বাংলা ও আরবি)
        @JvmStatic
        fun getAppName(lang: String): String {
            return when (lang) {
                "EN" -> "HURUFIA SHARIF"
                "AR" -> "حروفية شريف"
                "BN" -> "হরুফিয়া শরিফ"
                else -> "হরুফিয়া শরিফ"
            }
        }

        // ২. লগইন স্ক্রিনের টেক্সটসমূহ
        @JvmStatic
        fun getLoginTitle(lang: String): String {
            return when (lang) {
                "EN" -> "Welcome Back"
                "AR" -> "أهلاً بك"
                "BN" -> "স্বাগতম"
                else -> "স্বাগতম"
            }
        }

        @JvmStatic
        fun getEmailHint(lang: String): String {
            return when (lang) {
                "EN" -> "Enter Email"
                "AR" -> "أدخل البريد الإلكتروني"
                "BN" -> "ইমেইল লিখুন"
                else -> "ইমেইল লিখুন"
            }
        }

        @JvmStatic
        fun getPasswordHint(lang: String): String {
            return when (lang) {
                "EN" -> "Enter Password"
                "AR" -> "أدخل كلمة المرور"
                "BN" -> "পাসওয়ার্ড লিখুন"
                else -> "পাসওয়ার্ড লিখুন"
            }
        }

        @JvmStatic
        fun getSubmitBtn(lang: String): String {
            return when (lang) {
                "EN" -> "Login / Register"
                "AR" -> "تسجيل الدخول"
                "BN" -> "লগইন / রেজিস্ট্রেশন"
                else -> "লগইন / রেজিস্ট্রেশন"
            }
        }
    }
}

enum class Language(val code: String, val titleEn: String, val nativeLabel: String) {
    BN("BN", "Bangla", "বাংলা"),
    EN("EN", "English", "English"),
    AR("AR", "Arabic", "العربية")
}

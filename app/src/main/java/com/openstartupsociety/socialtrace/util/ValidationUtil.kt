package com.openstartupsociety.socialtrace.util

object ValidationUtil {

    private const val REGEX_PHONE = "^[0-9]{10}\$"

    fun isValidPhoneNumber(phone: String?): Boolean {
        return if (phone.isNullOrEmpty()) false
        else phone.matches(Regex(REGEX_PHONE))
    }
}
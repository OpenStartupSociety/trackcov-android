package com.openstartupsociety.socialtrace.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtil {

    val timestamp = Calendar.getInstance().timeInMillis

    fun getDayYear(date: Date): String {
        val simpleDateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getTimeZone("IST")
        return simpleDateFormat.format(date)
    }

    fun getDayYear(timestamp: Long): String {
        val simpleDateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getTimeZone("IST")
        return simpleDateFormat.format(timestamp)
    }

    fun getDateFromDayYear(text: String): Date {
        val simpleDateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getTimeZone("IST")
        return simpleDateFormat.parse(text)!!
    }

    fun convertToDDMMYYYY(text: String): String {
        var simpleDateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        val date = simpleDateFormat.parse(text)
        simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return simpleDateFormat.format(date!!)
    }

    fun convertToDMMMYYYY(text: String): String {
        var simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = simpleDateFormat.parse(text)
        simpleDateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        return simpleDateFormat.format(date!!)
    }

    @JvmStatic
    fun convertToDMMMYYYY(timestamp: Long): String {
        val date = Date(timestamp)
        val simpleDateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        return simpleDateFormat.format(date)
    }

    fun convertToDDMMYYYY(timestamp: Long): String {
        val date = Date(timestamp)
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return simpleDateFormat.format(date)
    }
}

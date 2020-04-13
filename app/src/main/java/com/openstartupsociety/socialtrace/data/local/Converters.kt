package com.openstartupsociety.socialtrace.data.local

import androidx.room.TypeConverter
import com.openstartupsociety.socialtrace.data.local.entities.DailySymptomQuestion
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class Converters {
    private val moshi by lazy { Moshi.Builder().build() }

    @TypeConverter
    fun fromStringToDailyCheckQuestion(value: String): List<DailySymptomQuestion> {
        val listTypes = Types.newParameterizedType(List::class.java, DailySymptomQuestion::class.java)
        val adapter = moshi.adapter<List<DailySymptomQuestion>>(listTypes)
        return adapter.fromJson(value)!!
    }

    @TypeConverter
    fun fromDailyCheckQuestionsToString(value: List<DailySymptomQuestion>): String {
        val listTypes = Types.newParameterizedType(List::class.java, DailySymptomQuestion::class.java)
        val adapter = moshi.adapter<List<Any>>(listTypes)
        return adapter.toJson(value)
    }
}
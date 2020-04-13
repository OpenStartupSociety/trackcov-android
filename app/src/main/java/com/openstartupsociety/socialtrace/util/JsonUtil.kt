package com.openstartupsociety.socialtrace.util

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

object JsonUtil {

    private val moshi by lazy { Moshi.Builder().build() }

    fun <T> fromJson(json: String, type: Class<T>): T? {
        val adapter: JsonAdapter<T> = moshi.adapter(type)
        return adapter.fromJson(json)
    }

    fun toJson(bean: Any): String {
        val adapter = moshi.adapter(bean.javaClass)
        return adapter.toJson(bean)
    }

    fun <T> listFromJson(json: String, type: Class<T>): List<T>? {
        val listTypes = Types.newParameterizedType(List::class.java, type)
        val adapter = moshi.adapter<List<T>>(listTypes)
        return adapter.fromJson(json)
    }

    fun listToJson(list: List<Any>): String {
        val listTypes = Types.newParameterizedType(List::class.java, Any::class.java)
        val adapter = moshi.adapter<List<Any>>(listTypes)
        return adapter.toJson(list)
    }
}

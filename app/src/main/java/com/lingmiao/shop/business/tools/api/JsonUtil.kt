package com.lingmiao.shop.business.tools.api

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class JsonUtil private constructor() {

    private var json : Gson;

    init {
        json = Gson();
    }

    fun getGson() : Gson {
        return json;
    }

    companion object {
        val instance : JsonUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            JsonUtil()
        }
    }

    fun toJson(string: Any) : String {
        return json.toJson(string);
    }

    fun fromJson(string: String) : Map<String, Map<String, Any>> {
        return json?.getAdapter(TypeToken.get(LinkedTreeMap<String, LinkedTreeMap<String, Any>>()::class.java)).fromJson(string);
    }

    fun <T> fromJson(json: String?, classOfT: Class<T>?): T? {
        return if (TextUtils.isEmpty(json) || TextUtils.equals("null", json)) {
            null
        } else try {
            instance.fromJson(json, classOfT)
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    fun <T> fromJson(json: String?, type: Type?): T? {
        return if (TextUtils.isEmpty(json) || TextUtils.equals("null", json)) {
            null
        } else instance.fromJson(json, type)
    }

}
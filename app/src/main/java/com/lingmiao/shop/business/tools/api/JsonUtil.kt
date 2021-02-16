package com.lingmiao.shop.business.tools.api

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken

class JsonUtil private constructor() {

    private var json : Gson;

    init {
        json = Gson();
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
}
package com.constant.japaneseeveryday.util

import android.content.Context
import com.constant.japaneseeveryday.model.Kanji
import com.constant.japaneseeveryday.model.Vocabulary
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject

class JsonParser {

    // JSON 파일을 읽고 파싱하여 HashMap으로 변환
    fun parseJsonToHashMap(context: Context, fileName: String): HashMap<String, String> {
        val hashMap = HashMap<String, String>()

        try {
            // JSON 파일 읽기
            val jsonString = loadJsonFromAsset(context, fileName)

            // JSON 파싱
            val jsonObject = JSONObject(jsonString)
            val keys = jsonObject.keys()

            while (keys.hasNext()) {
                val key = keys.next()
                val value = jsonObject.getString(key)
                hashMap[key] = value
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return hashMap
    }
    fun parseJsonArrayToList(context: Context, fileName: String): List<HashMap<String, String>> {
        val list = mutableListOf<HashMap<String, String>>()

        try {
            val jsonString = loadJsonFromAsset(context, fileName)

            if (jsonString.trim().startsWith("[")) {
                val jsonArray = JSONArray(jsonString)

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val map = HashMap<String, String>()
                    val keys = jsonObject.keys()

                    while (keys.hasNext()) {
                        val key = keys.next()
                        val value = jsonObject.getString(key)
                        map[key] = value
                    }

                    list.add(map)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return list
    }
    // assets에서 JSON 파일을 읽어 문자열로 반환
    fun loadJsonFromAsset(context: Context, fileName: String): String {
        return try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            String(buffer, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun decodeJSONtoKanjiArray(jsonData: String): List<Kanji> {
        return try {
            val type = object : TypeToken<List<Kanji>>() {}.type
            Gson().fromJson(jsonData, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun decodeJSONtoVocabularyArray(jsonData: String): List<Vocabulary> {
        return try {
            val type = object : TypeToken<List<Vocabulary>>() {}.type
            Gson().fromJson(jsonData, type)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
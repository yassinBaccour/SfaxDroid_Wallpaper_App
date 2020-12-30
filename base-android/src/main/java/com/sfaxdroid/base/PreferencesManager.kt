package com.sfaxdroid.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import java.io.*

class PreferencesManager(val context: Context, private var prefName: String) {

    inline operator fun <reified T : Any> get(key: String, default: T): T {
        return getSharedPreferences(key, default)
    }

    inline operator fun <reified T : Any> set(key: String, default: T?) {
        putSharedPreferences(key, default)
    }

    fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        getPref().registerOnSharedPreferenceChangeListener(listener)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getSharedPreferences(name: String, default: T): T = with(getPref()) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default) ?: ""
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> deSerialization(getString(name, serialize(default)) ?: "")
        }
        return res as T
    }

    @SuppressLint("CommitPrefEdits")
    fun <T> putSharedPreferences(name: String, value: T) = with(getPref().edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> putString(name, serialize(value))
        }.apply()
    }

    @Throws(IOException::class)
    private fun <A> serialize(obj: A): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(
            byteArrayOutputStream
        )
        objectOutputStream.writeObject(obj)
        var serStr = byteArrayOutputStream.toString("ISO-8859-1")
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8")
        objectOutputStream.close()
        byteArrayOutputStream.close()
        return serStr
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(IOException::class, ClassNotFoundException::class)
    private fun <A> deSerialization(str: String): A {
        val redStr = java.net.URLDecoder.decode(str, "UTF-8")
        val byteArrayInputStream = ByteArrayInputStream(
            redStr.toByteArray(charset("ISO-8859-1"))
        )
        val objectInputStream = ObjectInputStream(
            byteArrayInputStream
        )
        val obj = objectInputStream.readObject() as A
        objectInputStream.close()
        byteArrayInputStream.close()
        return obj
    }

    fun contains(key: String): Boolean {
        return getPref().contains(key)
    }

    private fun getPref(): SharedPreferences {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }


}
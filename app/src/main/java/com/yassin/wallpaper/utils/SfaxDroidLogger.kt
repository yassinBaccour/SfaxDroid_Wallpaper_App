package com.yassin.wallpaper.utils

import android.os.Build
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.sfaxdroid.data.entity.Logger
import timber.log.Timber
import java.util.regex.Pattern
import javax.inject.Inject


class SfaxDroidLogger @Inject constructor() : Logger {

    fun setup(debugMode: Boolean) {
        if (debugMode) {
            Timber.plant(FmmDebugTree())
        }
        Timber.plant(CrashlyticsTree())
    }

    override fun v(message: String, vararg args: Any?) {
        Timber.v(message, *args)
    }

    override fun v(t: Throwable, message: String, vararg args: Any?) {
        Timber.v(t, message, *args)
    }

    override fun v(t: Throwable) {
        Timber.v(t)
    }

    override fun d(message: String, vararg args: Any?) {
        Timber.d(message, *args)
    }

    override fun d(t: Throwable, message: String, vararg args: Any?) {
        Timber.d(t, message, *args)
    }

    override fun d(t: Throwable) {
        Timber.d(t)
    }

    override fun i(message: String, vararg args: Any?) {
        Timber.i(message, *args)
    }

    override fun i(t: Throwable, message: String, vararg args: Any?) {
        Timber.i(t, message, *args)
    }

    override fun i(t: Throwable) {
        Timber.i(t)
    }

    override fun w(message: String, vararg args: Any?) {
        Timber.w(message, *args)
    }

    override fun w(t: Throwable, message: String, vararg args: Any?) {
        Timber.w(t, message, *args)
    }

    override fun w(t: Throwable) {
        Timber.w(t)
    }

    override fun e(message: String, vararg args: Any?) {
        Timber.e(message, *args)
    }

    override fun e(t: Throwable, message: String, vararg args: Any?) {
        Timber.e(t, message, *args)
    }

    override fun e(t: Throwable) {
        Timber.e(t)
    }

    override fun wtf(message: String, vararg args: Any?) {
        Timber.wtf(message, *args)
    }

    override fun wtf(t: Throwable, message: String, vararg args: Any?) {
        Timber.wtf(t, message, *args)
    }

    override fun wtf(t: Throwable) {
        Timber.wtf(t)
    }
}

private class FmmDebugTree : Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, createClassTag(), message, t)
    }

    private fun createClassTag(): String {
        val stackTrace = Throwable().stackTrace
        if (stackTrace.size <= CALL_STACK_INDEX) {
            throw IllegalStateException("Synthetic stacktrace didn't have enough elements: are you using proguard?")
        }
        var tag = stackTrace[CALL_STACK_INDEX].className
        val m = ANONYMOUS_CLASS.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }
        tag = tag.substring(tag.lastIndexOf('.') + 1)
        return if (tag.length <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= 24) {
            tag
        } else tag.substring(
            0,
            MAX_TAG_LENGTH
        )
    }

    companion object {
        private const val MAX_TAG_LENGTH = 23
        private const val CALL_STACK_INDEX = 7
        private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")
    }
}

private class CrashlyticsTree : Timber.Tree() {
    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return priority >= Log.DEBUG
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.log(message)
    }
}

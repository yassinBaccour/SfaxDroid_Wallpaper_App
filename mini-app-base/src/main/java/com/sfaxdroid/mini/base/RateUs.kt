package com.sfaxdroid.mini.base

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class RateUs(var context: Context, var packageName: String) {

    private val preferences: SharedPreferences = context.getSharedPreferences(Constans.PREF_NAME, 0)

    fun appLaunched() {
        val isRateDialogShowed = preferences.getBoolean(Constans.PREF_RATE_APP_KEY, false)
        if (isRateDialogShowed) {
            return
        } else {
            showRateDialog()
        }
    }

    private fun showRateDialog() {
        Dialog(context).let { dialog ->
            dialog.setCanceledOnTouchOutside(true)
            dialog.setTitle(R.string.txtrate2)
            dialog.setContentView(LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL

                addView(TextView(context).apply {
                    setTextColor(Color.WHITE)
                    textSize = 16f
                    setText(R.string.txtrate1)
                    width = 240
                    setPadding(20, 0, 4, 10)
                })

                addView(Button(context).apply {
                    setText(R.string.txtrate5)
                    setOnClickListener {
                        startRateUs(context, packageName)
                        dialog.dismiss()
                    }
                })

                addView(Button(context).apply {
                    setText(R.string.txtrate3)
                    setOnClickListener { dialog.dismiss() }
                })

                addView(Button(context).apply {
                    setText(R.string.txtrate4)
                    setOnClickListener {
                        setSetting()
                        dialog.dismiss()
                    }
                })
            })
            dialog.show()
        }
    }

    private fun startRateUs(context: Context, packageName: String) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW, Uri
                    .parse("market://details?id=$packageName")
            )
        )
    }

    private fun setSetting() {
        preferences.edit().apply {
            putBoolean(Constans.PREF_RATE_APP_KEY, true)
            apply()
        }
    }
}
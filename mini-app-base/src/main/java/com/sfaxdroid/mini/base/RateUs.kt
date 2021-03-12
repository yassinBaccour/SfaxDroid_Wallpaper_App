package com.sfaxdroid.mini.base

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class RateUs(var context: Context, var packageName: String) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(BaseConstants.PREF_NAME, 0)

    fun appLaunched() {
        val isRateDialogShowed = preferences.getBoolean(BaseConstants.PREF_RATE_APP_KEY, false)
        if (isRateDialogShowed) {
            return
        } else {
            showRateDialog()
        }
    }

    private fun showRateDialog() {
        Dialog(context).let { dialog ->
            dialog.setCanceledOnTouchOutside(true)
            dialog.setTitle(R.string.rating_box_title)
            dialog.setContentView(LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL

                addView(TextView(context).apply {
                    textSize = 16f
                    setText(R.string.rating_box_description)
                    width = 240
                    setPadding(20, 0, 4, 10)
                })

                addView(Button(context).apply {
                    setText(R.string.rating_box_rate)
                    setOnClickListener {
                        startRateUs(context, packageName)
                        dialog.dismiss()
                    }
                })

                addView(Button(context).apply {
                    setText(R.string.rating_box_later)
                    setOnClickListener { dialog.dismiss() }
                })

                addView(Button(context).apply {
                    setText(R.string.rating_box_never)
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
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri
                        .parse("market://details?id=$packageName")
                )
            )
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                context,
                " unable to find market app",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setSetting() {
        preferences.edit().apply {
            putBoolean(BaseConstants.PREF_RATE_APP_KEY, true)
            apply()
        }
    }
}
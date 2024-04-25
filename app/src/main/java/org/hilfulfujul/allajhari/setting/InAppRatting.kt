package org.hilfulfujul.allajhari.setting

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog

object InAppRatting {
    private const val MINIMUM_DAYS_BEFORE_PROMPT = 7

    fun promptAppRating(activity: Activity) {
        val sharedPreferences = activity.getSharedPreferences("apprating", Context.MODE_PRIVATE)
        val lastPromptDate = sharedPreferences.getLong("lastPromptDate", 0)

        if (System.currentTimeMillis() - lastPromptDate >= MINIMUM_DAYS_BEFORE_PROMPT * 24 * 60 * 60 * 1000) {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Enjoying the app?")
                .setMessage("If you enjoy using the app, please take a moment to rate it. Thank you for your support!")
                .setPositiveButton("Rate Now") { _, _ ->
                    // Open app store link for rating
                    val appPackageName = activity.packageName
                    try {
                        activity.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=$appPackageName")
                            )
                        )
                    } catch (e: ActivityNotFoundException) {
                        activity.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                            )
                        )
                    }

                    // Save last prompt date
                    sharedPreferences.edit().putLong("lastPromptDate", System.currentTimeMillis())
                        .apply()
                }.setNegativeButton("No, Thanks") { _, _ ->
                    // User declined to rate the app, save the last prompt date
                    sharedPreferences.edit().putLong("lastPromptDate", System.currentTimeMillis())
                        .apply()
                }.setNeutralButton("Remind Me Later") { _, _ ->
                    // Remind user later, no action needed here
                }.show()
        }
    }
}
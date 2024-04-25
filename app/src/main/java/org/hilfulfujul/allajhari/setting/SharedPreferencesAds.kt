package org.hilfulfujul.allajhari.setting

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesAds(context: Context) {
    private val PREFS_NAME = "mySharedPreferences" // Name of your preferences file

    companion object {
        val BANNER_ADS_KEY = "BANNER_ADS"
        val BANNER_ADS_ID = "BANNER_ID"
        val INTERSTITIAL_ADS_KEY = "INTERSTITIAL_ADS"
        val INTERSTITIAL_ADS_ID = "INTERSTITIAL_ID"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String? = null): String? {
        return prefs.getString(key, defaultValue)
    }

    fun saveBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }
}
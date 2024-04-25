package org.hilfulfujul.allajhari.setting

import android.content.Context
import android.net.ConnectivityManager

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}
package org.hilfulfujul.allajhari.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import org.hilfulfujul.allajhari.R

fun Context.gotoUrlRedirect(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(intent)
}

fun Context.loadUrlInApp(url: String) {
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(this, Uri.parse(url))
}

private var mCloseAppListener: CloseApplication? = null
fun Context.exitAlertApplication(closeAppListener: CloseApplication) {
    mCloseAppListener = closeAppListener
    AlertDialog.Builder(this).setTitle("Alert Dialog Application.")
        .setMessage("আপনি কি এখান থেকে বের হতে চান?").setIcon(R.drawable.ic_close_icon)
        .setCancelable(false).setPositiveButton("হ্যাঁ") { _, _ ->
            mCloseAppListener?.onCloseApplication()
        }.setNegativeButton("না") { dialog, _ ->
            dialog.cancel()
        }.show()
}

interface CloseApplication {
    fun onCloseApplication()
}

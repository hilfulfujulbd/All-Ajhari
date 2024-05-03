package org.hilfulfujul.allajhari.notification

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.hilfulfujul.allajhari.R
import org.hilfulfujul.allajhari.setting.createNotificationChannel


class MyMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("Notification", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        showNotification(this, message)
    }

    private fun showNotification(context: Context, message: RemoteMessage) {
        val title = message.notification!!.title
        val body = message.notification!!.body
        val channel = message.notification!!.channelId ?: "1712782075"

        if (channel != "1712782075") {
            createNotificationChannel(
                context, channel, channel, NotificationManager.IMPORTANCE_DEFAULT
            )
        }

        val notificationBuilder =
            NotificationCompat.Builder(context, channel).setSmallIcon(R.drawable.ic_contact_us)
                .setContentTitle(title).setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notification = notificationBuilder.build()

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(context).notify(1, notification)
    }
}
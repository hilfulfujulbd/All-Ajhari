package org.hilfulfujul.allajhari

import android.app.Application
import android.app.NotificationManager
import org.hilfulfujul.allajhari.setting.createNotificationChannel

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()

        createNotificationChannel(
            this,
            "1712782075",
            "Notification Default",
            NotificationManager.IMPORTANCE_HIGH
        )
    }
}
package org.hilfulfujul.allajhari.setting

import android.app.Activity
import android.content.IntentSender
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

const val MY_REQUEST_CODE = 100
object AppUpdateHelper {

    fun checkForAppUpdate(activity: Activity) {
        val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(activity)
        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                startAppUpdateFlow(activity, appUpdateManager, appUpdateInfo)
            } else {
                Toast.makeText(activity, "No update available yet!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startAppUpdateFlow(activity: Activity, appUpdateManager: AppUpdateManager, appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, activity, MY_REQUEST_CODE)
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }
    }

    fun handleUpdateResult(activity: Activity, resultCode: Int) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                Toast.makeText(activity, "Update successful!", Toast.LENGTH_SHORT).show()
                // Update successful, handle it accordingly
            }
            Activity.RESULT_CANCELED -> {
                Toast.makeText(activity, "Update canceled!", Toast.LENGTH_SHORT).show()
                // Update canceled by user, handle it accordingly
            }
            ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                Toast.makeText(activity, "Update failed!", Toast.LENGTH_SHORT).show()
                // Update failed, handle it accordingly
            }
        }
    }
}
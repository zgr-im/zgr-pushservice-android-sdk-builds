package im.zgr.pushservice.app

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import im.zgr.pushservice.domain.dto.NotificationCustomActionDto
import im.zgr.pushservice.domain.dto.NotificationDto
import im.zgr.pushservice.utils.IntentHelper


abstract class MainService : Service() {

    abstract fun createNotificationIntent(): Intent

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val currentNotification: NotificationDto? = IntentHelper.getNotificationFromIntent(intent)
            val currentCustomAction: NotificationCustomActionDto? = IntentHelper.getCustomActionFromIntent(intent)
            when (intent.action?.replace(String.format("%s-", packageName), "")) {
                 "link" -> {
                    val notificationIntent = createNotificationIntent()
                    notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    notificationIntent.putExtra("content_url", currentCustomAction?.options)
                    startActivities(arrayOf(notificationIntent))
                    sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
                }
                "open-app" -> {
                    startActivity(packageManager.getLaunchIntentForPackage(applicationContext.packageName))
                    sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
                }
                "clear" -> {

                }
            }
            if (currentNotification != null) {
                dismissNotification(currentNotification.pushServiceId)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun dismissNotification(notificationId: Int) {
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}

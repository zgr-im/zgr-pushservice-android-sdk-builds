package im.zgr.pushservice.sample

import android.os.Bundle
import android.widget.Toast
import im.zgr.pushservice.NotificationActivityFlutter
import im.zgr.pushservice.NotificationImportance
import im.zgr.pushservice.NotificationSdk

class MainActivity:  NotificationActivityFlutter() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeText("Started")
        registerToken()
    }

    private fun registerToken() = NotificationSdk.getInstance(this).apply {
        setLogsEnabled(true)
        setLocalDatabaseEnabled(true)
        setNotificationIcon(R.mipmap.ic_launcher, android.R.color.white)
        setNotificationImportance(NotificationImportance.High)
        setNotificationChannelName("Очень важные уведомления")
        registerPushToken({
              updateUserProfile(null, "flutter",
                  this@MainActivity::makeText, this@MainActivity::makeText)
        }, this@MainActivity::makeText)
    }

    private fun makeText(v: Any) =
        Toast.makeText(this, v.toString(), Toast.LENGTH_LONG).show()


}

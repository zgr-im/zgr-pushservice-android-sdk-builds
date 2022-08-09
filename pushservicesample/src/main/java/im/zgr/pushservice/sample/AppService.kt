package im.zgr.pushservice.sample

import android.content.Intent
import im.zgr.pushservice.app.MainService

class AppService: MainService() {
    override fun createNotificationIntent() =
        Intent(applicationContext, ViewActivity::class.java)
}
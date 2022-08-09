package im.zgr.pushservice.sample

import android.content.Intent
import im.zgr.pushservice.app.MainActivity

class AppActivity: MainActivity() {

    override fun getAppName() = getString(R.string.app_name)

    override val appVersionName: String
        get() = BuildConfig.VERSION_NAME

    override fun createViewIntent() =
        Intent(this, ViewActivity::class.java)

}

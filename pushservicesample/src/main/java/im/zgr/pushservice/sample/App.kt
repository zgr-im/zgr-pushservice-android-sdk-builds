package im.zgr.pushservice.sample

import android.content.Intent
import im.zgr.pushservice.app.AppBasic
import im.zgr.pushservice.app.BuildConfig

class App: AppBasic() {

    override val sdkVersion: String
        get() = BuildConfig.SDK_VERSION_NAME

    override val appVersion: String
        get() = BuildConfig.VERSION_NAME

    override fun createWebViewIntent() =
        Intent(this, AppWebActivity::class.java)

    override fun createVideoViewIntent() =
        Intent(this, AppVideoActivity::class.java)

}
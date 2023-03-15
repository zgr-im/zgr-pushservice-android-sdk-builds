package im.zgr.pushservice.app

import android.app.Application
import android.content.Intent
import im.zgr.pushservice.domain.dto.NotificationDto

abstract class AppBasic: Application() {

    companion object {
        const val urlName = "urlName"
        const val titleName = "titleName"
        const val textName = "textName"
    }

    abstract val sdkVersion: String
    abstract val appVersion: String
    abstract fun createWebViewIntent(): Intent
    abstract fun createVideoViewIntent(): Intent
    fun startView(
        title: String?, text: String?,
        category: String?, url: String?
    ) = when (category) {
        NotificationDto.YOUTUBE -> createVideoViewIntent()
        else -> createWebViewIntent()
    }.apply {
        putExtra(titleName, title)
        putExtra(textName, text)
        putExtra(urlName, url)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(this)
    }

}
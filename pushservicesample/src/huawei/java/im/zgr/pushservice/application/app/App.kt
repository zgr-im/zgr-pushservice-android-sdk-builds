package im.zgr.pushservice.application.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import im.zgr.pushservice.MessagingService

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        // только для отправки сообщений
        MessagingService.init(this,"103798949",
        "db17c7763bdec28ab93cae97bda59bcbf9942dabc606aea293f68433dfd01a47")
    }

}
package im.zgr.pushservice.application.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import im.zgr.pushservice.MessagingService

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        MessagingService.init(this,
            "72TEWoFTtTDt8z7kzkl2HWYc9sQhW3Kg",
            "EEaRxQqe39pHmN8ff8EVVQKkrZjdlGAPCenTyfy1Ap36NXci8-BZaVtCP1cvhosA")
    }

}
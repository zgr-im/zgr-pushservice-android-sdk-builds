package im.zgr.pushservice.application.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import im.zgr.pushservice.MessagingService

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        MessagingService.init(this, "zagruzka-aba49",
            "AAAAiCDBZQ8:APA91bGQRrc3VSBPSVdORd3YNqII_cdkJQ2Oo9kO02MLYR7t5v13nEcwt4kyZAkvg9oalG6Hh9BDiGDWylkDoitRD3he4T1WnChMejZDitH-Gv8ZdOjXe3fuPpwWkeb8kgthK7NQYCjB")
    }

}
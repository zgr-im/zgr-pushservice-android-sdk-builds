package im.zgr.pushservice.sample

import android.content.Intent
import im.zgr.pushservice.app.MainActivity

class AppActivity: MainActivity() {

    override fun openSettingsActivity() {
        startActivity(Intent(this, PrefsActivity::class.java))
    }

}

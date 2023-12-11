package im.zgr.pushservice.application.ui

import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import im.zgr.pushservice.NotificationSdk
import im.zgr.pushservice.application.R
import im.zgr.pushservice.application.app.AppAct
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class AppActivity: AppAct() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragment(MainView())
    }

    override fun onNewIntent(intent: Intent) {
        goToMainScreen()
        super.onNewIntent(intent)
    }

    private fun goToMainScreen() = findNavController(R.id.nav).apply {
        while (backQueue.size > 0 && currentDestination?.id != R.id.mainContentView) navigateUp()
    }

    override fun onBackPressed() {
        when(findNavController(R.id.nav).currentDestination?.id) {
            R.id.loginView -> finish()
            R.id.mainContentView -> finish()
            else -> super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        subscribeOnMessages()
    }

    override fun onPause() {
        unsubscribeOnMessages()
        super.onPause()
    }

    private fun subscribeOnMessages() = NotificationSdk.getInstance(this).apply {
        subscribeOnAppMessage { say(String.format("Поступило сообщение приложения %s", it.toString())) }
        subscribeOnAppMessageAction { say(String.format("Действие с push сообщением %s", it.notificationEvent.name)) }
    }

    private fun unsubscribeOnMessages() = NotificationSdk.getInstance(this).apply {
        subscribeOnAppMessageAction(null)
        subscribeOnAppMessage(null)
    }

}
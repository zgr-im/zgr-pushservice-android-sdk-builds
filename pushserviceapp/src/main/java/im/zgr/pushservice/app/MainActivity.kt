package im.zgr.pushservice.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import im.zgr.pushservice.NotificationActivity
import im.zgr.pushservice.NotificationImportance
import im.zgr.pushservice.NotificationSdk
import im.zgr.pushservice.domain.dto.NotificationDto
import im.zgr.pushservice.utils.IntentHelper
import java.util.*

abstract class MainActivity: NotificationActivity() {

    protected val lineToken: LinearLayout by lazy { findViewById(R.id.line_token) }
    protected val lineCustomPayload: LinearLayout by lazy { findViewById(R.id.line_custom_payload) }

    private val btnShowTestNotification: TextView by lazy { findViewById(R.id.btnShowTestNotification) }
    private val tokenTextView: TextView by lazy { findViewById(R.id.current_push_token) }
    private val customPayloadTextView: TextView by lazy { findViewById(R.id.custom_payload) }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        registerToken()
        btnShowTestNotification.visibility = View.GONE
        btnShowTestNotification.setOnClickListener { showTestNotification() }
        checkWebView(intent)

    }

    override fun onResume() {
        super.onResume()
        NotificationSdk.getInstance(this).apply {
            subscribeOnPushMessage {
                say(String.format("Поступило сообщение push %s", it.toString()))
            }
            subscribeOnAppMessage {
                say(String.format("Поступило сообщение приложения %s", it.toString()))
            }
            subscribeOnMessageAction {
                say(String.format("Действие с push сообщением %s", it.toString()))
            }
        }
    }

    override fun onPause() {
        NotificationSdk.getInstance(this).apply {
            subscribeOnPushMessage()
            subscribeOnAppMessage()
            subscribeOnMessageAction()
        }; super.onPause()
    }

    private fun say(text: String) =
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()

    override fun onNewIntentExtra(intent: Intent?) {
        super.onNewIntentExtra(intent)
        if (intent != null) {
            lineCustomPayload.visibility = View.GONE
            this.checkWebView(intent)
        }
    }

    private fun checkWebView(intent: Intent) {
        val currentNotification: NotificationDto? = IntentHelper.getNotificationFromIntent(intent)
        if (currentNotification != null) {

            if (currentNotification.customPayload != null) {
                customPayloadTextView.text = currentNotification.customPayload
                lineCustomPayload.visibility = View.VISIBLE
            }

            if (currentNotification.contentUrl != null) {
                val title = currentNotification.title
                val text = currentNotification.text
                val category = currentNotification.contentCategory?.toLowerCase(Locale.getDefault())
                val app = applicationContext as AppBasic
                val url = currentNotification.contentUrl.toString()
                app.startView(title, text, category, url)
            }

        }
    }

    // Uncomment only when pushservice module dependency is used
    private fun showTestNotification() {
//        val serviceLocator = im.zgr.pushservice.di.ServiceLocator(this)
//        val notificationShower = serviceLocator.getNotificationShower()
//        val id = random.nextInt(10000)
//        val notificationDto = NotificationDto(id, "${System.currentTimeMillis()}\n" +
//                "private val sendButton: Button by lazy { findViewById<Button>(R.id.phone_number_button) }\n" +
//                "    private val phoneNumberEditText: EditText by lazy { findViewById<EditText>(R.id.phone_number) }\n" +
//                "    private val tokenTextView: TextView by lazy { findViewById<TextView>(R.id.current_push_token) }\n" +
//                "    private val btnShowTestNotification: TextView by lazy { findViewById<TextView>(R.id.btnShowTestNotification) }\n")
//        notificationShower.showNotification(notificationDto, this)
    }

    abstract fun openSettingsActivity()

    @SuppressLint("SetTextI18n")
    private fun registerToken() {
        NotificationSdk.getInstance(this)
            .setLogsEnabled(true)
            .setLocalDatabaseEnabled(true)
            .setNotificationIconResId(R.drawable.notification_icon)
            .setNotificationImportance(NotificationImportance.High)
            .setNotificationChannelName("Очень важные уведомления")
            .registerPushToken({ token ->
                tokenTextView.text = token
            }, {
                tokenTextView.text = "Ошибка при регистрации токена $it"
            })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                openSettingsActivity()
                true
            } else -> true
        }
    }

}

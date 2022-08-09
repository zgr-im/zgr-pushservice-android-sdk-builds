package im.zgr.pushservice.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import im.zgr.pushservice.NotificationImportance
import im.zgr.pushservice.NotificationSdk
import im.zgr.pushservice.domain.dto.NotificationDto
import im.zgr.pushservice.utils.IntentHelper
import java.util.*

abstract class MainActivity : AppCompatActivity() {

    val connectionButton: Button by lazy { findViewById<Button>(R.id.connection_button) }
    val settingsButton: Button by lazy { findViewById<Button>(R.id.settings_button) }

    private val btnShowTestNotification: TextView by lazy { findViewById<TextView>(R.id.btnShowTestNotification) }
    private val lineCustomPayload: LinearLayout by lazy { findViewById<LinearLayout>(R.id.line_custom_payload) }
    private val notificationHistoryButton: Button by lazy { findViewById<Button>(R.id.notification_history_button) }
    private val tokenTextView: TextView by lazy { findViewById<TextView>(R.id.current_push_token) }
    private val customPayloadTextView: TextView by lazy { findViewById<TextView>(R.id.custom_payload) }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .replace(R.id.loadProfile, UserLoadFragment()).commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.saveProfile, UserSaveFragment()).commit()

        setContentView(R.layout.activity_main)
        setVersion()
        registerToken()

        connectionButton.setOnClickListener { openConnectionActivity() }
        settingsButton.setOnClickListener { openSettingsActivity() }
        notificationHistoryButton.setOnClickListener { openNotificationHistoryActivity() }
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

    abstract val appVersionName: String
    abstract fun createViewIntent(): Intent

    private fun setVersion() {
        title = (getAppName() + " (sdk=%s app=%s)").format(
            BuildConfig.SDK_VERSION_NAME,
            appVersionName
        )
    }

    abstract fun getAppName(): String

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
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

            val category = currentNotification.contentCategory?.toLowerCase(Locale.getDefault())
            val show = category == "html" || category == "image"
            if (show && currentNotification.contentUrl != null) {
                val notificationIntent = createViewIntent()
                notificationIntent.putExtra("content_url", currentNotification.contentUrl.toString())
                startActivity(notificationIntent)
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

    private fun openConnectionActivity() {
        startActivity(Intent(this, ConnectionActivity::class.java))
    }

    private fun openSettingsActivity() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun openNotificationHistoryActivity() {
        startActivity(Intent(this, NotificationHistoryActivity::class.java))

    }

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

}

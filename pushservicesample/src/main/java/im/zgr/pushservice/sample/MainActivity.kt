package im.zgr.pushservice.sample

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import im.zgr.pushservice.NotificationImportance
import im.zgr.pushservice.NotificationSdk
import im.zgr.pushservice.domain.dto.NotificationDto
import im.zgr.pushservice.utils.IntentHelper

class MainActivity : AppCompatActivity() {

    private val saveUserButton: Button by lazy { findViewById<Button>(R.id.save_user_button) }
    private val personalizeButton: Button by lazy { findViewById<Button>(R.id.personalize_button) }
    private val settingsButton: Button by lazy { findViewById<Button>(R.id.settings_button) }
    private val notificationHistoryButton: Button by lazy { findViewById<Button>(R.id.notification_history_button) }
    private val phoneNumberEditText: EditText by lazy { findViewById<EditText>(R.id.phone_number) }
    private val externalUserIdEditText: EditText by lazy { findViewById<EditText>(R.id.external_user_id) }
    private val tokenTextView: TextView by lazy { findViewById<TextView>(R.id.current_push_token) }
    private val btnShowTestNotification: TextView by lazy { findViewById<TextView>(R.id.btnShowTestNotification) }
    private val logoutButton: Button by lazy { findViewById<Button>(R.id.logout_button) }
    private val lineCustomPayload: LinearLayout by lazy { findViewById<LinearLayout>(R.id.line_custom_payload) }
    private val customPayloadTextView: TextView by lazy { findViewById<TextView>(R.id.custom_payload) }
    private val loadedPhoneNumberEditText: EditText by lazy { findViewById<EditText>(R.id.loaded_phone) }
    private val loadedExternalUserIdEditText: EditText by lazy { findViewById<EditText>(R.id.loaded_external_user_id) }
    private val loadProfileButton: Button by lazy { findViewById<Button>(R.id.load_profile_button) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerToken()

        saveUserButton.setOnClickListener {
            saveUser()
        }
        personalizeButton.setOnClickListener {
            personalize()
        }
        settingsButton.setOnClickListener {
            openSettingsActivity()
        }
        notificationHistoryButton.setOnClickListener {
            openNotificationHistoryActivity()
        }
        btnShowTestNotification.visibility = View.GONE // if (BuildConfig.DEBUG) View.VISIBLE else View.GONE
        btnShowTestNotification.setOnClickListener {
            showTestNotification()
        }
        logoutButton.setOnClickListener { logout() }
        loadProfileButton.setOnClickListener { loadUserProfile() }
        this.checkWebView(intent)

        LocalBroadcastManager.getInstance(this).registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                val messageId = intent.getStringExtra("messageId")
                messageId.isNullOrBlank()
            }}, IntentFilter ("im.zgr.pushservice.message"))

    }

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
            if (currentNotification.contentCategory == "html" && currentNotification.contentUrl != null) {
                val notificationIntent = Intent(this, WebViewActivity::class.java)
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

    private fun logout() {
        NotificationSdk.getInstance(this).logout({
            lineCustomPayload.visibility = View.GONE
            loadedPhoneNumberEditText.setText("не загружен")
            setFieldExternalUserId("не загружен")
            Toast.makeText(this, "Выход: успешно!", Toast.LENGTH_SHORT).show()
        },
            { Toast.makeText(this, "Выход: ошибка - ${it.localizedMessage}", Toast.LENGTH_SHORT).show() })
    }

    private fun openSettingsActivity() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun openNotificationHistoryActivity() {
        startActivity(Intent(this, NotificationHistoryActivity::class.java))

    }

    private fun saveUser() {
        if (phoneNumberEditText.text.isNotBlank()) {
            NotificationSdk.getInstance(this).saveUser(
                phoneNumberEditText.text.toString(),
                { Toast.makeText(this, "Сохранение параметров клиента: успешно!", Toast.LENGTH_SHORT).show() },
                {
                    Toast.makeText(
                        this,
                        "Сохранение параметров клиента: ошибка - ${it.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                })
        } else {
            Toast.makeText(
                this,
                "Сохранение параметров клиента: ошибка - пустое поле Телефон",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun personalize() {
        if (externalUserIdEditText.text.isNotBlank()) {
            NotificationSdk.getInstance(this).personalize(
                externalUserIdEditText.text.toString(),
                { Toast.makeText(this, "Персонализация установки: успешно!", Toast.LENGTH_SHORT).show() },
                {
                    Toast.makeText(
                        this,
                        "Персонализация установки: ошибка - ${it.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                })
        } else {
            Toast.makeText(
                this,
                "Персонализация установки: ошибка - пустое поле ExternalUserId",
                Toast.LENGTH_SHORT
            ).show()
        }

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

    private fun setFieldExternalUserId(value: String?) {
        if (value == null) {
            loadedExternalUserIdEditText.setText("не персонифицированно")
        } else {
            loadedExternalUserIdEditText.setText(value)
        }
    }

    private fun loadUserProfile() {
        NotificationSdk.getInstance(this).getUserProfile({
            loadedPhoneNumberEditText.setText(it.phone)
            setFieldExternalUserId(it.externalUserId)
            Toast.makeText(this, "Загрузка профиля: Успешно!", Toast.LENGTH_SHORT).show()
        },
            { Toast.makeText(this, "Загрузка профиля: ошибка - ${it.localizedMessage}", Toast.LENGTH_SHORT).show() })
    }
}

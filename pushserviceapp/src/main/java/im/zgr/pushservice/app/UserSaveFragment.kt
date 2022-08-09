package im.zgr.pushservice.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import im.zgr.pushservice.NotificationSdk

class UserSaveFragment: AppFragment() {

    private val phoneNumberEditText: EditText by lazy { requireView().findViewById(R.id.phone_number) }
    private val externalUserIdEditText: EditText by lazy { requireView().findViewById(R.id.external_user_id) }
    private val personalizeButton: Button by lazy { requireView().findViewById(R.id.personalize_button) }
    private val saveUserButton: Button by lazy { requireView().findViewById(R.id.save_user_button) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_user_save,
        container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saveUserButton.setOnClickListener { saveUser() }
        personalizeButton.setOnClickListener { personalize() }
    }

    private fun saveUser() {
        if (phoneNumberEditText.text.isNotBlank()) {
            NotificationSdk.getInstance(requireContext()).saveUser(
                phoneNumberEditText.text.toString(),
                { say("Сохранение параметров клиента: успешно!") },
                { say("Сохранение параметров клиента: ошибка - ${it.localizedMessage}") })
        } else say("Сохранение параметров клиента: ошибка - пустое поле Телефон")
    }

    private fun personalize() {
        if (externalUserIdEditText.text.isNotBlank()) {
            NotificationSdk.getInstance(requireContext()).personalize(
                externalUserIdEditText.text.toString(),
                { say("Персонализация установки: успешно!") },
                { say("Персонализация установки: ошибка - ${it.localizedMessage}") })
        } else say("Персонализация установки: ошибка - пустое поле ExternalUserId")
    }

}
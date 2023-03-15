package im.zgr.pushservice.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import im.zgr.pushservice.NotificationSdk

class UserFragment : AppFragment() {

    private val phoneEdit: EditText by lazy { requireView().findViewById(R.id.phone) }
    private val externalUserIdEdit: EditText by lazy { requireView().findViewById(R.id.externalUserID) }

    private val loadProfileButton: Button by lazy { requireView().findViewById(R.id.load_profile) }
    private val logoutButton: Button by lazy { requireView().findViewById(R.id.logout) }
    private val personalize: Button by lazy { requireView().findViewById(R.id.personalize) }
    private val saveUser: Button by lazy { requireView().findViewById(R.id.save_user) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_user,
        container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadProfileButton.setOnClickListener { loadUserProfile() }
        logoutButton.setOnClickListener { logout() }
        saveUser.setOnClickListener { saveUser() }
        personalize.setOnClickListener { personalize() }

    }

    private fun setFieldExternalUserId(value: String?) {
        if (value == null) externalUserIdEdit.setText("не персонифицированно")
        else externalUserIdEdit.setText(value)
    }

    private fun loadUserProfile() {
        NotificationSdk.getInstance(requireContext()).getUserProfile({
            phoneEdit.setText(it.phone)
            setFieldExternalUserId(it.externalUserId)
            say("Загрузка профиля: Успешно!")
        }, {
            say("Загрузка профиля: ошибка - ${it.localizedMessage}")
        })
    }

    private fun logout() {
        NotificationSdk.getInstance(requireContext()).logout({
            phoneEdit.setText("не загружен")
            setFieldExternalUserId("не загружен")
            say("Выход: успешно!")
        }, {
            say("Выход: ошибка - ${it.localizedMessage}")
        })
    }

    private fun saveUser() {
        if (phoneEdit.text.isNotBlank()) {
            NotificationSdk.getInstance(requireContext()).saveUser(
                phoneEdit.text.toString(),
                { say("Сохранение параметров клиента: успешно!") },
                { say("Сохранение параметров клиента: ошибка - ${it.localizedMessage}") })
        } else say("Сохранение параметров клиента: ошибка - пустое поле Телефон")
    }

    private fun personalize() {
        if (externalUserIdEdit.text.isNotBlank()) {
            NotificationSdk.getInstance(requireContext()).personalize(
                externalUserIdEdit.text.toString(),
                { say("Персонализация установки: успешно!") },
                { say("Персонализация установки: ошибка - ${it.localizedMessage}") })
        } else say("Персонализация установки: ошибка - пустое поле ExternalUserId")
    }

}
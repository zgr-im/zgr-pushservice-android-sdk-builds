package im.zgr.pushservice.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import im.zgr.pushservice.NotificationSdk

class UserLoadFragment : AppFragment() {

    private val loadedPhoneNumberEditText: EditText by lazy { requireView().findViewById(R.id.loaded_phone) }
    private val loadedExternalUserIdEditText: EditText by lazy { requireView().findViewById(R.id.loaded_external_user_id) }
    private val loadProfileButton: Button by lazy { requireView().findViewById(R.id.load_profile_button) }
    private val logoutButton: Button by lazy { requireView().findViewById(R.id.logout_button) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_user_load,
        container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadProfileButton.setOnClickListener { loadUserProfile() }
        logoutButton.setOnClickListener { logout() }
    }

    private fun setFieldExternalUserId(value: String?) {
        if (value == null) loadedExternalUserIdEditText.setText("не персонифицированно")
        else loadedExternalUserIdEditText.setText(value)
    }

    private fun loadUserProfile() {
        NotificationSdk.getInstance(requireContext()).getUserProfile({
            loadedPhoneNumberEditText.setText(it.phone)
            setFieldExternalUserId(it.externalUserId)
            say("Загрузка профиля: Успешно!")
        }, {
            say("Загрузка профиля: ошибка - ${it.localizedMessage}")
        })
    }

    private fun logout() {
        NotificationSdk.getInstance(requireContext()).logout({
//            lineCustomPayload.visibility = View.GONE
            loadedPhoneNumberEditText.setText("не загружен")
            setFieldExternalUserId("не загружен")
            say("Выход: успешно!")
        }, {
            say("Выход: ошибка - ${it.localizedMessage}")
        })
    }

}
package im.zgr.pushservice.application.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import im.zgr.pushservice.application.app.AppFragment
import im.zgr.pushservice.application.app.AppModel
import im.zgr.pushservice.domain.dto.UserProfileDto
import im.zgr.pushservice.notification.AbstractMessagingService
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
abstract class LoginFragment: AppFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        act.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setObserving()
        setActions()
        setTitle("")
    }

    private fun setActions() {
        logo.setOnLongClickListener { copyToken(); true }
        login.setOnClickListener {
            val user = user.input.text.toString()
            val phone = phone.input.text.toString()
            val phone1 = phone.replace(Regex("[^\\d.]"), "")
            val phone2 = if(phone1 == "7") "" else phone1
            viewModel.doLogin(sdk, user, phone2.ifEmpty { null })
        }
    }

    private fun setObserving() = viewModel.apply {
        login.observe(viewLifecycleOwner, this@LoginFragment::onLogin)
        loginError.observe(viewLifecycleOwner, this@LoginFragment::onError)
    }

    private fun onLogin(u: UserProfileDto) {
        moveTo(LoginViewDirections.actionLoginViewToMainContentView())
    }

    private fun onError(t: Throwable) {
        say("Сохранение параметров клиента: ошибка - ${t.localizedMessage}")
    }

    abstract val logo: View
    abstract val user: ViewEdit
    abstract val phone: ViewPhone
    abstract val login: View

    private fun copyToken() {
        val token = AppModel.token.value ?: ""
        AbstractMessagingService.toClipBoard(requireContext(), "token", token)
        say(String.format("Токен: %s", token))
    }

}
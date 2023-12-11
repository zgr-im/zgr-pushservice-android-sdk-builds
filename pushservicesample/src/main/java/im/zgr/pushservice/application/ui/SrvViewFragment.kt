package im.zgr.pushservice.application.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import im.zgr.pushservice.Preferences
import im.zgr.pushservice.application.R
import im.zgr.pushservice.application.app.AppFragment
import im.zgr.pushservice.application.app.AppModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
abstract class SrvViewFragment: AppFragment() {

    abstract val apiKeyView: ViewEdit
    abstract val applicationIdView: ViewEdit
    abstract val endpointURLView: ViewEdit

    private val pref: Preferences by
        lazy { Preferences(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        act.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle("Соединение")
        setObserving()
        getData()
        setMenu()
    }

    lateinit var onLogout: () -> Unit
    private fun doLogout() {
        if(settings.externalUserID == null) onLogout()
        else viewModel.doLogout(sdk)
    }

    private fun setObserving() = viewModel.apply {
        logout.observe(viewLifecycleOwner, this@SrvViewFragment::setLogout)
        logoutError.observe(viewLifecycleOwner, this@SrvViewFragment::setLogoutError)
    }

    private fun setLogout(d: Boolean) { onLogout() }
    private fun setLogoutError(t: Throwable) {
        say("Деперсонализация: ошибка - $t")
    }

    private fun getData() = pref.config.configDto.apply {
        applicationIdView.input.setText(pref.applicationId.replace(applicationId, ""))
        endpointURLView.input.setText(pref.endpointURL.replace(endpointURL, ""))
        apiKeyView.input.setText(pref.apiKey.replace(apiKey, ""))
    }

    private fun setMenu() = setMenu(R.menu.menu_server) { when(it) {
        R.id.apply -> onApply()
        R.id.delete -> onReset()
    }}

    private fun onApply() = pref.apply {
        onLogout = {
            applicationId = applicationIdView.input.text.toString()
            endpointURL = endpointURLView.input.text.toString()
            apiKey = apiKeyView.input.text.toString()
            moveTo(SrvViewDirections.actionSrvViewToAppView())
        }; doLogout()
    }

    private fun onReset() = pref.config.configDto.apply {
        onLogout = {
            pref.applicationId = applicationId
            pref.endpointURL = endpointURL
            pref.apiKey = apiKey
            moveTo(SrvViewDirections.actionSrvViewToAppView())
        }; doLogout()
    }

}
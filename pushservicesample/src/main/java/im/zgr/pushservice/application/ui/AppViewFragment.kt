package im.zgr.pushservice.application.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.viewModels
import im.zgr.pushservice.application.R
import im.zgr.pushservice.application.app.AppFragment
import im.zgr.pushservice.application.app.AppModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
abstract class AppViewFragment: AppFragment() {

    abstract val logo: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActions()
        setTitle("")
        setObserving()
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.doToken(sdk)
        }, 1500)
    }

    private fun setActions() = logo.setOnLongClickListener {
        openConnection(); true
    }

    private fun setObserving() = viewModel.apply { AppModel.
        token.observe(viewLifecycleOwner, this@AppViewFragment::onToken)
        tokenError.observe(viewLifecycleOwner, this@AppViewFragment::onError)
    }

    private fun onToken(t: String) {
        if(settings.externalUserID.get() == null) openLogin()
        else openMain().apply { act.onNewIntentExtra(act.intent) }
    }

    private fun onError(t: Throwable) {
        val msg1 = getString(R.string.PushTokenError)
        val msg = String.format(msg1, t.message)
        showError(msg)
    }

    private fun openMain() =
        moveTo(AppViewDirections.actionAppViewToMainContentView())

    private fun openLogin() =
        moveTo(AppViewDirections.actionAppViewToLoginView())

    private fun openConnection() =
        moveTo(AppViewDirections.actionAppViewToSrvView());

    abstract fun showError(v: String)
    fun close() = requireActivity().finish()

}
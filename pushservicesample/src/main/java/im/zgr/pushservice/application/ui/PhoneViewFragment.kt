package im.zgr.pushservice.application.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import im.zgr.pushservice.application.R
import im.zgr.pushservice.application.app.AppFragment
import im.zgr.pushservice.application.app.AppModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
abstract class PhoneViewFragment: AppFragment() {

    private val args: PhoneViewArgs
        get() = PhoneViewArgs.fromBundle(requireArguments())

    lateinit var viewPhone: ViewPhone

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        act.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        delayed(0) { viewPhone.text = args.data }
        setTitle("Номер телефона")
        setObserving()
        setMenu()
    }

    private fun setMenu() = setMenu(R.menu.menu_apply) {
        when(it) { R.id.apply -> viewModel.doUser(sdk, viewPhone.text) }
    }

    private fun setObserving() = viewModel.apply {
        user.observe(viewLifecycleOwner, this@PhoneViewFragment::onUser)
        userError.observe(viewLifecycleOwner, this@PhoneViewFragment::onError)
    }

    private fun onUser(u: Boolean) = u.apply { moveUp() }
    private fun onError(t: Throwable) = say("Сохранение телефона: ошибка - $t")

}
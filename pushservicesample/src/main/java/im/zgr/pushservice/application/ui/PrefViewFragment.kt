package im.zgr.pushservice.application.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import im.zgr.pushservice.domain.dto.InstallationInfoDto
import im.zgr.pushservice.application.R
import im.zgr.pushservice.application.app.AppFragment
import im.zgr.pushservice.application.app.AppModel
import im.zgr.pushservice.domain.dto.UserProfileDto
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
abstract class PrefViewFragment: AppFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        act.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle("Настройки")
        setMenu()
        setVer()
        setObserving()
        viewModel.getInstall(sdk)
        viewModel.getProfile(sdk)
    }

    private fun setObserving() = viewModel.apply {
        install.observe(viewLifecycleOwner, this@PrefViewFragment::setInstall)
        installError.observe(viewLifecycleOwner, this@PrefViewFragment::setInstallError)
        profile.observe(viewLifecycleOwner, this@PrefViewFragment::setProfile)
        profileError.observe(viewLifecycleOwner, this@PrefViewFragment::setProfileError)
        logout.observe(viewLifecycleOwner, this@PrefViewFragment::setLogout)
        logoutError.observe(viewLifecycleOwner, this@PrefViewFragment::setLogoutError)
        apply.observe(viewLifecycleOwner, this@PrefViewFragment::setApply)
        applyError.observe(viewLifecycleOwner, this@PrefViewFragment::setApplyError)
    }

    private fun setInstallError(t: Throwable) {
        say("Получение настроек: ошибка - $t")
    }

    private fun setProfileError(t: Throwable) {
        say("Получение профиля: ошибка - $t")
    }

    private fun setLogout(d: Boolean) {
        moveTo(PrefViewDirections.actionPrefViewToLoginView())
    }

    private fun setLogoutError(t: Throwable) {
        say("Деперсонализация: ошибка - $t")
    }

    private fun setApply(d: Boolean) {
        say("Сохранение настроек: успешно!")
        moveUp()
    }

    private fun setApplyError(t: Throwable) {
        say("Сохранение настроек: ошибка - $t")
    }

    private fun setMenu() = setMenu(R.menu.menu_apply) { when(it) {
        R.id.apply -> onApply()
    }}

    fun onPhone() =
        moveTo(
            PrefViewDirections.actionMessageListViewToPhoneView(
                viewModel.profile.value?.phone ?: ""
            )
        )

    fun onLogout() =
        viewModel.doLogout(sdk)

    private fun onApply() {
        viewModel.doApply(sdk, getApplyData())
    }

    abstract fun getApplyData(): AppModel.ApplyData
    abstract fun setInstall(v: InstallationInfoDto)
    abstract fun setProfile(d: UserProfileDto)
    abstract fun setVer()

}
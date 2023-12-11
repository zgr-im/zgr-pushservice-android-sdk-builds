package im.zgr.pushservice.application.app

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import im.zgr.pushservice.NotificationImportance
import im.zgr.pushservice.NotificationSdk
import im.zgr.pushservice.application.R
import im.zgr.pushservice.domain.dto.InstallationInfoDto
import im.zgr.pushservice.domain.dto.InstallationInfoSettingDto
import im.zgr.pushservice.domain.dto.UserProfileDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
open class AppModel @Inject constructor() : ViewModel() {

    companion object {
        val token = MutableLiveData<String>()
    }

    val wait = MutableLiveData(false)

    val tokenError = MutableLiveData<Throwable>()
    fun doToken(
        notificationSdk: NotificationSdk
    ) {
        wait.value = true
        notificationSdk
            .setLogsEnabled(true)
            .setLocalDatabaseEnabled(true)
            .setNotificationIcon(
                R.drawable.notification_icon,
                R.color.colorWhite)
            .setNotificationImportance(NotificationImportance.High)
            .setNotificationChannelName("Очень важные уведомления")
            .registerPushToken({
                wait.value = false
                token.postValue(it)
            }, {
                wait.value = false
                tokenError.postValue(it)
            })
    }

    val login = MutableLiveData<UserProfileDto>()
    val loginError = MutableLiveData<Throwable>()
    fun doLogin(
        sdk: NotificationSdk,
        user: String, phone: String?
    ) {
        wait.value = true
        sdk.personalize(user, phone, {
            wait.value = false
            login.postValue(it)
        }, {
            wait.value = false
            loginError.postValue(it)
        })
    }

    class Dates(
        var date1: Long? = null,
        var date2: Long? = null
    ): java.io.Serializable {
        companion object {
            const val name: String = "dates"
        }
    }

    val user = MutableLiveData<Boolean>()
    val userError = MutableLiveData<Throwable>()
    fun doUser(
        sdk: NotificationSdk,
        phone: String
    ) {
        wait.value = true
        sdk.personalize(null, phone, {
            wait.value = false
            user.postValue(true)
        }, {
            wait.value = false
            userError.postValue(it)
        })
    }

    val install = MutableLiveData<InstallationInfoDto>()
    val installError = MutableLiveData<Throwable>()
    fun getInstall(sdk: NotificationSdk) {
        wait.value = true
        sdk.getInstallationInfo({
            wait.value = false
            install.postValue(it)
        }, {
            wait.value = false
            installError.postValue(it)
        })
    }

    val profile = MutableLiveData<UserProfileDto>()
    val profileError = MutableLiveData<Throwable>()
    fun getProfile(sdk: NotificationSdk) {
        wait.value = true
        sdk.getUserProfile({
            wait.value = false
            profile.postValue(it)
        }, {
            wait.value = false
            profileError.postValue(it)
        })
    }

    val logout = MutableLiveData<Boolean>()
    val logoutError = MutableLiveData<Throwable>()
    fun doLogout(sdk: NotificationSdk) {
        wait.value = true
        sdk.depersonalize ({
            wait.value = false
            logout.postValue(true)
        }, {
            wait.value = false
            logoutError.postValue(it)
        })
    }

    class ApplyData(
        val pushEnabled: Boolean,
        val settings: List<InstallationInfoSettingDto>,
        val primary: Boolean
    )

    val apply = MutableLiveData<Boolean>()
    val applyError = MutableLiveData<Throwable>()
    fun doApply(sdk: NotificationSdk, ad: ApplyData) {
        wait.value = true
        sdk.updateInstallationInfo (
            ad.pushEnabled,
            ad.settings,
            ad.primary, {
                wait.value = false
                apply.postValue(true)
            }, {
                wait.value = false
                applyError.postValue(it)
            })
    }

    val requestNotification = MutableLiveData<Boolean>()
    val requestNotificationError = MutableLiveData<Throwable>()
    fun requestNotification(sdk: NotificationSdk) {
        wait.value = true
        sdk.requestNotification({
            wait.value = false
            requestNotification.postValue(true)
        }, {
            wait.value = false
            requestNotificationError.postValue(it)
        })
    }

}

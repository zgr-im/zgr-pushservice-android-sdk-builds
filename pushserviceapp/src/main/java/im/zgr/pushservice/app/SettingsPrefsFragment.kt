package im.zgr.pushservice.app

import android.os.Bundle
import android.widget.Toast
import androidx.preference.*
import im.zgr.pushservice.NotificationSdk
import im.zgr.pushservice.domain.dto.InstallationInfoDto
import im.zgr.pushservice.domain.dto.InstallationInfoSettingDto

class SettingsPrefsFragment : PreferenceFragmentCompat() {
        private lateinit var installationInfoDto: InstallationInfoDto

        private fun say(s: String) =
            Toast.makeText(requireContext(), s,
                Toast.LENGTH_SHORT).show()

        fun saveSettings() {
            NotificationSdk.getInstance(requireContext()).updateInstallationInfo(
                installationInfoDto.pushEnabled,
                installationInfoDto.settings,
                installationInfoDto.primary,
                { say("Сохранение настроек: успешно!") },
                { say("Сохранение настроек: ошибка - ${it.localizedMessage}") })
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

            NotificationSdk.getInstance(requireContext()).getInstallationInfo({

                setPreferencesFromResource(R.xml.empty_preference_screen, rootKey)
                val preferenceScreen: PreferenceScreen = this.preferenceScreen

                val preferenceCategoryCommon = PreferenceCategory(preferenceScreen.context)
                preferenceCategoryCommon.setTitle("Общие")
                preferenceScreen.addPreference(preferenceCategoryCommon)

                val preferenceCategorySettings = PreferenceCategory(preferenceScreen.context)
                preferenceCategorySettings.setTitle("Подписки")
                preferenceScreen.addPreference(preferenceCategorySettings)
                //*******************************

                Toast.makeText(requireContext(), "Загрузка настроек: успешно!", Toast.LENGTH_SHORT).show()
                installationInfoDto = it

                val preferenceNotifications = SwitchPreference(preferenceScreen.context)
                preferenceNotifications.title = "Разрешить уведомления"
                preferenceNotifications.setDefaultValue(installationInfoDto.pushEnabled)
                preferenceNotifications.onPreferenceChangeListener =
                    Preference.OnPreferenceChangeListener { preference, newValue ->
                        installationInfoDto.pushEnabled = newValue as Boolean
                        true
                    }
                preferenceCategoryCommon.addPreference(preferenceNotifications)

                val preferencePrimaryDevice = SwitchPreference(preferenceScreen.context)
                preferencePrimaryDevice.title = "Основное устройство"
                preferencePrimaryDevice.setDefaultValue(installationInfoDto.primary)
                preferencePrimaryDevice.onPreferenceChangeListener =
                    Preference.OnPreferenceChangeListener { preference, newValue ->
                        installationInfoDto.primary = newValue as Boolean
                        true
                    }
                preferenceCategoryCommon.addPreference(preferencePrimaryDevice)

                installationInfoDto.settings.forEach {
                    var preference: Preference? = null

                    if (it.type == InstallationInfoSettingDto.SettingType.PERMISSION) {
                        it.value = it.value.toBoolean().toString()
                        preference = SwitchPreference(preferenceScreen.context)
                        preference.setDefaultValue(it.value.toBoolean())
                    }

                    if (it.type == InstallationInfoSettingDto.SettingType.SETTING) {
                        preference = Preference(preferenceScreen.context)
                        preference.setDefaultValue(it.value)
                    }

                    if (preference != null) {
                        preference.title = it.title
                        preference.onPreferenceChangeListener =
                            Preference.OnPreferenceChangeListener { preference, newValue ->
                                it.value = newValue.toString()
                                true
                            }
                        preferenceCategorySettings.addPreference(preference)
                    }
                }

            }, { say("Загрузка настроек: ошибка - ${it.localizedMessage}") })
        }
    }


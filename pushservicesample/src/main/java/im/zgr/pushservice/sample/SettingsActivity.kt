package im.zgr.pushservice.sample

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.*
import im.zgr.pushservice.NotificationSdk
import im.zgr.pushservice.domain.dto.InstallationInfoDto
import im.zgr.pushservice.domain.dto.InstallationInfoSettingDto

class SettingsActivity : AppCompatActivity() {
    private val saveButton: Button by lazy { findViewById<Button>(R.id.save_button) }
    private lateinit var settingsFragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        settingsFragment = SettingsFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, settingsFragment)
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        saveButton.setOnClickListener {
            settingsFragment.saveSettings()
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private lateinit var installationInfoDto: InstallationInfoDto

        fun saveSettings() {
            NotificationSdk.getInstance(context!!).updateInstallationInfo(
                installationInfoDto.pushEnabled,
                installationInfoDto.settings,
                installationInfoDto.primary,
                {
                    Toast.makeText(context!!, "Сохранение настроек: успешно!", Toast.LENGTH_SHORT).show()
                },
                {
                    Toast.makeText(
                        context!!,
                        "Сохранение настроек: ошибка - ${it.localizedMessage}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                })
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

            NotificationSdk.getInstance(context!!).getInstallationInfo({
                setPreferencesFromResource(R.xml.empty_preference_screen, rootKey)
                // create preferences manually
                val preferenceScreen: PreferenceScreen = this.preferenceScreen

                val preferenceCategoryCommon = PreferenceCategory(preferenceScreen.context)
                preferenceCategoryCommon.setTitle("Общие")
                preferenceScreen.addPreference(preferenceCategoryCommon)


                val preferenceCategorySettings = PreferenceCategory(preferenceScreen.context)
                preferenceCategorySettings.setTitle("Подписки")
                preferenceScreen.addPreference(preferenceCategorySettings)
                //*******************************
                Toast.makeText(context!!, "Загрузка настроек: успешно!", Toast.LENGTH_SHORT).show()
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

                installationInfoDto.settings?.forEach {
                    var preference: Preference? = null
                    if (it.type == InstallationInfoSettingDto.SettingType.PERMISSION) {
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

            }, {
                Toast.makeText(context, "Загрузка настроек: ошибка - ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish(); // to close the activity
        }

        return super.onOptionsItemSelected(item)
    }
}

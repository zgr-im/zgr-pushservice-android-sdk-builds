package im.zgr.pushservice.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Switch
import im.zgr.pushservice.NotificationSdk
import im.zgr.pushservice.domain.dto.InstallationInfoDto
import im.zgr.pushservice.domain.dto.InstallationInfoSettingDto

class SettingsTogglesFragment : AppFragment() {

    lateinit var info: InstallationInfoDto

    private val listView: LinearLayout by lazy { requireView().findViewById(R.id.toggle_list) }
    private val pushEnabledView: Switch by lazy { requireView().findViewById(R.id.toggle_pushEnabled) }
    private val mainDeviceView: Switch by lazy { requireView().findViewById(R.id.toggle_mainDevice) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_toggles, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        NotificationSdk.getInstance(requireContext()).getInstallationInfo({ data ->
            info = data; loadSettings()
        })
    }

    fun saveSettings() {
        NotificationSdk.getInstance(requireContext()).updateInstallationInfo(
            info.pushEnabled, info.settings, info.primary,
            { say("Сохранение настроек: успешно!") },
            { say("Сохранение настроек: ошибка - ${it.localizedMessage}") }
        )
    }

    private fun loadSettings() = info.apply {

        pushEnabledView.isChecked = pushEnabled
        pushEnabledView.setOnClickListener { it as Switch
            pushEnabled = it.isChecked
        }

        mainDeviceView.isChecked = primary
        mainDeviceView.setOnClickListener { it as Switch
            primary = it.isChecked
        }

        settings.forEach { setting ->
            if(setting.type == InstallationInfoSettingDto.SettingType.PERMISSION) {
                listView.addView(Switch(context).apply {
                    setOnClickListener { setting.value = isChecked.toString() }
                    isChecked = setting.value.toBoolean()
                    text = setting.title
                })
            }
        }

    }

}
package im.zgr.pushservice.application.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Switch
import androidx.core.view.children
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import im.zgr.pushservice.BuildConfig
import im.zgr.pushservice.domain.dto.InstallationInfoDto
import im.zgr.pushservice.domain.dto.InstallationInfoSettingDto
import im.zgr.pushservice.application.R
import im.zgr.pushservice.application.app.AppModel
import im.zgr.pushservice.application.app.AppViewInt
import im.zgr.pushservice.application.databinding.LayoutPrefBinding
import im.zgr.pushservice.domain.dto.UserProfileDto
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class PrefView: AppViewInt<LayoutPrefBinding>, PrefViewFragment() {

    override val viewModel: AppModel by viewModels()

    private val prefs =  HashMap<String, InstallationInfoSettingDto>()

    override val binding: LayoutPrefBinding by lazy {
        LayoutPrefBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun setVer() {
        binding.apply {
            appVer.text = im.zgr.pushservice.application.BuildConfig.VERSION_NAME
            sdkVer.text = BuildConfig.SDK_VERSION_NAME
        }
    }

    override fun setInstall(v: InstallationInfoDto) {

        val padding = requireContext().resources
            .getDimension(R.dimen.view_marginX1).toInt()

        binding.prefs1.removeAllViews()
        binding.prefs1.addView(Switch(requireContext()).apply {
            setPadding(padding, padding, padding, padding)
            isChecked = v.pushEnabled
            text = getString(R.string.pushEnabled)
            id = R.id.pushEnabled
        })

        binding.prefs1.addView(Switch(requireContext()).apply {
            setPadding(padding, padding, padding, padding)
            isChecked = v.primary
            text = getString(R.string.primaryDevice)
            id = R.id.primaryDevice
        })

        binding.prefs2.removeAllViews()
        v.settings.forEach { prefs[it.title] = it
            binding.prefs2.addView(Switch(requireContext()).apply {
                setPadding(padding, padding, padding, padding)
                isChecked = it.value.toBoolean()
                text = it.title
            })
        }

    }

    override fun setProfile(d: UserProfileDto) {
        binding.apply {
            phone.text = d.phone
            user.text = d.externalUserId
            phone.setOnClickListener { onPhone() }
            logout.setOnClickListener { onLogout() }
            if(d.phone.isNullOrEmpty())
                phone.text = getString(R.string.phone)
        }
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun getApplyData(): AppModel.ApplyData {

        val pushEnabled = requireView().findViewById<Switch>(R.id.pushEnabled)
        val primaryDevice = requireView().findViewById<Switch>(R.id.primaryDevice)
        binding.prefs2.children.forEach { it as Switch
            prefs[it.text]?.apply { value = it.isChecked.toString() }
        }

        return AppModel.ApplyData(
            pushEnabled.isChecked,
            prefs.values.toList(),
            primaryDevice.isChecked)

    }

}
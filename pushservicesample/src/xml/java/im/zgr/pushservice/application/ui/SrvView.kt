package im.zgr.pushservice.application.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import im.zgr.pushservice.application.app.AppViewInt
import im.zgr.pushservice.application.databinding.LayoutSrvBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class SrvView: AppViewInt<LayoutSrvBinding>, SrvViewFragment() {

    override val binding: LayoutSrvBinding by lazy {
        LayoutSrvBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override val apiKeyView: ViewEdit get() = binding.apiKey
    override val applicationIdView: ViewEdit get() = binding.applicationId
    override val endpointURLView: ViewEdit get() = binding.endpointURL

}
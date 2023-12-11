package im.zgr.pushservice.application.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import im.zgr.pushservice.application.app.AppViewInt
import im.zgr.pushservice.application.databinding.LayoutPhoneBinding
import im.zgr.pushservice.application.ui.PhoneViewFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class PhoneView: AppViewInt<LayoutPhoneBinding>, PhoneViewFragment() {

    override val binding: LayoutPhoneBinding by lazy {
        LayoutPhoneBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPhone = binding.phone
    }

}
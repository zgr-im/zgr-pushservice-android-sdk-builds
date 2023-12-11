package im.zgr.pushservice.application.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import im.zgr.pushservice.application.databinding.LayoutLoginBinding
import im.zgr.pushservice.application.app.AppViewInt
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class LoginView: AppViewInt<LayoutLoginBinding>, LoginFragment() {

    override val binding: LayoutLoginBinding by lazy {
        LayoutLoginBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override val logo: View get() = binding.logo
    override val login: View get() = binding.login
    override val user: ViewEdit get() = binding.user
    override val phone: ViewPhone get() = binding.phone

}
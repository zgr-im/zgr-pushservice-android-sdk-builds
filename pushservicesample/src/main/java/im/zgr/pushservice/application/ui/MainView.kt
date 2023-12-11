package im.zgr.pushservice.application.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import im.zgr.pushservice.application.app.AppFragment
import im.zgr.pushservice.application.app.AppModel
import im.zgr.pushservice.application.databinding.LayoutMainBinding
import im.zgr.pushservice.application.app.AppViewInt
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MainView: AppViewInt<LayoutMainBinding>, AppFragment() {

    override val binding: LayoutMainBinding by lazy {
        LayoutMainBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

}
package im.zgr.pushservice.application.ui

import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.method.MovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import im.zgr.pushservice.BuildConfig
import im.zgr.pushservice.application.app.AppViewInt
import im.zgr.pushservice.application.databinding.LayoutAppBinding
import im.zgr.pushservice.application.ui.AppViewFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class AppView: AppViewInt<LayoutAppBinding>, AppViewFragment() {

    override val logo: View get() = binding.logo

    override val binding: LayoutAppBinding by lazy {
        LayoutAppBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.close.setOnClickListener { close () }
        binding.service.text =
            String.format("Для %s", BuildConfig.FLAVOR)
    }

    override fun showError(v: String) {
        binding.error.text = Html.fromHtml(v)
        binding.error.movementMethod = LinkMovementMethod.getInstance()
        binding.errorGroup.isVisible = true
    }

}
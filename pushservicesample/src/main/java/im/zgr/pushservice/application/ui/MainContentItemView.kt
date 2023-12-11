package im.zgr.pushservice.application.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import im.zgr.pushservice.NotificationActivity
import im.zgr.pushservice.application.R
import im.zgr.pushservice.application.app.AppFragment
import im.zgr.pushservice.application.app.AppModel
import im.zgr.pushservice.domain.dto.NotificationDto
import im.zgr.pushservice.domain.dto.NotificationEvent
import im.zgr.pushservice.application.databinding.LayoutMainContentItemBinding
import im.zgr.pushservice.application.app.AppViewInt
import im.zgr.pushservice.domain.dto.NotificationCustomActionDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MainContentItemView: AppViewInt<LayoutMainContentItemBinding>, AppFragment() {

    private val args: MainContentItemViewArgs
        get() = MainContentItemViewArgs.fromBundle(requireArguments())

    override val binding: LayoutMainContentItemBinding by lazy {
        LayoutMainContentItemBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        act.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setBinding()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setBinding() = binding.apply {
        setMenu(R.menu.menu_delete) { when(it) { R.id.delete -> {
            sdk.deleteNotificationFromHistory(args.data.id!!)
            moveUp()
        }}}
        webView.settings.apply {
            javaScriptEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
        }
        setData(args.data)
    }

    private fun setData(notificationDto: NotificationDto) = notificationDto.apply {

        val format = SimpleDateFormat("dd.MM.yyyy HH:mm")
        binding.title.text = title
        binding.text.text = text
        binding.status.text = getDisplayStatus()
        binding.time.text = format.format(date)
        binding.customPayload.text = customPayload
        binding.customPayloadGroup.isVisible = (customPayload != null)
        setTitle(String.format("Пуш %d", this.id))

        if(contentCategory?.lowercase() == NotificationDto.IMAGE) {
            showContent(contentUrl.toString())
        }

        if(contentCategory?.lowercase() == NotificationDto.HTML) {
            showContent(contentUrl.toString())
        }

        notificationDto.customActionList
            ?.let(this@MainContentItemView::showActions)

        NotificationActivity.setSeen(
            requireContext(), notificationDto)

    }

    private fun showContent(url: String) {
        binding.contentGroup.isVisible = true
        binding.webView.loadUrl(url)
        viewModel.wait.value = true
        binding.webView.webViewClient = object: WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                viewModel.wait.value = false
                super.onPageFinished(view, url)
            }
        }
    }

    private fun showActions(actions: List<NotificationCustomActionDto>) {
        var v = ""; val f = "<A href='%s'>%s</A><BR>"
        actions.forEach { if(it.action == "link")
            v += String.format(f, it.options, it.title.ifBlank { it.options })
        }; binding.linksGroup.isVisible = v.isNotEmpty()
        binding.links.apply {
            movementMethod = LinkMovementMethod()
            text = Html.fromHtml(v);
        }
    }


    override fun onResume() {
        super.onResume()
        subscribeOnMessages()
    }

    override fun onPause() {
        unsubscribeOnMessages()
        super.onPause()
    }

    private fun subscribeOnMessages() = sdk.apply {
        subscribeOnAppMessageAction {
            if(it.notificationEvent == NotificationEvent.DELETE) return@subscribeOnAppMessageAction
            setData(it.notificationDto)
        }
    }

    private fun unsubscribeOnMessages() = sdk.apply {
        subscribeOnAppMessageAction(null)
    }

}
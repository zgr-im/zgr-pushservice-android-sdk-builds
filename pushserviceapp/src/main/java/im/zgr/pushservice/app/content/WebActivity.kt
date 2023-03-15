package im.zgr.pushservice.app.content

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.core.view.isVisible
import im.zgr.pushservice.app.AppBasic
import im.zgr.pushservice.app.R

open class WebActivity : ContentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        webView.webViewClient = WebViewClient()
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
    }

    private val titleView: TextView by lazy { findViewById(R.id.titleView) }
    private val textView: TextView by lazy { findViewById(R.id.textView) }
    private val webView: WebView by lazy { findViewById(R.id.webView) }
    override fun start() {
        setData(titleView, intent.getStringExtra(AppBasic.titleName))
        setData(textView, intent.getStringExtra(AppBasic.textName))
        setData(webView, intent.getStringExtra(AppBasic.urlName))
    }

}

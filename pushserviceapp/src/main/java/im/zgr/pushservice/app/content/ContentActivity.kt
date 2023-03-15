package im.zgr.pushservice.app.content

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.webkit.WebView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import im.zgr.pushservice.app.AppBasic
import im.zgr.pushservice.app.R

abstract class ContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.title_activity_content_view)
        Handler(Looper.getMainLooper()).post(this::start)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Handler(Looper.getMainLooper()).post(this::start)
    }

    abstract fun start()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    fun setData(textView: TextView, v: String?) {
        textView.isVisible = false
        v?.let {
            if(it.isEmpty()) return
            textView.isVisible = true
            textView.text = it
        }
    }

    fun setData(webView: WebView, url: String?) {
        webView.isVisible = false
        url?.let {
            if(it == "null") return
            if(it.isEmpty()) return
            webView.isVisible = true
            webView.loadUrl(it)
        }
    }

}

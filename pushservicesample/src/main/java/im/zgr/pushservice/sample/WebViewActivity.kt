package im.zgr.pushservice.sample

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class WebViewActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        webView = findViewById(R.id.fullscreen_content)
        webView.webViewClient = WebViewClient()

        webView.loadUrl(intent.getStringExtra("content_url"));

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        webView.loadUrl(intent?.getStringExtra("content_url"));
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish(); // to close the activity
        }

        return super.onOptionsItemSelected(item)
    }
}

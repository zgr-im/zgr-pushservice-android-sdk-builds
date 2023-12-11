package im.zgr.pushservice.application.ui

import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import android.text.method.LinkMovementMethod
import android.view.Gravity
import androidx.core.text.HtmlCompat

@Composable
fun ViewHtml(html: String, modifier: Modifier = Modifier) {
    AndroidView(modifier = modifier,
        factory = { context -> TextView(context).apply {
            movementMethod = LinkMovementMethod.getInstance()
            gravity = Gravity.CENTER
        }},
        update = { it.text =
            HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
    )
}
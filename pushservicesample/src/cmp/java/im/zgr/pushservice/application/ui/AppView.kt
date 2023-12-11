package im.zgr.pushservice.application.ui

import android.os.Bundle
import android.text.Spannable
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import im.zgr.pushservice.application.R
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class AppView: AppViewFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent { ComposeContent() }}

    private val errorGroupVisible = mutableStateOf(false)
    private val errorText = mutableStateOf("нет ошибок")

    override fun showError(v: String) {
        errorGroupVisible.value = true
        errorText.value = v
    }

    @Preview
    @Composable
    fun ComposeContent() {
        val blueColor = colorResource(R.color.blue_button)
        val sizePadding = dimensionResource(R.dimen.view_marginX1)
        val sizeAppBar = dimensionResource(R.dimen.appBar)
        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()) {
                Image(
                    contentDescription = null,
                    painter = painterResource(R.drawable.logo_blank),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = sizeAppBar)
                        .size(122.dp, 33.dp))
                Column(
                    modifier = Modifier
                        .padding(sizePadding)
                        .align(Alignment.BottomCenter)
                        .alpha(if(errorGroupVisible.value) 1f else 0f)) {
                        ViewHtml(html = errorText.value)
                        ClickableText(
                            text = AnnotatedString("Закрыть"),
                            style = TextStyle(
                                color = blueColor,
                                textAlign = TextAlign.Center),
                            onClick = { close() },
                            modifier = Modifier
                                .padding(sizePadding)
                                .align(Alignment.CenterHorizontally))
            }
        }
    }

}
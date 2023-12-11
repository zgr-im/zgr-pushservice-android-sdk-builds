package im.zgr.pushservice.application.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import dagger.hilt.android.AndroidEntryPoint
import im.zgr.pushservice.application.R
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class PhoneView: PhoneViewFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent { ComposeContent() }}

    @Preview
    @Composable
    fun ComposeContent() {
        val sizeX1 = dimensionResource(R.dimen.view_marginX1)
        Column(modifier = Modifier
                .fillMaxSize()
                .background(Color.White)) {
            AndroidView(
                modifier = Modifier.padding(sizeX1),
                factory = { context -> ViewPhone(context).apply {
                    frame.hint = "Введите номер"
                    viewPhone = this
                }})
        }
    }

}
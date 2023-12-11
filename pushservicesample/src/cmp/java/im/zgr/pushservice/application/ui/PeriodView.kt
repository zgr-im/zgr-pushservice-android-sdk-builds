package im.zgr.pushservice.application.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class PeriodView: PeriodViewFragment() {

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
        val fms = Modifier.fillMaxWidth()
        Column(modifier = fms.background(Color.White), content = {
            AndroidView({CalendarView(it).apply { setOnDateChangeListener {
                _, y, m, d -> dates.date1 = getMillis(y, m, d) }}}, fms)
            AndroidView({CalendarView(it).apply { setOnDateChangeListener {
                _, y, m, d -> dates.date2 = getMillis(y, m, d) }}}, fms)
        })
    }

}
package im.zgr.pushservice.application.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import im.zgr.pushservice.application.app.AppViewInt
import im.zgr.pushservice.application.databinding.LayoutPeriodBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.Calendar

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class PeriodView: AppViewInt<LayoutPeriodBinding>, PeriodViewFragment() {

    override val binding: LayoutPeriodBinding by lazy {
        LayoutPeriodBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.date1.setOnDateChangeListener { _, y, m, d ->
            dates.date1 = getMillis(y, m, d).timeInMillis }
        binding.date2.setOnDateChangeListener { _, y, m, d ->
            dates.date2 = getMillis(y, m, d).apply {
                add(Calendar.SECOND, 24 * 60 * 60 - 1)
            }.timeInMillis }
    }

}
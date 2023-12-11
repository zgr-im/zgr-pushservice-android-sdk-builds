package im.zgr.pushservice.application.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import im.zgr.pushservice.application.R
import im.zgr.pushservice.application.app.AppFragment
import im.zgr.pushservice.application.app.AppModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.Calendar

@ExperimentalCoroutinesApi
abstract class PeriodViewFragment: AppFragment() {

    private val args: PeriodViewArgs
        get() = PeriodViewArgs.fromBundle(requireArguments())

    protected val dates: AppModel.Dates by lazy { args.data }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        act.supportActionBar?.setDisplayHomeAsUpEnabled(true);
        setTitle("Период")
        setMenu()
    }

    private fun setMenu() = setMenu(R.menu.menu_calendar) { when(it) {
        R.id.apply -> { setResult(AppModel.Dates.name, dates); moveUp() }
        R.id.delete -> { setResult(AppModel.Dates.name, AppModel.Dates()); moveUp() }
    }}

    fun getMillis(y: Int, m: Int, d: Int) =
        Calendar.getInstance().apply {
            set(y, m, d, 0, 0, 0)
        }

}
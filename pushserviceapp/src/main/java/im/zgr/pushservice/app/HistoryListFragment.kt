package im.zgr.pushservice.app

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import im.zgr.pushservice.NotificationSdk
import im.zgr.pushservice.domain.dto.NotificationDto
import java.text.SimpleDateFormat
import java.util.*

class HistoryListFragment : Fragment() {

    private val dateFromEditText: EditText by lazy { requireView().findViewById(R.id.date_from) }
    private val dateToEditText: EditText by lazy { requireView().findViewById(R.id.date_to) }
    private val resetButton: Button by lazy { requireView().findViewById(R.id.reset_button) }
    private val deleteAllButton: Button by lazy { requireView().findViewById(R.id.delete_all_button) }
    private val refreshButton: Button by lazy { requireView().findViewById(R.id.refresh_button) }
    private val nextPageButton: Button by lazy { requireView().findViewById(R.id.next_page_button) }
    private var startFromId: Long = 0
    private var dateFrom: Date? = null
    private var dateTo: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.activity_notification_history, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dateFromEditText.setOnClickListener {
            showDatePickerDialog(dateFromEditText)
        }
        dateToEditText.setOnClickListener {
            showDatePickerDialog(dateToEditText)
        }

        resetButton.setOnClickListener {
            resetButtonOnClick()
        }
        deleteAllButton.setOnClickListener {
            deleteAllButtonOnClick()
        }
        refreshButton.setOnClickListener {
            refreshButtonOnClick()
        }
        nextPageButton.setOnClickListener {
            nextPageLoad()
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is HistoryItemFragment) {
            fragment.setFragmentReadyListener {
                nextPageLoad()
            }
            fragment.setItemDeleteListener { notificationDto, index ->
                onDeleteItem(notificationDto)
            }
        }
    }

    private fun onDeleteItem(notificationDto: NotificationDto) {
        NotificationSdk.getInstance(requireContext()).deleteNotificationFromHistory(notificationDto.id!!)
    }

    private fun resetButtonOnClick() {
        dateFromEditText.setText("")
        dateToEditText.setText("")
        dateFrom = null
        dateTo = null
        refreshButtonOnClick()
    }

    private fun getItemFragment(): HistoryItemFragment {
        return childFragmentManager.findFragmentById(R.id.fragment_container_view) as HistoryItemFragment
    }

    private fun deleteAllButtonOnClick() {
        NotificationSdk.getInstance(requireContext()).deleteAllNotificationsFromHistory({
            startFromId = 0
            getItemFragment().setItems(listOf())
        })
    }

    private fun refreshButtonOnClick() {
        startFromId = 0
        getItemFragment().setItems(listOf())
        nextPageLoad()
    }

    private fun nextPageLoad() {
        NotificationSdk.getInstance(requireContext())
            .getNotificationsFromHistory(PAGE_SIZE, startFromId, dateFrom, dateTo, false, {
                if (it.isNotEmpty()) {
                    startFromId = it.last().id!!
                    if (getItemFragment().getItems() != null) {
                        getItemFragment().setItems(getItemFragment().getItems()!! + it)
                    } else {
                        getItemFragment().setItems(it)
                    }
                }
            })

    }

    private fun showDatePickerDialog(editText: EditText) {
        val myCalendar: Calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(), getOnDateSetListener(editText), myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    private fun getOnDateSetListener(editText: EditText): OnDateSetListener {
        return OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val myCalendar: Calendar = Calendar.getInstance()
            myCalendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0)
            myCalendar[Calendar.MILLISECOND] = 0
            editText.setText(SimpleDateFormat("dd.MM.yyyy", Locale.US).format(myCalendar.time))
            if (editText.id == R.id.date_from) {
                dateFrom = myCalendar.time
            } else if (editText.id == R.id.date_to) {
                myCalendar[Calendar.HOUR_OF_DAY] = 23
                myCalendar[Calendar.MINUTE] = 59
                myCalendar[Calendar.SECOND] = 59
                myCalendar[Calendar.MILLISECOND] = 999
                dateTo = myCalendar.time
            }
            refreshButtonOnClick()
        }
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}

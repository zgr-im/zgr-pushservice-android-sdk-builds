package im.zgr.pushservice.app

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import im.zgr.pushservice.NotificationSdk
import im.zgr.pushservice.domain.dto.NotificationDto
import java.text.SimpleDateFormat
import java.util.*


class NotificationHistoryActivity : AppCompatActivity() {
    private val dateFromEditText: EditText by lazy { findViewById<EditText>(R.id.date_from) }
    private val dateToEditText: EditText by lazy { findViewById<EditText>(R.id.date_to) }
    private val resetButton: Button by lazy { findViewById<Button>(R.id.reset_button) }
    private val deleteAllButton: Button by lazy { findViewById<Button>(R.id.delete_all_button) }
    private val refreshButton: Button by lazy { findViewById<Button>(R.id.refresh_button) }
    private val nextPageButton: Button by lazy { findViewById<Button>(R.id.next_page_button) }
    private var startFromId: Long = 0
    private var dateFrom: Date? = null
    private var dateTo: Date? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_history)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
        if (fragment is NotificationHistoryItemFragment) {
            fragment.setFragmentReadyListener {
                nextPageLoad()
            }
            fragment.setItemDeleteListener { notificationDto, index ->
                onDeleteItem(notificationDto)
            }
        }
    }

    private fun onDeleteItem(notificationDto: NotificationDto) {
        NotificationSdk.getInstance(this).deleteNotificationFromHistory(notificationDto.id!!)
    }

    private fun resetButtonOnClick() {
        dateFromEditText.setText("")
        dateToEditText.setText("")
        dateFrom = null
        dateTo = null
        refreshButtonOnClick()
    }

    private fun getItemFragment(): NotificationHistoryItemFragment {
        return supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NotificationHistoryItemFragment
    }

    private fun deleteAllButtonOnClick() {
        NotificationSdk.getInstance(this).deleteAllNotificationsFromHistory({
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
        NotificationSdk.getInstance(this)
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
            this, getOnDateSetListener(editText), myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish(); // to close the activity
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}

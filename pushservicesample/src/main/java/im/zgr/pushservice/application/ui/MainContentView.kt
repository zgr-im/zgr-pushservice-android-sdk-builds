package im.zgr.pushservice.application.ui

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import dagger.hilt.android.AndroidEntryPoint
import im.zgr.pushservice.NotificationSdk
import im.zgr.pushservice.application.R
import im.zgr.pushservice.application.app.AppFragment
import im.zgr.pushservice.application.app.AppModel
import im.zgr.pushservice.application.app.AppViewInt
import im.zgr.pushservice.application.databinding.LayoutMainContentBinding
import im.zgr.pushservice.domain.dto.NotificationActionDto
import im.zgr.pushservice.domain.dto.NotificationDto
import im.zgr.pushservice.domain.dto.NotificationEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MainContentView: AppViewInt<LayoutMainContentBinding>, AppFragment() {

    private var dates = AppModel.Dates()

    override val binding: LayoutMainContentBinding by lazy {
        LayoutMainContentBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    private val adapter = MainContentAdapter (this::openItem, this::deleteItem)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        act.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setTitle("Пуши")
        setObserving()
        setBinding()
    }

    private fun setObserving() = viewModel.apply {
        requestNotification.observe(viewLifecycleOwner, this@MainContentView::onRequestNotification)
        requestNotificationError.observe(viewLifecycleOwner, this@MainContentView::onRequestNotificationError)
    }

    private fun setBinding() = binding.apply {

        // setup menu a
        setMenu(R.menu.menu_main) { when(it) {
            R.id.filter -> openFilter()
            R.id.settings -> openSettings()
        }}

        // setup filtering
        top.list.adapter = adapter
        filterGroup.isVisible = false
        filterReset.setOnClickListener {
            setFilter(AppModel.Dates())
        }; setFilter(dates)

        bottom.pushImage.setOnLongClickListener {
            requestNotification(); true
        }

        // setup swipes
        val callback: ItemTouchHelper.Callback =
            MainContentAdapter.TouchCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(top.list)

        // setup dates apply
        getResult(AppModel.Dates.name,
            this@MainContentView::setFilter)

    }

    private fun setFilter(d: AppModel.Dates) {
        dates = d; onAppMessage()
        if(d.date1 == null || d.date2 == null) {
            binding.bottom.info.text = getString(R.string.messages0Full)
            binding.filterGroup.isVisible = false
        } else {
            binding.bottom.info.text = getString(R.string.messages0Filtered)
            binding.filterGroup.isVisible = true
            val f = SimpleDateFormat("dd MMMM", Locale.getDefault())
            val d1 = f.format(Date(d.date1!!))
            val d2 = f.format(Date(d.date2!!))
            binding.filterDates.text =
                String.format("%s - %s", d1, d2)
        }
    }

    override fun onResume() {
        super.onResume()
        subscribeOnMessages()
        onAppMessage()
    }

    override fun onPause() {
        unsubscribeOnMessages()
        super.onPause()
    }

    private fun subscribeOnMessages() =
        NotificationSdk.getInstance(requireContext()).apply {
        subscribeOnAppMessageAction(this@MainContentView::onAppMessageAction)
        subscribeOnAppMessage(this@MainContentView::onAppMessage)
    }

    private fun unsubscribeOnMessages() =
        NotificationSdk.getInstance(requireContext()).apply {
        subscribeOnAppMessageAction()
        subscribeOnAppMessage()
    }

    private fun onAppMessage(unUsed: Int = 0) {
        val d1 = dates.date1?.let { Date(it) }
        val d2 = dates.date2?.let { Date(it) }
        sdk.getNotificationsFromHistory(Int.MAX_VALUE, 0, d1, d2, false, {
            adapter.setData(it)
            showState()
        })
    }

    private fun showState() {
        val count = adapter.itemCount
        binding.bottom.blankFrame.isVisible = count == 0
        binding.top.listFrame.isVisible = count > 0
    }

    private fun onAppMessageAction(item: NotificationActionDto) {
        when(item.notificationEvent) {
            NotificationEvent.DELETE -> onAppMessage()
            NotificationEvent.CONTENT -> openItem(item.notificationDto)
            NotificationEvent.ACTION -> item.notificationCustomActionDto?.let { act ->
                when(act.action.replace("-app", "")) {
                    "link" -> openItem(item.notificationDto).also { open(act.options) }
                    "open" -> openItem(item.notificationDto)
                }
            }
        }
    }

    private fun openFilter() =
        moveTo(MainContentViewDirections.actionMessageListViewToCalendarView(dates))

    private fun openSettings() =
        moveTo(MainContentViewDirections.actionMessageListViewToPrefView())

    private fun openItem(item: NotificationDto) =
        moveTo(MainContentViewDirections.actionMessageListViewToContentItemView(item))

    private fun deleteItem(item: NotificationDto) {
        sdk.deleteNotificationFromHistory(item.id!!)
        showState()
    }

    private fun requestNotification() = viewModel.requestNotification(sdk)
    private fun onRequestNotificationError(t: Throwable) = t.apply(this::say)
    private fun onRequestNotification(v: Boolean) = v.apply {
        say("Сообщение отправлено")
    }

}
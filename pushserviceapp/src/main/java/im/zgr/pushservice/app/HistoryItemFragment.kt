package im.zgr.pushservice.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import im.zgr.pushservice.app.HistoryListAdapter.SimpleItemTouchHelperCallback
import im.zgr.pushservice.domain.dto.NotificationDto


/**
 * A fragment representing a list of Items.
 */
class HistoryItemFragment : Fragment() {

    private lateinit var recyclerViewAdapter: HistoryListAdapter
    private var fragmentReadyListener: (() -> Unit)? = null
    private lateinit var itemDeleteListener: ((notificationDto: NotificationDto, index: Int) -> Unit)

    private val receiver = object: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            recyclerViewAdapter.update(p1?.getLongExtra("id", 0))
        }
    }

    fun setFragmentReadyListener(listener: (() -> Unit)) {
        fragmentReadyListener = listener
    }

    fun setItemDeleteListener(listener: ((notificationDto: NotificationDto, index: Int) -> Unit)) {
        itemDeleteListener = listener
    }

    fun setItems(items: List<NotificationDto>) {
        recyclerViewAdapter.setItems(items)
    }

    fun getItems(): List<NotificationDto>? {
        return recyclerViewAdapter.getItems()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentReadyListener?.invoke()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false) as RecyclerView
        // Set the adapter
        with(view) {
            layoutManager = LinearLayoutManager(context)
            adapter = HistoryListAdapter()
        }
        recyclerViewAdapter = view.adapter as HistoryListAdapter
        recyclerViewAdapter.setOnDeleteItemListener { notificationDto, index ->
            itemDeleteListener(notificationDto, index)
            recyclerViewAdapter.removeItem(index)
        }

        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(recyclerViewAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(view)

        registerReceiver()
        return view
    }

    override fun onDestroyView() {
        unregisterReceiver()
        super.onDestroyView()
    }

    private fun registerReceiver() =
        requireContext().registerReceiver(
            receiver, IntentFilter("push_status"))

    private fun unregisterReceiver() =
        requireContext().unregisterReceiver(receiver)

}

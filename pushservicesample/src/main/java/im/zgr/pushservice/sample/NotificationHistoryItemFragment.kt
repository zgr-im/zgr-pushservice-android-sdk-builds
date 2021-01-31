package im.zgr.pushservice.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import im.zgr.pushservice.domain.dto.NotificationDto

/**
 * A fragment representing a list of Items.
 */
class NotificationHistoryItemFragment : Fragment() {

    private lateinit var recyclerViewAdapter: MyItemRecyclerViewAdapter
    private var fragmentReadyListener: (() -> Unit)? = null
    private lateinit var itemDeleteListener: ((notificationDto: NotificationDto, index: Int) -> Unit)


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
            adapter = MyItemRecyclerViewAdapter()
        }
        recyclerViewAdapter = view.adapter as MyItemRecyclerViewAdapter
        recyclerViewAdapter.setOnDeleteItemListener { notificationDto, index ->
            itemDeleteListener(notificationDto, index)
            recyclerViewAdapter.removeItem(index)
        }
        return view
    }
}

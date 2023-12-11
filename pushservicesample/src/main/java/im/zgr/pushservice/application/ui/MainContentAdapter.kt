package im.zgr.pushservice.application.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import im.zgr.pushservice.application.R
import im.zgr.pushservice.domain.dto.NotificationDto
import im.zgr.pushservice.application.databinding.LayoutMainContentListItemBinding
import org.joda.time.DateTime
import java.util.*
import kotlin.collections.ArrayList

class MainContentAdapter(
    private val click: (v: NotificationDto) -> Unit,
    private val delete: (v: NotificationDto) -> Unit
) : RecyclerView.Adapter<MainContentAdapter.Holder>() {

    private var list: ArrayList<Pair<DateTime, NotificationDto?>> = arrayListOf()
    fun isDate(pos: Int): Boolean { return list[pos].second == null }

    override fun getItemCount() = list.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(parent)
    override fun onBindViewHolder(
        holder: Holder, position: Int
    ) { list[position].run {

            holder.binding.dateGroup.isVisible = false
            holder.binding.itemGroup.isVisible = false

            if(isDate(position)) {
                holder.binding.dateGroup.isVisible = true
                holder.binding.date.text = first.toString("dd MMMM")
                holder.binding.separatorDate.isVisible = position > 0
            } else second?.apply {
                holder.binding.itemGroup.isVisible = true
                showStatus(holder.binding.status, this)
                holder.itemView.setOnClickListener { click(this) }
                holder.binding.separatorItem.isVisible = true
                holder.binding.time.text = first.toString("HH:mm")
                holder.binding.title.text = title
                holder.binding.text.text = text
                Picasso.with(holder.itemView.context)
                    .load(contentUrl).into(holder.binding.image);
            }

        }
    }

    private fun showStatus(textView: TextView, item: NotificationDto) {
        val c = textView.context
        textView.text = item.getDisplayStatus()
        when(item.status) {
            NotificationDto.Companion.ContentStatus.Delivered.toString() -> {
                textView.setTextColor(ContextCompat.getColor(c, R.color.delivered))
            }
            NotificationDto.Companion.ContentStatus.Opened.toString() -> {
                textView.setTextColor(ContextCompat.getColor(c, R.color.opened))
            }
        }
    }

    class Holder(parent: ViewGroup, val binding: LayoutMainContentListItemBinding =
        LayoutMainContentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    ): RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<NotificationDto>) {

        val dates = data
            .map { DateTime(it.date.time).withTimeAtStartOfDay().plusDays(1).minusMillis(1) }.distinct()
            .map { Pair<DateTime, NotificationDto?>(it, null) }

        val items = data
            .map { Pair<DateTime, NotificationDto?>(DateTime(it.date.time), it)  }

        val listData = ArrayList(items).apply {
            addAll(dates)
            sortByDescending { it.first.millis }
        }

        list = ArrayList(listData)
        notifyDataSetChanged()

    }

    private fun remove(pos: Int) {
        val removed = list.removeAt(pos)
        notifyItemRemoved(pos)
        // Если за эту дату больше нет сообщений - то дату удалить
        if(pos > 0 && isDate(pos - 1) &&
            (pos - 1 == list.lastIndex || isDate(pos)))
                remove(pos - 1)
        removed.second?.also { delete(it) }
    }

    class TouchCallback(
        var adapter: MainContentAdapter
    ): ItemTouchHelper.Callback() {

        override fun isLongPressDragEnabled(): Boolean { return true }
        override fun isItemViewSwipeEnabled(): Boolean { return true }
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return if(adapter.isDate(viewHolder.adapterPosition))
                makeMovementFlags(0, 0)
            else
                makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            adapter.remove(viewHolder.adapterPosition)
        }

    }

}

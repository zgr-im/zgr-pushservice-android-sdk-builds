package im.zgr.pushservice.app

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import im.zgr.pushservice.domain.dto.NotificationDto
import java.util.*

class MyItemRecyclerViewAdapter() : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {
    private var values: MutableList<NotificationDto> = mutableListOf()
    private var onDeleteItem: ((notificationDto: NotificationDto, index: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: NotificationDto = values[position]
        val notificationParts = mutableListOf<String>()
        notificationParts.add("push-id: " + item.pushServiceId.toString())
        notificationParts.add("title: " + item.title)
        notificationParts.add("text: " + item.text)
        notificationParts.add("date: " + item.date)
        notificationParts.add("url: " + item.contentUrl)
        var customPayload: String? = item.customPayload
        if (customPayload != null && customPayload.length > 50) {
            customPayload = customPayload.substring(0, 50)
        }
        notificationParts.add("customPayload: $customPayload")
        notificationParts.add("status: " + item.status)
        holder.contentView.text = notificationParts.joinToString(" | ")
        holder.deleteButton.setOnClickListener {
            onDeleteItem?.invoke(item, position)
        }

        val category = item.contentCategory?.toLowerCase(Locale.getDefault())
        if(category == "image") {
            holder.imageView.visibility = VISIBLE
            Picasso.with(holder.imageView.context)
                .load(Uri.parse(item.contentUrl.toString()))
                .into(holder.imageView)
        } else {
            holder.imageView.visibility = GONE
        }

    }

    fun setOnDeleteItemListener(onDeleteItemListener: (notificationDto: NotificationDto, index: Int) -> Unit) {
        onDeleteItem = onDeleteItemListener
    }

    fun setItems(items: List<NotificationDto>) {
        values = items.toMutableList()
        notifyDataSetChanged()
    }

    fun getItems(): List<NotificationDto> {
        return values
    }

    fun removeItem(position: Int) {
        values.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = values.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contentView: TextView = view.findViewById(R.id.content)
        val deleteButton: Button = view.findViewById(R.id.delete_button)
        val imageView: ImageView = view.findViewById(R.id.image)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

    fun update(id: Long?) {
        values.find { it.id == id }?.let {
            notifyItemChanged(values.indexOf(it))
        }
    }

}

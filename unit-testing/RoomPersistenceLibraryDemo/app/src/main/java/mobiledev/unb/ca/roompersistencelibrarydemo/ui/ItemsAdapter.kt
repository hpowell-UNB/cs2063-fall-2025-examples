package mobiledev.unb.ca.roompersistencelibrarydemo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mobiledev.unb.ca.roompersistencelibrarydemo.R
import mobiledev.unb.ca.roompersistencelibrarydemo.entities.Item

class ItemsAdapter(private val dbItems: List<Item>,
                   private val listener: (Item) -> Unit) :
    RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ItemsViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ItemsViewHolder,
        position: Int
    ) {
        val item = dbItems[position]

        holder.nameTextView.text = item.name
        holder.numberTextView.text = item.num.toString()

        holder.deleteButton.setOnClickListener { listener(item) }
    }

    override fun getItemCount(): Int {
        return dbItems.size
    }

    // Inner ViewHolder Class
    class ItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.itemText)
        val numberTextView: TextView = itemView.findViewById(R.id.numberText)
        var deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
    }
}
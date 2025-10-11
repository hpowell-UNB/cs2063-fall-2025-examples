package mobiledev.unb.ca.httpurlconnectiondemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mobiledev.unb.ca.httpurlconnectiondemo.models.CountryItem

class MyAdapter(private val countryList: List<CountryItem>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        // Set the content of the current item
        val countryItem = countryList[position]

        holder.countryNameTextView.text = countryItem.title
        holder.capitalNameTextView.text = countryItem.capital
        holder.continentNameTextView.text = countryItem.continent
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    // Inner ViewHolder Class
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countryNameTextView: TextView = itemView.findViewById(R.id.countryName)
        val capitalNameTextView: TextView = itemView.findViewById(R.id.capitalCity)
        val continentNameTextView: TextView = itemView.findViewById(R.id.continentName)
    }
}
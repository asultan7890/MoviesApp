package com.example.moviesapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// custom adapter that helps the favorites fragment with the recycler view
class ListAdapter(val favlist: MutableList<List<String>>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(x: ViewGroup, y: Int): ViewHolder {
        val z = LayoutInflater.from(x.context).inflate(R.layout.adapter_layout, x, false)
        return ViewHolder(z)
    }
    override fun getItemCount(): Int {
        return favlist.size
    }
    override fun onBindViewHolder(x: ViewHolder, y: Int) {

        // show title for each favorite and open details when clicked on
        x.name?.text = favlist[y][1]
        x.name.setOnClickListener {
            val intent = Intent(x.name.context, DetailsActivity::class.java)
            intent.putExtra("ID", favlist[y][0])
            x.name.context.startActivity(intent)
        }
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.name)
    }
}
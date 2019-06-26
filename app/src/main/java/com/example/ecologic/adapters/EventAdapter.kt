package com.example.ecologic.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecologic.R
import com.example.ecologic.entities.*
import com.example.ecologic.interfaces.RecyclerViewClickListener
import kotlinx.android.synthetic.main.card_event.view.*

class EventAdapter(var items: List<Event>, var listener: View.OnClickListener) :
    RecyclerView.Adapter<EventAdapter.EventHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_event, parent, false)

        var holder = EventHolder(view)
        holder.onClick(listener)

        return holder
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        holder.bind(items.get(position))
    }

    class EventHolder(itemView: View) : RecyclerView.ViewHolder(itemView), RecyclerViewClickListener {

        override fun onClick(listener: View.OnClickListener) {
            itemView.setOnClickListener(listener)
        }

        fun bind(item: Event) = with(itemView) {
            Glide.with(this)
                .load(item.image)
                .into(iv_e_preview)
            tv_e_title.text = item.title
            tv_e_description.text = item.description
            tv_e_count.text = item.count.toString() + " participantes"
        }
    }

    fun setData(newList: List<Event>) {
        items = newList
        notifyDataSetChanged()
    }
}
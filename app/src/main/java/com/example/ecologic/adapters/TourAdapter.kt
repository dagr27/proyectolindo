package com.example.ecologic.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecologic.R
import com.example.ecologic.entities.*
import com.example.ecologic.interfaces.RecyclerViewClickListener
import kotlinx.android.synthetic.main.card_blog.view.*
import kotlinx.android.synthetic.main.card_tour.view.*

class TourAdapter(var items: List<Tour>, var listener: View.OnClickListener) :
    RecyclerView.Adapter<TourAdapter.TourHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_tour, parent, false)

        var holder = TourHolder(view)
        holder.onClick(listener)

        return holder
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TourHolder, position: Int) {
        holder.bind(items.get(position))
    }

    class TourHolder(itemView: View) : RecyclerView.ViewHolder(itemView), RecyclerViewClickListener {

        override fun onClick(listener: View.OnClickListener) {
            itemView.setOnClickListener(listener)
        }

        fun bind(item: Tour) = with(itemView) {
            Glide.with(this)
                .load(item.image)
                .into(iv_t_preview)
            tv_t_title.text = item.name
            tv_t_rate.text = item.rate.toString()
        }
    }

    fun setData(newList: List<Tour>) {
        items = newList
        notifyDataSetChanged()
    }
}
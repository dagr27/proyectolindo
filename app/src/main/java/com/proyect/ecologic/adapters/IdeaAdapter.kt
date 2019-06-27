package com.proyect.ecologic.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.proyect.ecologic.R
import com.proyect.ecologic.entities.*
import com.proyect.ecologic.interfaces.RecyclerViewClickListener
import kotlinx.android.synthetic.main.card_idea.view.*

class IdeaAdapter(var items: List<Idea>, var listener: View.OnClickListener) :
    RecyclerView.Adapter<IdeaAdapter.IdeaHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdeaHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_idea, parent, false)

        var holder = IdeaHolder(view)
        holder.onClick(listener)

        return holder
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: IdeaHolder, position: Int) {
        holder.bind(items.get(position))
    }

    class IdeaHolder(itemView: View) : RecyclerView.ViewHolder(itemView), RecyclerViewClickListener {

        override fun onClick(listener: View.OnClickListener) {
            itemView.setOnClickListener(listener)
        }

        fun bind(item: Idea) = with(itemView) {
            Glide.with(this)
                .load(item.image)
                .into(iv_i_preview)
            tv_i_title.text = item.title
            tv_i_username.text = item.username
            tv_i_date.text = item.date
        }
    }

    fun setData(newList: List<Idea>) {
        items = newList
        notifyDataSetChanged()
    }
}
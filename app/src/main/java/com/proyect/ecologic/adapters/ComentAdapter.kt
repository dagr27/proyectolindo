package com.proyect.ecologic.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proyect.ecologic.R
import com.proyect.ecologic.entities.*
import com.proyect.ecologic.interfaces.RecyclerViewClickListener
import kotlinx.android.synthetic.main.card_coment.view.*

class ComentAdapter(var items: List<Coment>, var listener: View.OnClickListener) :
    RecyclerView.Adapter<ComentAdapter.ComentHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComentHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_coment, parent, false)

        var holder = ComentHolder(view)
        holder.onClick(listener)

        return holder
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ComentHolder, position: Int) {
        holder.bind(items.get(position))
    }

    class ComentHolder(itemView: View) : RecyclerView.ViewHolder(itemView), RecyclerViewClickListener {

        override fun onClick(listener: View.OnClickListener) {
            itemView.setOnClickListener(listener)
        }

        fun bind(item: Coment) = with(itemView) {
            tv_cm_username.text = item.username
            tv_cm_text.text = item.coment
            tv_cm_date.text = item.date
        }
    }

    fun setData(newList: List<Coment>) {
        items = newList
        notifyDataSetChanged()
    }
}
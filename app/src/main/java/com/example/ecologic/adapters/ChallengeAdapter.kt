package com.example.ecologic.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecologic.R
import com.example.ecologic.entities.*
import com.example.ecologic.interfaces.RecyclerViewClickListener
import kotlinx.android.synthetic.main.card_challenge.view.*

class ChallengeAdapter(var items: List<Challenge>, var listener: View.OnClickListener) :
    RecyclerView.Adapter<ChallengeAdapter.ChallengeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_challenge, parent, false)

        var holder = ChallengeHolder(view)
        holder.onClick(listener)

        return holder
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ChallengeHolder, position: Int) {
        holder.bind(items.get(position))
    }

    class ChallengeHolder(itemView: View) : RecyclerView.ViewHolder(itemView), RecyclerViewClickListener {

        override fun onClick(listener: View.OnClickListener) {
            itemView.setOnClickListener(listener)
        }

        fun bind(item: Challenge) = with(itemView) {
            tv_c_title.text = item.title
            tv_c_sun.text = item.sun.toString() + "%"
            tv_c_love.text = item.love.toString() + "%"
            tv_c_water.text = item.water.toString() + "%"

            if (item.type == "Reciclaje") {
                Glide.with(this)
                    .load(R.drawable.reciclaje)
                    .into(iv_type)

            } else if (item.type == "Consumo") {
                Glide.with(this)
                    .load(R.drawable.consumo)
                    .into(iv_type)
            } else if (item.type == "Transporte") {
                Glide.with(this)
                    .load(R.drawable.transporte)
                    .into(iv_type)
            } else if (item.type == "Social") {
                Glide.with(this)
                    .load(R.drawable.social2)
                    .into(iv_type)
            } else {
                Glide.with(this)
                    .load(R.drawable.energia)
                    .into(iv_type)
            }
        }
    }

    fun setData(newList: List<Challenge>) {
        items = newList
        notifyDataSetChanged()
    }
}
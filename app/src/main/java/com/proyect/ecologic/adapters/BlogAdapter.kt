package com.proyect.ecologic.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.proyect.ecologic.R
import com.proyect.ecologic.entities.*
import com.proyect.ecologic.interfaces.RecyclerViewClickListener
import kotlinx.android.synthetic.main.card_blog.view.*

class BlogAdapter(var items: List<Blog>, var listener: View.OnClickListener) :
    RecyclerView.Adapter<BlogAdapter.BlogHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_blog, parent, false)

        var holder = BlogHolder(view)
        holder.onClick(listener)

        return holder
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BlogHolder, position: Int) {
        holder.bind(items.get(position))
    }

    class BlogHolder(itemView: View) : RecyclerView.ViewHolder(itemView), RecyclerViewClickListener {

        override fun onClick(listener: View.OnClickListener) {
            itemView.setOnClickListener(listener)
        }

        fun bind(item: Blog) = with(itemView) {
            Glide.with(this)
                .load(item.image)
                .into(iv_b_preview)
            tv_b_title.text = item.title
            tv_b_description.text = item.description
            tv_b_category.text = item.category
        }
    }

    fun setData(newList: List<Blog>) {
        items = newList
        notifyDataSetChanged()
    }
}
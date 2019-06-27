package com.proyect.ecologic.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proyect.ecologic.R
import com.proyect.ecologic.entities.Places

class PlacesAdapter(var list:ArrayList<Places>): RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: PlacesAdapter.ViewHolder, position: Int) {
        holder.bindItem(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.places_item, parent,false)
        return ViewHolder(v)
    }
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        fun bindItem(data: Places){
            /*Crear CardView Y asignar chunches*/
        }
    }
}
package com.example.ecologic.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ecologic.entities.User
import com.example.ecologic.Login
import com.example.ecologic.R
import com.example.ecologic.userData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class UserAdapter (var list:ArrayList<User>):RecyclerView.Adapter<UserAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        holder.bindItem(list[position])
    }

    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        fun bindItem(data:User){
            val db = FirebaseFirestore.getInstance()
            val user:TextView = itemView.findViewById(R.id.user_sup)
            val email:TextView = itemView.findViewById(R.id.user_email)
            val enable:Switch = itemView.findViewById(R.id.user_enable)
            val btnView:TextView= itemView.findViewById(R.id.user_view)
            if (data.status==0){
                enable.isChecked = false
            }else{
                enable.isChecked = true
            }
            user.text=data.username
            email.text=data.email

            enable?.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked) {
                    db.collection("users").document(data.username)
                        .update("status", 1)
                        .addOnSuccessListener {
                            Toast.makeText(itemView.context, "YES", Toast.LENGTH_LONG).show()
                        }

                }else{
                    db.collection("users").document(data.username)
                        .update("status", 0)
                        .addOnSuccessListener {
                            Toast.makeText(itemView.context, "YES", Toast.LENGTH_LONG).show()
                        }
                }

            }
            /*Lanzar actividad*/
            btnView.setOnClickListener{
                val intent = Intent(itemView.context, userData::class.java)
                intent.putExtra("username",data.username)
                startActivity(itemView.context,intent,null)

            }


        }
    }
    fun setData(newList:ArrayList<User>){
        list = newList
        notifyDataSetChanged()
    }

}
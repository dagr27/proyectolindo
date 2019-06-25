package com.example.ecologic

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecologic.Adapter.UserAdapter
import com.example.ecologic.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sup_home.*

class SupHome : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sup_home)
        val mAuth=FirebaseAuth.getInstance()
        var fullName:String
        val email = mAuth.currentUser!!.email.toString()
        val text = findViewById(R.id.admin_user) as TextView
        val logout:Button = findViewById(R.id.logout)
        val btnT:Button = findViewById(R.id.tourismAct)
        val data = FirebaseFirestore.getInstance().collection("users").whereEqualTo("email",email.toString())
            .get().addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    for (document in task.result!!){
                        fullName = document["name"].toString() +" "+ document["lastname"].toString()
                        text.setText(fullName)
                    }
                }
            }

        val Recycler:RecyclerView=findViewById(R.id.all_users)
        Recycler.layoutManager=LinearLayoutManager(this)

        /*Inflando el Recycler con Firebase*/
        val db = FirebaseFirestore.getInstance()

        db.collection("users").whereEqualTo("type",1).get()
            .addOnCompleteListener{task ->
                var users : ArrayList<User> = ArrayList()
                for(document in task.result!!){
                    var user1 = document.id
                    var email1 = document["email"]
                    var status  = document["status"].toString().toInt()
                    /*username:String, name: String, lastName: String, password: String, email: String, profilePicture: String, status: Int, lastDate: String, type: Int*/
                    users.add(User(user1,"","","","",status, "" ,document["type"].toString().toInt()))
                }
                val adapter = UserAdapter(users)
                Recycler.layoutManager=GridLayoutManager(this,2)
                Recycler.adapter = adapter
            }

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this,"Sesion Cerrada con exito", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, Login::class.java))
        }

        btnT.setOnClickListener {
            startActivity(Intent(this,Tourism::class.java))
        }



    }
}

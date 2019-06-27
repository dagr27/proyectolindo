package com.proyect.ecologic

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proyect.ecologic.Adapter.UserAdapter
import com.proyect.ecologic.activities.RetosSup
import com.proyect.ecologic.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SupHome : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_sup_home)
        val mAuth = FirebaseAuth.getInstance()
        var fullName: String
        val email = mAuth.currentUser!!.email.toString()
        val text = findViewById(R.id.admin_user) as TextView
        val logout: Button = findViewById(R.id.logout)
        val btnT: Button = findViewById(R.id.tourismAct)
        val retosbtn: Button = findViewById(R.id.retas)
        val Recycler: RecyclerView = findViewById(R.id.all_users)
        Recycler.layoutManager = LinearLayoutManager(this)

        /*Inflando el Recycler con Firebase*/
        val db = FirebaseFirestore.getInstance()

        db.collection("users").whereEqualTo("type", 0).get()
            .addOnCompleteListener { task ->
                var users: ArrayList<User> = ArrayList()
                for (document in task.result!!) {
                    var user1 = document["username"].toString()
                    var email1 = document["email"]
                    var status = document["status"].toString().toInt()
                    /*username:String, name: String, lastName: String, password: String, email: String, profilePicture: String, status: Int, lastDate: String, type: Int*/
                    users.add(
                        User(
                            user1,
                            "",
                            "",
                            "",
                            "",
                            status,
                            "",
                            document["type"].toString().toInt(),
                            email1.toString()
                        )
                    )
                }
                val adapter = UserAdapter(users)
                Recycler.layoutManager = GridLayoutManager(this, 2)
                Recycler.adapter = adapter
            }

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "Sesion Cerrada con exito", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        btnT.setOnClickListener {
            startActivity(Intent(this, Tourism::class.java))
        }

        retosbtn.setOnClickListener {
            startActivity(Intent(this, RetosSup::class.java))
        }
    }
}

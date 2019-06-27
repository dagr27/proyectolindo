package com.proyect.ecologic.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.proyect.ecologic.Login
import com.proyect.ecologic.R
import com.proyect.ecologic.Register
import com.proyect.ecologic.SupHome
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val reg = findViewById(R.id.reg) as Button
        val log = findViewById(R.id.log) as Button
        val infoBtn :TextView = findViewById(R.id.infoMore)
        /*Verificar Sesion*/
        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseFirestore.getInstance().collection("users")
                .whereEqualTo("email", FirebaseAuth.getInstance().currentUser!!.email)
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            if (document["type"].toString() == "1") {
                                startActivity(Intent(this, SupHome::class.java))
                                finish()
                            } else if(document["type"].toString() == "0") {
                                startActivity(Intent(this, UserActivity::class.java))
                                finish()
                            }
                        }
                    }
                }
        }

        reg.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }

        log.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
        infoBtn.setOnClickListener {
        }

    }
}
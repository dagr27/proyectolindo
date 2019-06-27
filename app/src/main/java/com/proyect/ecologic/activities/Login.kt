package com.proyect.ecologic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.proyect.ecologic.activities.MainActivity
import com.proyect.ecologic.activities.UserActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        val txtEmail: EditText = findViewById(R.id.txt_username)
        val txtPass: EditText = findViewById(R.id.txt_pass)
        val btnLog = findViewById<Button>(R.id.btnLog)
        val btnRegAct = findViewById<TextView>(R.id.btnRegAct)
        fun login() {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(txtEmail.text.toString(), txtPass.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        FirebaseFirestore.getInstance().collection("users")
                            .whereEqualTo("email", txtEmail.text.toString())
                            .get().addOnCompleteListener { tasks ->
                                if (tasks.isSuccessful) {
                                    for (document in tasks.result!!) {
                                        if (document["type"].toString() == "1" && document["status"].toString() == "1") {
                                            startActivity(Intent(this, SupHome::class.java))
                                            finish()
                                        } else if (document["type"].toString() == "0" && document["status"].toString() == "1") {
                                            startActivity(Intent(this, UserActivity::class.java))
                                            finish()
                                        } else {
                                            Toast.makeText(
                                                this,
                                                "Su cuenta no se encuentra activa, pero registrada, contactar al Administrador",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                }
                            }
                    } else {
                        Toast.makeText(this, "Inicio de sesion fallido", Toast.LENGTH_SHORT).show()

                    }
                }
        }
        btnLog.setOnClickListener {
            if (txtEmail.text.isEmpty()) {
                txtEmail.setError("Campo email vacio")
            } else if (txtPass.text.isEmpty()) {
                txtPass.setError("Campo contraseña vacio")
            } else if (txtPass.length() < 6) {
                txtPass.setError("Debes tener un minimo de 6 caracteres")
            } else if (txtEmail.text.isEmpty() and txtEmail.text.isEmpty()) {
                Toast.makeText(this, "Debes llenar los campos", Toast.LENGTH_LONG).show()
            } else {
                login()
            }
        }
        btnRegAct.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }


    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}


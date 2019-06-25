package com.example.ecologic

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Login:AppCompatActivity() {
    /*Cambia esta cosa a 1 si queres entrar en tu main y quema los datos no entiendo porque no me agarra esto si funcionaba en el otro proyecto */
    var ty:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        var type: TextView = findViewById(R.id.type_login)
        val txtEmail: EditText = findViewById(R.id.txt_username)
        val txtPass: EditText = findViewById(R.id.txt_pass)

        val btnLog = findViewById<Button>(R.id.btnLog)
        val btnRegAct = findViewById<TextView>(R.id.btnRegAct)
        fun login() {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(txtEmail.text.toString(), txtPass.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        FirebaseFirestore.getInstance().collection("users").whereEqualTo("email","erikrenderos@gmail.com")
                            .get().addOnCompleteListener{ tasks ->
                                if(tasks.isSuccessful){
                                    for (document in tasks.result!!){
                                        ty=document["type"].toString().toInt()
                                    }
                                }
                            }
                        Toast.makeText(this, "Inicio de Sesion Exitoso", Toast.LENGTH_SHORT).show()
                        if(ty == 0){
                            startActivity(Intent(this, SupHome::class.java))
                        }else if(ty == 1){
                            /*MAJEEE AQUI VA TU ACTIVITY*/
                            Toast.makeText(this,"ERIK AQUI VA TU ACTIVIDAD", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this, "Inicio de Sesion Fallido", Toast.LENGTH_SHORT).show()

                    }
                }
        }
            btnLog.setOnClickListener {
                if (txtEmail.text.isEmpty()) {
                    txtEmail.setError("Campo Email vacio")
                } else if (txtPass.text.isEmpty()) {
                    txtPass.setError("Campo Contraseña vacio")
                } else if (txtPass.length() < 6) {
                    txtPass.setError("Debes Tener un minimo de 6 caracteres")
                } else if (txtEmail.text.isEmpty() and txtEmail.text.isEmpty()) {
                    Toast.makeText(this, "Debes llenar los campos", Toast.LENGTH_LONG).show()
                } else {
                    login()
                }
            }
        btnLog.setOnClickListener {
            login()
        }
            btnRegAct.setOnClickListener {
                val intent = Intent(this, Register::class.java)
                startActivity(intent)
            }


        }
    }


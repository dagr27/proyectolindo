package com.example.ecologic

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.register.*
import java.util.*

class Register :AppCompatActivity(){
    lateinit var db : DocumentReference

    private fun validarVacios(name :EditText, lastname:EditText, email:EditText, pass:EditText, date:EditText, username:EditText): Boolean {
        if(name.getText().toString().isEmpty()){
            Toast.makeText(this, "Campo Nombre Vacio", Toast.LENGTH_LONG).show()
            return false
        }else if(lastname.getText().toString().isEmpty()){
            Toast.makeText(this, "Campo Apellido Vacio", Toast.LENGTH_LONG).show()
            return false
        }else if(email.getText().toString().isEmpty()){
            Toast.makeText(this, "Campo Email Vacio", Toast.LENGTH_LONG).show()
            return false
        }else if(pass.getText().toString().isEmpty()){
            Toast.makeText(this, "Campo Contraseña Vacio", Toast.LENGTH_LONG).show()
            return false
        }else if(date.getText().toString().isEmpty()){
            Toast.makeText(this, "Campo Fecha de Nacimiento Vacio", Toast.LENGTH_LONG).show()
            return false
        }else if(username.getText().toString().isEmpty()){
            Toast.makeText(this, "Campo Nombre de Usuario Vacio", Toast.LENGTH_LONG).show()
            return false
        }else if(pass.length() < 6){
            pass.setError("Debes tener minimo 6 Caracteres")
            return false
        }else{
            return true
        }

    }

    private fun insertar(name :EditText, lastname:EditText, email:EditText, pass:EditText, date:EditText, username:EditText, intent:Intent){
        try{
            val db = FirebaseFirestore.getInstance()
            val user = HashMap<String, Any>()
            user.put("name", name.getText().toString())
            user.put("lastname", lastname.getText().toString())
            user.put("email",email.getText().toString())
            user.put("password",pass.getText().toString())
            user.put("date",date.getText().toString())
            user.put("type", 1)

            db.collection("users").document(username.getText().toString()).set(user).addOnSuccessListener {
                Toast.makeText(this, "Usuario Registrado", Toast.LENGTH_LONG).show()
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                    .addOnCompleteListener(this){
                            task -> if(task.isSuccessful){
                        startActivity(intent)
                    }
                    }
            }.addOnFailureListener {
                    exception: java.lang.Exception -> Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
            }
        }catch (e:Exception){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        val btnReg = findViewById(R.id.reg_btn) as Button
        val name = findViewById(R.id.name) as EditText
        val lastname = findViewById(R.id.lastname) as EditText
        val email = findViewById(R.id.email) as EditText
        val pass = findViewById(R.id.pass) as EditText
        val username = findViewById(R.id.user) as EditText
        val date = findViewById(R.id.date) as EditText

        btnReg.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            val state = validarVacios(name, lastname,email,pass,date,username)
            /*Verificando e Insertando*/
            if(state == true) insertar(name, lastname,email, pass, date,username,intent)

        }


    }
}
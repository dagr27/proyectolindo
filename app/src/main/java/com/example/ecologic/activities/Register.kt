package com.example.ecologic

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecologic.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.register.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
            val sdf = SimpleDateFormat("dd/M/yyyy")
            val currentDate = sdf.format(Date())
            val db = FirebaseFirestore.getInstance()
            /*username:String, name: String, lastname: String, password: String, profilePicture: String, status: Int, lastDate: String, type: Int*/
            val user = HashMap<String, Any>()
            user.put("username", username.text.toString())
            user.put("name", name.text.toString())
            user.put("lastname", lastname.text.toString())
            user.put("password",pass.text.toString())
            user.put("email",email.text.toString())
            user.put("profilePicture","gs://ecologic-a7174.appspot.com/users/default.png")
            user.put("status",1)
            user.put("lastdate",currentDate)
            user.put("date",date.text.toString())
            user.put("type", 1)
            val challenges = HashMap<String, Any>()
            val plant = HashMap<String, Any>()
            plant.put("level", 0)
            plant.put("love", 0)
            plant.put("name", "Cactusin")
            plant.put("sun", 0)
            plant.put("water", 0)
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email.text.toString(),pass.text.toString())
                .addOnCompleteListener(this){task->
                        if (task.isSuccessful) {
                            db.collection("users").document(email.text.toString()).set(user).addOnSuccessListener {
                                db.collection("users/" + email.text.toString() + "/plants").add(plant).addOnSuccessListener {
                                }
                                db.collection("users/" + email.text.toString() + "/challenges").add(challenges).addOnSuccessListener {
                                }
                            }
                            Toast.makeText(this, "Usuario Registrado", Toast.LENGTH_LONG).show()
                            startActivity(intent)
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
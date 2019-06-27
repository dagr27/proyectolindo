package com.proyect.ecologic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.isVisible
import com.google.firebase.firestore.FirebaseFirestore

class userData : AppCompatActivity() {
    fun enableData(status:Boolean){
        val lastname:EditText= findViewById(R.id.lastname_su)
        val name:EditText= findViewById(R.id.name_su)
        val date:EditText= findViewById(R.id.date_su)
        val email:EditText= findViewById(R.id.email_su)
        name.setEnabled(status)
        lastname.setEnabled(status)
        email.setEnabled(status)
        date.setEnabled(status)
        val btn :Button= findViewById(R.id.btnAct_su)
        btn.isVisible = status
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_data)
        val username = intent.getStringExtra("username")

        val enable:Switch=findViewById(R.id.enable_edit)
        val user:EditText= findViewById(R.id.user_su)
        val lastname:EditText= findViewById(R.id.lastname_su)
        val name:EditText= findViewById(R.id.name_su)
        val date:EditText= findViewById(R.id.date_su)
        val email:EditText= findViewById(R.id.email_su)
        val status :TextView = findViewById(R.id.status_su)
        val btn :Button= findViewById(R.id.btnAct_su)
        val db = FirebaseFirestore.getInstance()
        user.setText(username.toString())

        /*Llenar con base de datos*/
        db.collection("users").document(username)
            .get().addOnCompleteListener { task->
                name.setText(task.result!!.get("name").toString())
                lastname.setText(task.result!!.get("lastname").toString())
                date.setText(task.result!!.get("date").toString())
                email.setText(task.result!!.get("email").toString())
                val n = task.result!!.get("status")
                if (n == 1) status.setText("Habilitado") else status.setText("Desabilitado")
            }

        /*Habilitar Edicion*/
        enable?.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked)enableData(true) else enableData(false)
        }
        btn.setOnClickListener {
            db.collection("users").document(username)
                .update("name", name.text.toString(),"lastname",lastname.text.toString(),"date",date.text.toString(),"email",email.text.toString())
                .addOnCompleteListener{
                    Toast.makeText(this, "Usuario Actualizado con Exito", Toast.LENGTH_LONG).show()
                }
        }



    }
}

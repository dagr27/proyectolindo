package com.proyect.ecologic.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide
import com.proyect.ecologic.R
import com.proyect.ecologic.entities.Coment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyect.ecologic.entities.User
import kotlinx.android.synthetic.main.activity_add_coment.*
import kotlinx.android.synthetic.main.activity_add_idea.toolbar
import kotlinx.android.synthetic.main.content_add_tour.*
import java.text.SimpleDateFormat
import java.util.*

class AddComent : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_add_coment)
        setSupportActionBar(toolbar)

        val mAuth= FirebaseAuth.getInstance()
        val user = mAuth.currentUser!!.email.toString()

        db.collection("users").document(user)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var document = task.result!!

                    val user = document.toObject(User::class.java)
                    Glide.with(this)
                        .load(user?.profilePicture)
                        .into(iv_coment_user)
                }
            }

        val extras = intent.extras
        tv_t_place.text = extras.getString("COMENT")

        btn_t_publish.setOnClickListener {
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
            val currentDate = sdf.format(Date())

            db.collection("touristPlaces").document(tv_t_place.text.toString())
                .get()
                .addOnCompleteListener { task ->
                    var document = task.result!!
                    val coment = Coment(user, et_t_coment.text.toString(), currentDate, document.id)

                    db.collection("coments")
                        .add(coment)
                    Toast.makeText(this, "Comentario agregado con exito.", Toast.LENGTH_SHORT).show()
                    finish()

                }
        }

        btn_t_cancel.setOnClickListener { finish() }
    }
}
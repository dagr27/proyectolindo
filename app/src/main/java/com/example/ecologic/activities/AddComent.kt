package com.example.ecologic.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecologic.R
import com.example.ecologic.entities.Coment
import com.example.ecologic.entities.Tour
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_coment.*
import kotlinx.android.synthetic.main.activity_add_idea.toolbar
import kotlinx.android.synthetic.main.content_add_tour.*
import java.text.SimpleDateFormat
import java.util.*

class AddComent : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    var user = "erikrenderos"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_coment)
        setSupportActionBar(toolbar)

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
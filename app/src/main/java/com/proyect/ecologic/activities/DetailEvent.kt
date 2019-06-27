package com.proyect.ecologic.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide
import com.proyect.ecologic.R
import com.proyect.ecologic.entities.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.activity_detail_event.*

class DetailEvent : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_detail_event)
        val reciever: Event = intent?.extras?.getParcelable("EVENT") ?: Event()
        init(reciever)

        val mAuth= FirebaseAuth.getInstance()
        val user = mAuth.currentUser!!.email.toString()

        db.collection("events").whereEqualTo("title", tv_de_title.text.toString())
            .get()
            .addOnCompleteListener { events ->
                for (document in events.result!!) {
                    db.collection("users").document(user).collection("events").document(document.id).get()
                        .addOnCompleteListener { task2 ->
                            val document2 = task2.result
                            if (document2!!.exists()) {
                                assist.text = "NO ASISTIR"
                            } else {
                                assist.text = "ASISTIR"
                            }
                        }
                }
            }

        assist.setOnClickListener {

            if (assist.text.toString() == "ASISTIR") {
                db.collection("events").whereEqualTo("title", tv_de_title.text.toString())
                    .get()
                    .addOnCompleteListener { events ->
                        for (document in events.result!!) {

                            db.collection("events").document(document.id)
                                .update("count",document["count"].toString().toInt() + 1)

                            val data = HashMap<String, Any>()
                            data["idEvent"] = document.id
                            db.collection("users").document(user).collection("events").document(document.id)
                                .set(data)

                            finish()
                        }
                    }

            } else {

                db.collection("events").whereEqualTo("title", tv_de_title.text.toString())
                    .get()
                    .addOnCompleteListener { events ->
                        for (document in events.result!!) {

                            db.collection("events").document(document.id)
                                .update("count",document["count"].toString().toInt() - 1)

                            val data = HashMap<String, Any>()
                            data["idEvent"] = document.id
                            db.collection("users").document(user).collection("events").document(document.id)
                                .delete()

                            finish()
                        }
                    }
            }

            startActivity(intent)

        }
    }

    fun init(event: Event) {
        Glide.with(this)
            .load(event.image)
            .into(iv_de)
        collapsingToolbarLayout.title = event.date
        tv_de_title.text = event.title
        tv_de_description.text = event.description
        tv_de_username.text = event.username
        tv_de_date.text = event.date
    }
}

package com.example.ecologic.activities

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide
import com.example.ecologic.R
import com.example.ecologic.entities.Event
import com.example.ecologic.fragments.HomeFragment
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.activity_detail_event.*

class DetailEvent : AppCompatActivity() {
    val user = "erikrenderos"
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_event)
        val reciever: Event = intent?.extras?.getParcelable("EVENT") ?: Event()
        init(reciever)

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
    }
}

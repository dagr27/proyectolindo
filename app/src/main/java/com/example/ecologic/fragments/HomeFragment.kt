package com.example.ecologic.fragments

import android.content.DialogInterface
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecologic.R
import com.example.ecologic.activities.DetailEvent
import com.example.ecologic.activities.SuccessActivity
import com.example.ecologic.adapters.*
import com.example.ecologic.entities.Event
import com.example.ecologic.entities.Plant
import com.example.ecologic.entities.UserXChallenge
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.card_event.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private lateinit var viewAdapter: EventAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var eventList: ArrayList<Event> = ArrayList<Event>()

    var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dailyChallenge()

        val mAuth= FirebaseAuth.getInstance()
        val user = mAuth.currentUser!!.email.toString()

        view.findViewById<View>(R.id.btn_challenges)
            .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_challenges))

        db.collection("users").document(user).collection("plants")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val plant = document.toObject(Plant::class.java)
                        tv_plant.text = plant.name
                        tv_level.text = plant.level.toString() + "%"
                        pb_level.progress = plant.level
                        pb_level2.progress = plant.level
                        pb_sun.progress = plant.sun
                        pb_water.progress = plant.water
                        pb_love.progress = plant.love

                        if (plant.sun < 15) {
                            pb_sun.progressDrawable.setColorFilter(
                                resources.getColor(R.color.colorRed),
                                PorterDuff.Mode.SRC_IN
                            )
                        } else if (plant.sun < 40) {
                            pb_sun.progressDrawable.setColorFilter(
                                resources.getColor(R.color.colorOrange),
                                PorterDuff.Mode.SRC_IN
                            )
                        } else if (plant.sun < 99) {
                            pb_sun.progressDrawable.setColorFilter(
                                resources.getColor(R.color.colorGreen),
                                PorterDuff.Mode.SRC_IN
                            )
                        } else if (plant.sun == 100) {
                            pb_sun.progressDrawable.setColorFilter(
                                resources.getColor(R.color.colorBlue),
                                PorterDuff.Mode.SRC_IN
                            )
                        }

                        if (plant.water < 15) {
                            pb_water.progressDrawable.setColorFilter(
                                resources.getColor(R.color.colorRed),
                                PorterDuff.Mode.SRC_IN
                            )
                        } else if (plant.water < 40) {
                            pb_water.progressDrawable.setColorFilter(
                                resources.getColor(R.color.colorOrange),
                                PorterDuff.Mode.SRC_IN
                            )
                        } else if (plant.water < 99) {
                            pb_water.progressDrawable.setColorFilter(
                                resources.getColor(R.color.colorGreen),
                                PorterDuff.Mode.SRC_IN
                            )
                        } else if (plant.water == 100) {
                            pb_water.progressDrawable.setColorFilter(
                                resources.getColor(R.color.colorBlue),
                                PorterDuff.Mode.SRC_IN
                            )
                        }

                        if (plant.love < 15) {
                            pb_love.progressDrawable.setColorFilter(
                                resources.getColor(R.color.colorRed),
                                PorterDuff.Mode.SRC_IN
                            )
                        } else if (plant.love < 40) {
                            pb_love.progressDrawable.setColorFilter(
                                resources.getColor(R.color.colorOrange),
                                PorterDuff.Mode.SRC_IN
                            )
                        } else if (plant.love < 99) {
                            pb_love.progressDrawable.setColorFilter(
                                resources.getColor(R.color.colorGreen),
                                PorterDuff.Mode.SRC_IN
                            )
                        } else if (plant.love == 100) {
                            pb_love.progressDrawable.setColorFilter(
                                resources.getColor(R.color.colorBlue),
                                PorterDuff.Mode.SRC_IN
                            )
                        }

                        if (plant.level < 30) {
                            iv_plant.setImageResource(R.drawable.plant1)
                        } else if (plant.level < 60) {
                            iv_plant.setImageResource(R.drawable.plant2)
                        } else if (plant.level < 99) {
                            iv_plant.setImageResource(R.drawable.plant3)
                        } else if (plant.level == 100) {
                            iv_plant.setImageResource(R.drawable.plant4)
                        }

                        if (plant.sun == 0 || plant.water == 0 || plant.love == 0) {
                            tv_status.text = "Muerta"
                            iv_plant.setImageResource(R.drawable.plant5)

                        } else if (plant.sun < 25 || plant.water < 25 || plant.love < 25) {
                            tv_status.text = "Muriendo"

                        } else if (plant.sun < 50 && plant.water < 50 && plant.love < 50) {
                            tv_status.text = "Triste"

                        } else if (plant.sun < 100 && plant.water < 100 && plant.love < 100) {
                            tv_status.text = "Estable"

                        } else if (plant.sun == 100 && plant.water == 100 && plant.love == 100) {
                            tv_status.text = "Feliz"
                        }
                    }
                }
            }.addOnFailureListener {}

        db.collection("events").get()
            .addOnCompleteListener { task ->
                for (document in task.result!!) {
                    db.collection("users").document(user).collection("events").document(document.id)
                        .get().addOnCompleteListener { task2 ->
                            val document2 = task2.result
                            if (document2!!.exists()) {
                                val event = document.toObject(Event::class.java)
                                eventList.add(event)
                            } else {
                            }
                            viewAdapter.setData(eventList)
                        }
                }
            }.addOnFailureListener {}

        val click = View.OnClickListener { v ->
            v.card_event.setOnClickListener {
                db.collection("events")
                    .whereEqualTo("title", v.tv_e_title.text.toString())
                    .get()
                    .addOnCompleteListener { task ->
                        for (document in task.result!!) {
                            val event = document.toObject(Event::class.java)

                            val eventBundle = Bundle()
                            eventBundle.putParcelable("EVENT", event)
                            startActivity(Intent(v.context, DetailEvent::class.java).putExtras(eventBundle))
                        }
                    }.addOnFailureListener {}
            }
        }

        viewAdapter = EventAdapter(eventList, click)
        viewManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        initRecycler()
    }

    private fun initRecycler() {
        with(rv_events) {
            adapter = viewAdapter
            layoutManager = viewManager
        }
    }

    private fun points(user: String, sun: Int, water: Int, love: Int) {
        db.collection("users").document(user).collection("plants")
            .get()
            .addOnCompleteListener { task ->
                for (document in task.result!!) {
                    val plant = document.toObject(Plant::class.java)

                    var sun = sun + plant.sun
                    if (sun > 100) {
                        sun = 100
                    }

                    var water = water + plant.water
                    if (water > 100) {
                        water = 100
                    }

                    var love = love + plant.love
                    if (love > 100) {
                        love = 100
                    }

                    var level = plant.level + 2

                    db.collection("users").document(user).collection("plants").document(document.id)
                        .update("sun", sun, "water", water, "love", love, "level", level)
                }
            }
    }

    private fun dailyChallenge(){
        val sdf = SimpleDateFormat("dd")
        val day = sdf.format(Date())

        val sdf2 = SimpleDateFormat("MMMM")
        val mes = sdf2.format(Date())

        tv_h_day.text = day.toString()
        tv_h_mes.text = mes.toString()
    }
}
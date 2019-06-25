package com.example.ecologic.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecologic.R
import com.example.ecologic.adapters.*
import com.example.ecologic.entities.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.card_challenge.view.*
import kotlinx.android.synthetic.main.fragment_challenge.*
import android.view.View.OnClickListener
import com.example.ecologic.activities.SuccessActivity
import com.google.firebase.auth.FirebaseAuth


class ChallengeFragment : Fragment() {

    private lateinit var viewAdapter: ChallengeAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var challengeList: ArrayList<Challenge> = ArrayList()

    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_challenge, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mAuth= FirebaseAuth.getInstance()
        val user = mAuth.currentUser!!.email.toString()

        db.collection("challenges").get()
            .addOnCompleteListener { task ->
                for (document in task.result!!) {
                    db.collection("users").document(user).collection("challenges").document(document.id)
                        .get().addOnCompleteListener { task2 ->
                            val document2 = task2.result
                            if (document2!!.exists()) {
                            } else {
                                val challenge = document.toObject(Challenge::class.java)
                                challengeList.add(challenge)
                            }
                            viewAdapter.setData(challengeList)
                        }
                }
            }

        btn_recycler.setOnClickListener {
            var challengeList: ArrayList<Challenge> = ArrayList()
            db.collection("challenges").whereEqualTo("type", "Reciclaje").get()
                .addOnCompleteListener { task ->
                    for (document in task.result!!) {
                        db.collection("users").document(user).collection("challenges").document(document.id)
                            .get().addOnCompleteListener { task2 ->
                                val document2 = task2.result
                                if (document2!!.exists()) {
                                } else {
                                    val challenge = document.toObject(Challenge::class.java)
                                    challengeList.add(challenge)
                                }
                                viewAdapter.setData(challengeList)
                            }
                    }
                }
        }

        btn_electric.setOnClickListener {
            var challengeList: ArrayList<Challenge> = ArrayList()
            db.collection("challenges").whereEqualTo("type", "Energia").get()
                .addOnCompleteListener { task ->
                    for (document in task.result!!) {
                        db.collection("users").document(user).collection("challenges").document(document.id)
                            .get().addOnCompleteListener { task2 ->
                                val document2 = task2.result
                                if (document2!!.exists()) {
                                } else {
                                    val challenge = document.toObject(Challenge::class.java)
                                    challengeList.add(challenge)
                                }
                                viewAdapter.setData(challengeList)
                            }
                    }
                }
        }

        btn_transport.setOnClickListener {
            var challengeList: ArrayList<Challenge> = ArrayList()
            db.collection("challenges").whereEqualTo("type", "Transporte").get()
                .addOnCompleteListener { task ->
                    for (document in task.result!!) {
                        db.collection("users").document(user).collection("challenges").document(document.id)
                            .get().addOnCompleteListener { task2 ->
                                val document2 = task2.result
                                if (document2!!.exists()) {
                                } else {
                                    val challenge = document.toObject(Challenge::class.java)
                                    challengeList.add(challenge)
                                }
                                viewAdapter.setData(challengeList)
                            }
                    }
                }
        }

        btn_food.setOnClickListener {
            var challengeList: ArrayList<Challenge> = ArrayList()
            db.collection("challenges").whereEqualTo("type", "Consumo").get()
                .addOnCompleteListener { task ->
                    for (document in task.result!!) {
                        db.collection("users").document(user).collection("challenges").document(document.id)
                            .get().addOnCompleteListener { task2 ->
                                val document2 = task2.result
                                if (document2!!.exists()) {
                                } else {
                                    val challenge = document.toObject(Challenge::class.java)
                                    challengeList.add(challenge)
                                }
                                viewAdapter.setData(challengeList)
                            }
                    }
                }
        }

        btn_social.setOnClickListener {
            var challengeList: ArrayList<Challenge> = ArrayList()
            db.collection("challenges").whereEqualTo("type", "Social").get()
                .addOnCompleteListener { task ->
                    for (document in task.result!!) {
                        db.collection("users").document(user).collection("challenges").document(document.id)
                            .get().addOnCompleteListener { task2 ->
                                val document2 = task2.result
                                if (document2!!.exists()) {
                                } else {
                                    val challenge = document.toObject(Challenge::class.java)
                                    challengeList.add(challenge)
                                }
                                viewAdapter.setData(challengeList)
                            }
                    }
                }
        }

        val click = OnClickListener { v ->
            val builder = AlertDialog.Builder(v.context)
            builder.setTitle(v.tv_c_title.text)
            builder.setMessage("¿Seguro que ya completaste este reto?")
            builder.setPositiveButton("Si") { dialogInterface: DialogInterface, i: Int ->

                db.collection("challenges").whereEqualTo("title", v.tv_c_title.text.toString())
                    .get()
                    .addOnCompleteListener { challenges ->
                        for (document in challenges.result!!) {
                            val userXchallenge = UserXChallenge(document.id)
                            db.collection("users").document(user).collection("challenges").document(document.id)
                                .set(userXchallenge)
                        }
                    }

                val sun = v.tv_c_sun.text.toString().substring(0,v.tv_c_sun.text.toString().length-1).toInt()
                val water = v.tv_c_water.text.toString().substring(0,v.tv_c_water.text.toString().length-1).toInt()
                val love =  v.tv_c_love.text.toString().substring(0,v.tv_c_love.text.toString().length-1).toInt()

                points(user, sun,water,love)

                startActivity(Intent(this.context, SuccessActivity::class.java))

                val ft = fragmentManager?.beginTransaction()
                ft?.replace(R.id.fragment, HomeFragment())?.addToBackStack(null)?.commit()
            }

            builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int -> }
            builder.show()
        }

        viewAdapter = ChallengeAdapter(challengeList, click)
        viewManager = LinearLayoutManager(context)
        initRecycler()
    }

    private fun initRecycler() {
        with(rv_challenges) {
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
                    if(sun > 100){sun = 100}

                    var water = water + plant.water
                    if(water > 100){water = 100}

                    var love = love + plant.love
                    if(love > 100){love = 100}

                    var level = plant.level + 2

                    db.collection("users").document(user).collection("plants").document(document.id)
                        .update("sun", sun, "water", water, "love", love, "level", level)
                }
            }
    }
}
package com.example.ecologic.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecologic.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.user_home.*
import kotlinx.android.synthetic.main.app_bar.*
import com.bumptech.glide.Glide
import com.example.ecologic.adapters.ChallengeAdapter
import com.example.ecologic.adapters.ComentAdapter
import com.example.ecologic.entities.*
import com.example.ecologic.fragments.EventFragment
import com.example.ecologic.fragments.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.card_challenge.view.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*

class UserActivity : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()
    val user = mAuth.currentUser!!.email.toString()
    val db = FirebaseFirestore.getInstance()

    private lateinit var viewAdapter: ChallengeAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var searchList: ArrayList<Challenge> = ArrayList<Challenge>()

    private var filter = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_home)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(filter == ""){
                    busqueda.visibility = View.GONE
                }
                busqueda.visibility = View.VISIBLE
                filter = s.toString()
                db.collection("challenges").whereEqualTo("title", filter)
                    .get()
                    .addOnCompleteListener { challenges ->
                        for (document in challenges.result!!) {
                            val challenge = document.toObject(Challenge::class.java)
                            searchList.add(challenge)
                        }
                        viewAdapter.setData(searchList)

                    }

                val click = View.OnClickListener { v ->
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

                        val sun = v.tv_c_sun.text.toString().substring(0, v.tv_c_sun.text.toString().length - 1).toInt()
                        val water =
                            v.tv_c_water.text.toString().substring(0, v.tv_c_water.text.toString().length - 1).toInt()
                        val love =
                            v.tv_c_love.text.toString().substring(0, v.tv_c_love.text.toString().length - 1).toInt()

                        points(user, sun, water, love)
                        finish()
                        startActivity(Intent(this@UserActivity, SuccessActivity::class.java))
                    }

                    builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int -> }
                    builder.show()
                }

                viewAdapter = ChallengeAdapter(searchList, click)
                viewManager = LinearLayoutManager(this@UserActivity)
                initRecycler()

            }

        })

        val navController = findNavController(R.id.fragment)
        nav_bottom_view.setupWithNavController(navController)
        nav_view.setupWithNavController(navController)

        var profile_image = nav_view.getHeaderView(0).findViewById(R.id.profile_image) as ImageView
        var tv_username = nav_view.getHeaderView(0).findViewById(R.id.tv_username) as TextView
        var tv_email = nav_view.getHeaderView(0).findViewById(R.id.tv_email) as TextView

        db.collection("users").document(user)
            .get()
            .addOnCompleteListener { user ->
                if (user.isSuccessful) {
                    val document = user.result
                    val userData = document?.toObject(User::class.java)

                    tv_username.text = userData?.name + " " + userData?.lastname
                    tv_email.text = document?.id

                    Glide.with(this)
                        .load(userData?.profilePicture)
                        .into(profile_image)

                    plant(userData?.lastdate)
                }
            }
    }

    private fun initRecycler() {
        with(busqueda) {
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

                    var level = plant.level + 1

                    db.collection("users").document(user).collection("plants").document(document.id)
                        .update("sun", sun, "water", water, "love", love, "level", level)
                }
            }
    }

    fun plant(lastDate: String?) {
        val sdf2 = SimpleDateFormat("dd")
        val currentDate2 = sdf2.format(Date())
        val date = lastDate?.substring(0, lastDate.length - 7)!!.toInt()
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())

        db.collection("users").document(user).collection("plants")
            .get()
            .addOnCompleteListener { task ->
                for (document in task.result!!) {
                    val plant = document.toObject(Plant::class.java)

                    if (date != currentDate2.toInt()) {

                        db.collection("challenges")
                            .get()
                            .addOnCompleteListener { task ->
                                for (document in task.result!!) {

                                    db.collection("users").document(user).collection("dailychallenge")
                                        .get()
                                        .addOnCompleteListener { task2 ->
                                            for (document2 in task2.result!!) {
                                                if (document.id == document2["idChallenge"].toString()) {

                                                } else {
                                                    db.collection("users").document(user).collection("dailychallenge")
                                                        .document(document2.id)
                                                        .update("idChallenge", document.id, "status", 0)
                                                }

                                            }
                                        }

                                }
                            }
                    }

                    if (date == currentDate2.toInt()) {

                    } else if (date + 1 == currentDate2.toInt()) {

                        var sun = plant.love - 10
                        if (sun < 0) {
                            sun = 0
                        }
                        var water = plant.love - 10
                        if (water < 0) {
                            water = 0
                        }
                        var love = plant.love - 10
                        if (love < 0) {
                            love = 0
                        }
                        var level = plant.level + 1
                        if (level > 100) {
                            level = 100
                        }

                        db.collection("users").document(user).collection("plants").document(document.id)
                            .update("sun", sun, "water", water, "love", love, "level", level)

                        db.collection("users").document(user)
                            .update("lastdate", currentDate)

                    } else {

                        var sun = plant.love - 15
                        if (sun < 0) {
                            sun = 0
                        }
                        var water = plant.love - 15
                        if (water < 0) {
                            water = 0
                        }
                        var love = plant.love - 15
                        if (love < 0) {
                            love = 0
                        }


                        db.collection("users").document(user).collection("plants").document(document.id)
                            .update("sun", sun, "water", water, "love", love)

                        db.collection("users").document(user)
                            .update("lastdate", currentDate)

                    }
                }
            }
    }
}
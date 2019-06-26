package com.example.ecologic.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ecologic.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.user_home.*
import kotlinx.android.synthetic.main.app_bar.*
import com.bumptech.glide.Glide
import com.example.ecologic.entities.Plant
import com.example.ecologic.entities.User
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class UserActivity : AppCompatActivity() {
    val mAuth= FirebaseAuth.getInstance()
    val user = mAuth.currentUser!!.email.toString()
    val db = FirebaseFirestore.getInstance()

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

                    if (date == currentDate2.toInt()){

                    }else if (date + 1 == currentDate2.toInt()) {

                        var sun = plant.love - 10
                        var water = plant.love - 10
                        var love = plant.love - 10
                        var level = plant.level + 1

                        db.collection("users").document(user).collection("plants").document(document.id)
                            .update("sun", sun, "water", water, "love", love, "level", level)

                        db.collection("users").document(user)
                            .update("lastDate", currentDate)

                    } else {

                        var sun = plant.love - 15
                        var water = plant.love - 15
                        var love = plant.love - 15


                        db.collection("users").document(user).collection("plants").document(document.id)
                            .update("sun", sun, "water", water, "love", love)

                        db.collection("users").document(user)
                            .update("lastDate", currentDate)

                    }
                }
            }
    }
}
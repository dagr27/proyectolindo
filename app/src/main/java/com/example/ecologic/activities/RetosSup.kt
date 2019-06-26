package com.example.ecologic.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecologic.R
import com.example.ecologic.adapters.ChallengeAdapter
import com.example.ecologic.entities.Challenge
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_retos_sup.*
import kotlinx.android.synthetic.main.fragment_challenge.*

class RetosSup : AppCompatActivity() {
    private lateinit var viewAdapter: ChallengeAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var challengeList: ArrayList<Challenge> = ArrayList()
    private fun initRecycler() {
        with(rv_challengesAdmin) {
            adapter = viewAdapter
            layoutManager = viewManager
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retos_sup)
        var btnAdd : TextView = findViewById(R.id.R_add)
        var title : EditText = findViewById(R.id.R_title)
        var cat : EditText = findViewById(R.id.R_cat)
        var sun : EditText = findViewById(R.id.R_sun)
        var water : EditText = findViewById(R.id.R_water)
        var love : EditText = findViewById(R.id.R_love)

        var enableT: Switch = findViewById(R.id.enable_add_reto)
        var addBox: LinearLayout = findViewById(R.id.addingR)

        btnAdd.setOnClickListener{
            FirebaseFirestore.getInstance().collection("challenges")
                    /*title: String, love: Int, sun: Int, water: Int, type: String*/
                .add(Challenge(title.text.toString(), love.text.toString().toInt(),sun.text.toString().toInt(),water.text.toString().toInt(),cat.text.toString()))
                .addOnSuccessListener {
                    Toast.makeText(this,"Reto Añadido Correctamente", Toast.LENGTH_LONG).show()
                }
        }
        enableT?.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) addBox.visibility = View.VISIBLE
            else addBox.visibility = View.GONE
        }
        val click = View.OnClickListener { v ->

        }

        FirebaseFirestore.getInstance().collection("challenges").get()
            .addOnCompleteListener { task ->
                for (document in task.result!!) {
                    val challenge = document.toObject(Challenge::class.java)
                    challengeList.add(challenge)
                }
                viewAdapter.setData(challengeList)
            }

        viewAdapter = ChallengeAdapter(challengeList, click)
        viewManager = LinearLayoutManager(this)
        initRecycler()
    }
}

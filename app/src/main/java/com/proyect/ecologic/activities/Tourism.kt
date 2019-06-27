package com.proyect.ecologic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proyect.ecologic.adapters.TourAdapter
import com.proyect.ecologic.entities.Places
import com.proyect.ecologic.entities.Tour
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_tourism.*

class Tourism : AppCompatActivity() {
    lateinit var viewManager: RecyclerView.LayoutManager
    var tourList: ArrayList<Tour> = ArrayList<Tour>()
    lateinit var viewAdapter: TourAdapter
    fun initRecycler() {
        with(rv_tourAdmin) {
            adapter = viewAdapter
            layoutManager = viewManager
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tourism)
        var btnAdd : TextView = findViewById(R.id.T_add)
        var title :EditText= findViewById(R.id.T_title)
        var rate :EditText= findViewById(R.id.T_rate)
        var desc :EditText= findViewById(R.id.T_desc)
        var enableT:Switch = findViewById(R.id.enable_add)
        var addBox:LinearLayout = findViewById(R.id.addingT)
        btnAdd.setOnClickListener{
            FirebaseFirestore.getInstance().collection("touristPlaces")
                .add(Places(title.text.toString(),desc.text.toString(),rate.text.toString().toDouble()))
                .addOnSuccessListener {
                    Toast.makeText(this,"Lugar Añadido Correctamente", Toast.LENGTH_LONG).show()
                }
        }
        enableT?.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) addBox.visibility = View.VISIBLE
            else addBox.visibility = View.GONE
        }

        FirebaseFirestore.getInstance().collection("touristPlaces")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val tour = document.toObject(Tour::class.java)
                        tourList.add(tour)
                    }
                    viewAdapter.setData(tourList)
                }
            }

        val click = View.OnClickListener { v ->
            /*Aqui vamos a añadir la otra actividad de esta cosa*/
        }

        viewAdapter = TourAdapter(tourList,click)
        viewManager = LinearLayoutManager(this)
        initRecycler()

    }
}

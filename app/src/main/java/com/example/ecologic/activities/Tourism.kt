package com.example.ecologic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.ecologic.entities.Places
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

class Tourism : AppCompatActivity() {

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
    }
}

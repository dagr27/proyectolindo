package com.proyect.ecologic.activities

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.firestore.FirebaseFirestore
import com.proyect.ecologic.R

class retosData : AppCompatActivity() {
    fun enableData(status:Boolean){
        var title : EditText = findViewById(R.id.R_title_edit)
        var cat : EditText = findViewById(R.id.R_cat_edit)
        var sun : EditText = findViewById(R.id.R_sun_edit)
        var water : EditText = findViewById(R.id.R_water_edit)
        var love : EditText = findViewById(R.id.R_love_edit)
        title.setEnabled(status)
        cat.setEnabled(status)
        sun.setEnabled(status)
        water.setEnabled(status)
        love.setEnabled(status)

        val btn : Button = findViewById(R.id.btnAct_re_su)
        btn.isVisible = status
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_retos_data)
        val btn : Button = findViewById(R.id.btnAct_re_su)
        var titleTxt : EditText = findViewById(R.id.R_title_edit)
        var cat : EditText = findViewById(R.id.R_cat_edit)
        var sun : EditText = findViewById(R.id.R_sun_edit)
        var water : EditText = findViewById(R.id.R_water_edit)
        var love : EditText = findViewById(R.id.R_love_edit)

        val enable:Switch = findViewById(R.id.enable_edit_re)
        val title = intent.getStringExtra("title")
        FirebaseFirestore.getInstance().collection("challenges").whereEqualTo("title", title)
            .get().addOnCompleteListener { task->

                for(document in task.result!!){
                    titleTxt.setText(document["title"].toString())
                    cat.setText(document["type"].toString())
                    sun.setText(document["sun"].toString())
                    water.setText(document["water"].toString())
                    love.setText(document["love"].toString())
                    btn.setOnClickListener {
                        FirebaseFirestore.getInstance().collection("challenges").document(document.id)
                            .update("title", titleTxt.text.toString(),"type",cat.text.toString()
                                ,"sun",sun.text.toString().toInt()
                                ,"water",water.text.toString().toInt()
                                ,"love",love.text.toString().toInt()
                            )
                            .addOnCompleteListener{
                                Toast.makeText(this, "Reto Actualizado con Exito", Toast.LENGTH_LONG).show()
                            }
                    }

                }
            }

        enable?.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked)enableData(true) else enableData(false)
        }



    }
}

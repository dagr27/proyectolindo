package com.proyect.ecologic.activities

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide
import com.proyect.ecologic.R
import kotlinx.android.synthetic.main.activity_success.*

class SuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_success)

        Glide.with(this).load(R.drawable.success).into(success)

        btn_back.setOnClickListener {
            finish()
            startActivity(Intent(this, UserActivity::class.java))
        }
    }
}

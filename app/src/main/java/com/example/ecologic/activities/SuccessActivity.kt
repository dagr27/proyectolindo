package com.example.ecologic.activities

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide
import com.example.ecologic.R
import kotlinx.android.synthetic.main.activity_success.*

class SuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        Glide.with(this).load(R.drawable.success).into(success)

        btn_back.setOnClickListener {
            finish()
        }
    }
}

package com.example.ecologic.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide
import com.example.ecologic.R
import com.example.ecologic.entities.Idea
import kotlinx.android.synthetic.main.activity_detail_blog.*

import kotlinx.android.synthetic.main.activity_detail_idea.*

class DetailIdea : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_idea)
        val reciever: Idea = intent?.extras?.getParcelable("IDEA") ?: Idea()
        init(reciever)
    }

    fun init(idea: Idea) {
        Glide.with(this)
            .load(idea.image)
            .into(iv_di)
        tv_di_title.text = idea.title
        tv_di_description.text = idea.description
        tv_di_username.text = idea.username
    }
}

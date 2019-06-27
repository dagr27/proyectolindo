package com.proyect.ecologic.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide
import com.proyect.ecologic.R
import com.proyect.ecologic.entities.Blog
import kotlinx.android.synthetic.main.activity_detail_blog.*

class DetailBlog : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_blog)
        val reciever: Blog = intent?.extras?.getParcelable("BLOG") ?: Blog()
        init(reciever)
    }

    fun init(blog: Blog){
        Glide.with(this)
            .load(blog.image)
            .into(iv_db)
        tv_db_title.text = blog.title
        tv_db_description.text = blog.description
    }
}
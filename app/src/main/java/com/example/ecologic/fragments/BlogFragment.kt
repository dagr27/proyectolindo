package com.example.ecologic.fragments

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecologic.R
import com.example.ecologic.activities.DetailBlog
import com.example.ecologic.adapters.*
import com.example.ecologic.entities.Blog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.card_blog.*
import kotlinx.android.synthetic.main.card_blog.view.*
import kotlinx.android.synthetic.main.fragment_blog.*
import kotlinx.android.synthetic.main.fragment_home.*

class BlogFragment : Fragment() {

    private lateinit var viewAdapter: BlogAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var blogList: ArrayList<Blog> = ArrayList<Blog>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blog, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mAuth= FirebaseAuth.getInstance()
        val user = mAuth.currentUser!!.email.toString()

        var db = FirebaseFirestore.getInstance()

        db.collection("blog")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val blog = document.toObject(Blog::class.java)
                        blogList.add(blog)
                    }
                    viewAdapter.setData(blogList)
                }
            }

        val click = View.OnClickListener { v ->
            db.collection("blog")
                .whereEqualTo("title", v.tv_b_title.text.toString())
                .get()
                .addOnCompleteListener { task ->
                    for (document in task.result!!) {
                        val blog = document.toObject(Blog::class.java)

                        val blogBundle = Bundle()
                        blogBundle.putParcelable("BLOG", blog)
                        startActivity(Intent(v.context, DetailBlog::class.java).putExtras(blogBundle))
                    }
                }
        }

        viewAdapter = BlogAdapter(blogList, click)
        viewManager = LinearLayoutManager(context)
        initRecycler()
    }

    private fun initRecycler() {
        with(rv_blog) {
            adapter = viewAdapter
            layoutManager = viewManager
        }
    }
}
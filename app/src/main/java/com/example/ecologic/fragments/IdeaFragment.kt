package com.example.ecologic.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecologic.R
import com.example.ecologic.adapters.*
import com.example.ecologic.entities.*
import com.google.firebase.firestore.FirebaseFirestore
import android.view.View.OnClickListener
import com.example.ecologic.activities.AddIdea
import com.example.ecologic.activities.DetailIdea
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.card_idea.*
import kotlinx.android.synthetic.main.card_idea.view.*
import kotlinx.android.synthetic.main.fragment_idea.*

class IdeaFragment : Fragment() {

    private lateinit var viewAdapter: IdeaAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var ideaList: ArrayList<Idea> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_idea, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mAuth= FirebaseAuth.getInstance()
        val user = mAuth.currentUser!!.email.toString()

        val db = FirebaseFirestore.getInstance()

        fab_add_idea.setOnClickListener {
            startActivity(Intent(this.context, AddIdea::class.java))
            val ft = fragmentManager?.beginTransaction()
            ft?.replace(R.id.fragment, IdeaFragment())?.addToBackStack(null)?.commit()
        }

        db.collection("ideas").get()
            .addOnCompleteListener { task ->
                for (document in task.result!!) {
                    val idea = document.toObject(Idea::class.java)
                    ideaList.add(idea)
                }
                viewAdapter.setData(ideaList)
            }

        val click = OnClickListener { v ->

            db.collection("ideas")
                .whereEqualTo("title", v.tv_i_title.text.toString())
                .get()
                .addOnCompleteListener { task ->
                    for (document in task.result!!) {
                        val idea = document.toObject(Idea::class.java)

                        val ideaBundle = Bundle()
                        ideaBundle.putParcelable("IDEA", idea)
                        startActivity(Intent(v.context, DetailIdea::class.java).putExtras(ideaBundle))
                    }
                }
        }

        btn_mine.setOnClickListener {
            btn_mine.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            btn_all.setBackgroundColor(resources.getColor(R.color.colorPrimary))

            ideaList = ArrayList()
            db.collection("ideas")
                .whereEqualTo("username", user)
                .get()
                .addOnCompleteListener { task ->
                    for (document in task.result!!) {
                        val idea = document.toObject(Idea::class.java)
                        ideaList.add(idea)
                    }
                    viewAdapter.setData(ideaList)
                }
        }

        btn_all.setOnClickListener {
            btn_all.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            btn_mine.setBackgroundColor(resources.getColor(R.color.colorPrimary))

            ideaList = ArrayList()
            db.collection("ideas")
                .get()
                .addOnCompleteListener { task ->
                    for (document in task.result!!) {
                        val idea = document.toObject(Idea::class.java)
                        ideaList.add(idea)
                    }
                    viewAdapter.setData(ideaList)
                }
        }

        viewAdapter = IdeaAdapter(ideaList, click)
        viewManager = LinearLayoutManager(context)
        initRecycler()
    }

    private fun initRecycler() {
        with(rv_ideas) {
            adapter = viewAdapter
            layoutManager = viewManager
        }
    }
}
package com.proyect.ecologic.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proyect.ecologic.R
import com.proyect.ecologic.activities.AddEvent
import com.proyect.ecologic.activities.DetailEvent
import com.proyect.ecologic.adapters.*
import com.proyect.ecologic.entities.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.card_event.view.*
import kotlinx.android.synthetic.main.fragment_event.*

class EventFragment : Fragment() {

    private lateinit var viewAdapter: EventAdapter
    private lateinit var viewAdapter2: EventAdapter

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewManager2: RecyclerView.LayoutManager
    private var bestList: ArrayList<Event> = ArrayList<Event>()
    private var youList: ArrayList<Event> = ArrayList<Event>()

    var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser!!.email.toString()

        db.collection("events")
            .orderBy("count", Query.Direction.DESCENDING).limit(5)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        db.collection("users").document(user).collection("events").document(document.id)
                            .get().addOnCompleteListener { task2 ->
                                val document2 = task2.result
                                if (document2!!.exists()) {
                                } else {
                                    val event = document.toObject(Event::class.java)
                                    bestList.add(event)
                                }
                                viewAdapter.setData(bestList)
                            }
                    }
                }
            }

        db.collection("events")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        db.collection("users").document(user).collection("events").document(document.id)
                            .get().addOnCompleteListener { task2 ->
                                val document2 = task2.result
                                if (document2!!.exists()) {
                                } else {
                                    val event = document.toObject(Event::class.java)
                                    youList.add(event)
                                }
                                viewAdapter2.setData(youList)
                            }
                    }
                }
            }

        val click = View.OnClickListener { v ->
            db.collection("events")
                .whereEqualTo("title", v.tv_e_title.text.toString())
                .get()
                .addOnCompleteListener { task ->
                    for (document in task.result!!) {
                        val event = document.toObject(Event::class.java)

                        val eventBundle = Bundle()
                        eventBundle.putParcelable("EVENT", event)
                        startActivity(Intent(v.context, DetailEvent::class.java).putExtras(eventBundle))
                    }
                }
        }

        fab_add_event.setOnClickListener {
            startActivity(Intent(this.context, AddEvent::class.java))
            val ft = fragmentManager?.beginTransaction()
            ft?.replace(R.id.fragment, EventFragment())?.addToBackStack(null)?.commit()
        }

        viewAdapter = EventAdapter(bestList, click)
        viewAdapter2 = EventAdapter(youList, click)

        viewManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewManager2 = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        initRecycler()
    }

    private fun initRecycler() {
        with(rv_e_best) {
            adapter = viewAdapter
            layoutManager = viewManager
        }

        with(rv_e_you) {
            adapter = viewAdapter2
            layoutManager = viewManager2
        }
    }
}
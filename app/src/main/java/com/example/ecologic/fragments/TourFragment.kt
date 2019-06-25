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
import com.example.ecologic.activities.DetailTour
import com.example.ecologic.adapters.*
import com.example.ecologic.entities.Tour
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.card_tour.view.*
import kotlinx.android.synthetic.main.fragment_tour.*

class TourFragment : Fragment() {

    private lateinit var viewAdapter: TourAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var tourList: ArrayList<Tour> = ArrayList<Tour>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tour, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mAuth= FirebaseAuth.getInstance()
        val user = mAuth.currentUser!!.email.toString()
        var db = FirebaseFirestore.getInstance()

        db.collection("touristPlaces")
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
            db.collection("touristPlaces")
                .whereEqualTo("name", v.tv_t_title.text.toString())
                .get()
                .addOnCompleteListener { task ->
                    for (document in task.result!!) {
                        val tour = document.toObject(Tour::class.java)

                        val tourBundle = Bundle()
                        tourBundle.putParcelable("TOUR", tour)
                        startActivity(Intent(v.context, DetailTour::class.java).putExtras(tourBundle))
                    }
                }
        }

        viewAdapter = TourAdapter(tourList, click)
        viewManager = LinearLayoutManager(context)
        initRecycler()
    }

    private fun initRecycler() {
        with(rv_tour) {
            adapter = viewAdapter
            layoutManager = viewManager
        }
    }
}
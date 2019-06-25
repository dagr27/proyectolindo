package com.example.ecologic.activities

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.Layout.JUSTIFICATION_MODE_INTER_WORD
import android.view.View
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecologic.R
import com.example.ecologic.adapters.ComentAdapter
import com.example.ecologic.entities.Blog
import com.example.ecologic.entities.Coment
import com.example.ecologic.entities.Tour
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_detail_blog.*
import kotlinx.android.synthetic.main.activity_detail_blog.collapsingToolbarLayout
import kotlinx.android.synthetic.main.activity_detail_tour.*
import kotlinx.android.synthetic.main.card_blog.view.*
import kotlinx.android.synthetic.main.card_tour.*

class DetailTour : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()
    private lateinit var viewAdapter: ComentAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var comentList: ArrayList<Coment> = ArrayList<Coment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tour)
        val reciever: Tour = intent?.extras?.getParcelable("TOUR") ?: Tour()
        init(reciever)
    }

    fun init(tour: Tour) {
        Glide.with(this)
            .load(tour.image)
            .into(iv_dt)
        collapsingTour.title = tour.name

        db.collection("touristPlaces").whereEqualTo("name", tour.name)
            .get()
            .addOnCompleteListener { events ->
                for (document in events.result!!) {
                    db.collection("coments")
                        .whereEqualTo("idTour", document.id).orderBy("date", Query.Direction.ASCENDING)
                        .get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                for (document in task.result!!) {
                                    val coment = document.toObject(Coment::class.java)
                                    comentList.add(coment)
                                }
                                fab_add_coment.setOnClickListener {
                                    val comentBundle = Bundle()
                                    comentBundle.putString("COMENT", document.id)
                                    finish()
                                    startActivity(intent)
                                    startActivity(Intent(this, AddComent::class.java).putExtras(comentBundle))
                                }
                                viewAdapter.setData(comentList)
                            }
                        }.addOnFailureListener {}
                }
            }
        val click = View.OnClickListener { v ->
        }

        viewAdapter = ComentAdapter(comentList, click)
        viewManager = LinearLayoutManager(this)
        initRecycler()
    }

    private fun initRecycler() {
        with(rv_coments) {
            adapter = viewAdapter
            layoutManager = viewManager
        }
    }
}

private fun Bundle.putParcelable(s: String, data: HashMap<String, Any>) {

}

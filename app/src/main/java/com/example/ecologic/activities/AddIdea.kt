package com.example.ecologic.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity;
import com.example.ecologic.R
import com.example.ecologic.entities.Idea
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

import kotlinx.android.synthetic.main.activity_add_idea.*
import kotlinx.android.synthetic.main.activity_add_idea.toolbar
import kotlinx.android.synthetic.main.content_add_idea.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddIdea : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    var firebaseStore = FirebaseStorage.getInstance()
    var storageReference = FirebaseStorage.getInstance().reference
    var db = FirebaseFirestore.getInstance()

    var user = "erikrenderos"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_idea)
        setSupportActionBar(toolbar)

        btn_i_selectImage.setOnClickListener { launchGallery() }

        btn_i_publish.setOnClickListener {
            uploadImage()
            Toast.makeText(this, "Post publicado con exito.", Toast.LENGTH_SHORT).show()
            finish()
        }

        btn_i_cancel.setOnClickListener { finish() }
    }

    // IMAGE

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val image = preview_i_image
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                image.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun addUploadRecordToDb(uri: String) {

        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())

        val idea = Idea(et_i_title.text.toString(), et_i_post.text.toString(), uri, currentDate, user)

        db.collection("ideas")
            .add(idea)
    }

    private fun uploadImage() {
        if (filePath != null) {
            val ref = storageReference.child("ideas/" + UUID.randomUUID().toString())
            val uploadTask = ref.putFile(filePath!!)

            val urlTask = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    addUploadRecordToDb(downloadUri.toString())
                }
            }.addOnFailureListener {

            }
        } else {

        }
    }
}

private fun Unit.setImageBitmap(bitmap: Bitmap?) {}

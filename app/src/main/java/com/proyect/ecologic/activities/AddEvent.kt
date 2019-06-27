package com.proyect.ecologic.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity;
import com.proyect.ecologic.R
import com.proyect.ecologic.entities.Event
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_event.*

import kotlinx.android.synthetic.main.activity_add_idea.toolbar
import kotlinx.android.synthetic.main.content_add_event.*
import java.io.IOException
import java.util.*

class AddEvent : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    var firebaseStore = FirebaseStorage.getInstance()
    var storageReference = FirebaseStorage.getInstance().reference
    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        setSupportActionBar(toolbar)

        val mAuth= FirebaseAuth.getInstance()
        val user = mAuth.currentUser!!.email.toString()

        btn_e_selectImage.setOnClickListener { launchGallery() }

        btn_e_publish.setOnClickListener {
            uploadImage(user)
            Toast.makeText(this, "Evento creado con exito.", Toast.LENGTH_SHORT).show()
            finish()
        }

        btn_e_cancel.setOnClickListener { finish() }
    }

    // IMAGE

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val image = preview_e_image
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

    private fun addUploadRecordToDb(user: String, uri: String) {
        val event =
            Event(et_e_title.text.toString(), et_e_description.text.toString(), uri, et_e_date.text.toString(), 0,user)

        db.collection("events")
            .add(event)
    }

    private fun uploadImage(user: String) {
        if (filePath != null) {
            val ref = storageReference.child("events/" + UUID.randomUUID().toString())
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
                    addUploadRecordToDb(user, downloadUri.toString())
                }
            }.addOnFailureListener {

            }
        } else {

        }
    }
}

private fun Unit.setImageBitmap(bitmap: Bitmap?) {}

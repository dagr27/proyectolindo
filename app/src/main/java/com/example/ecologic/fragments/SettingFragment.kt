package com.example.ecologic.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import com.example.ecologic.R
import com.google.firebase.firestore.FirebaseFirestore
import com.example.ecologic.entities.*
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_setting.*
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap
import com.bumptech.glide.Glide
import com.example.ecologic.activities.UserActivity
import com.google.firebase.auth.FirebaseAuth

class SettingFragment : Fragment() {
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null

    var firebaseStore = FirebaseStorage.getInstance()
    var storageReference = FirebaseStorage.getInstance().reference
    var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mAuth= FirebaseAuth.getInstance()
        val user = mAuth.currentUser!!.email.toString()

        db.collection("users").document(user)
            .get()
            .addOnCompleteListener { users ->
                if (users.isSuccessful) {
                    var document = users.result!!

                        val user = document.toObject(User::class.java)
                        et_email.setText(document.id)
                        et_name.setText(user?.name)
                        et_lastName.setText(user?.lastname)

                        Glide.with(this)
                            .load(user?.profilePicture)
                            .into(preview_image)

                        if (user?.status == 1) {
                            switchMode.isChecked = true
                        }

                }
            }

        db.collection("users").document(user)
            .collection("plants")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val plant = document.toObject(Plant::class.java)
                        et_plant.setText(plant.name)
                    }
                }
            }

        btn_changePhoto.setOnClickListener { launchGallery() }

        btn_saveChanges.setOnClickListener {

            var mode = 0
            if (switchMode.isChecked) {
                mode = 1
            }

            db.collection("users").document(user)
                .update(
                    "name",
                    et_name.text.toString(),
                    "lastname",
                    et_lastName.text.toString(),
                    "status", mode
                )

            db.collection("users").document(user).collection("plants")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            db.collection("users").document(user).collection("plants")
                                .document(document.id).update("name", et_plant.text.toString())
                        }
                    }
                }

            uploadImage(user)

            activity?.finish()
            startActivity(Intent(context, UserActivity::class.java))

            Toast.makeText(context, "Perfil actualizado correctamente.", Toast.LENGTH_SHORT).show()

        }
    }

    // PROFILE PICTURE

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Selecciona tu foto de perfil"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val image = preview_image
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, filePath)
                image.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun addUploadRecordToDb(user: String, uri: String) {
        val data = HashMap<String, Any>()
        data["profilePicture"] = uri

        db.collection("users").document(user)
            .update(data)
    }

    private fun uploadImage(user: String) {
        if (filePath != null) {
            val ref = storageReference.child("users/" + UUID.randomUUID().toString())
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
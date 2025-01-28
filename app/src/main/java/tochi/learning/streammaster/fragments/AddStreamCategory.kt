package tochi.learning.streammaster.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import tochi.learning.streammaster.R
import tochi.learning.streammaster.activities.MainActivityAdmin
import java.util.UUID


class AddStreamCategory : Fragment() {

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    private lateinit var selectedImageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_stream_category_form, container, false)


        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val categoryName = view.findViewById<EditText>(R.id.streamCategoryName)
        val categoryDescription = view.findViewById<EditText>(R.id.streamCategoryDescription)
        val submitButton = view.findViewById<Button>(R.id.btn_create_stream_category)
        val backButton : Button = view.findViewById(R.id.back_button)

        imageView.setOnClickListener{
            openGallery()
        }

        val mainActivity = requireActivity() as MainActivityAdmin

        backButton.setOnClickListener {
            val StreamCategories = StreamCategories()
            mainActivity.switchFragment(StreamCategories)
            val AddStreamCategoryManager = AddStreamCategoryManager()
            AddStreamCategoryManager.show(parentFragmentManager, "MovieManagerDialog")
        }


        submitButton.setOnClickListener {
            val name = categoryName.text.toString()
            val description = categoryDescription.text.toString()
            uploadDatatoFirebase(name,description)

        }


        return view
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data!!

            val imageView = view?.findViewById<ImageView>(R.id.imageView)
            imageView?.setImageURI(selectedImageUri)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun uploadDatatoFirebase(name: String, description: String) {

        val db = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference

        val imageFileName = UUID.randomUUID().toString()
        val imageRef = storageRef.child("images/$imageFileName.jpg")

        val uploadTask = imageRef.putFile(selectedImageUri)

        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                imageRef.downloadUrl.addOnSuccessListener { imageUrl ->
                    val data = hashMapOf(
                        "Category Name" to name,
                        "Category Description" to description,
                        "ImageUrl" to imageUrl.toString()
                    )

                    db.collection("forms")
                        .add(data)
                        .addOnSuccessListener {
                            showToast("Data uploaded successfully!")

                            val mainActivity = requireActivity() as MainActivityAdmin

                            val StreamCategories = StreamCategories()
                            mainActivity.switchFragment(StreamCategories)


                        }

                }.addOnFailureListener { e ->

                    showToast("Error uploading data to Firestore")

                }

            }else {
                val exception = task.exception
                Log.e("ImageUpload", "Error uploading image to Storage", exception)
                showToast("Error uploading image to Storage")
            }

        }
    }
}


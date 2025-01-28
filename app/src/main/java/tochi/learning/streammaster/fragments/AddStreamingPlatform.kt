package tochi.learning.streammaster.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import tochi.learning.streammaster.R
import java.util.*

class AddStreamingPlatform : Fragment() {

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    private lateinit var selectedImageUri: Uri
    private lateinit var imageView: ImageView
    private lateinit var streamingPlatformName: EditText
    private lateinit var streamingPlatformDescription: EditText
    private lateinit var streamCategorySpinner: Spinner
    private lateinit var addButton: Button

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_streaming_platform, container, false)

        imageView = view.findViewById(R.id.imageView)
        streamingPlatformName = view.findViewById(R.id.streamingPlatformName)
        streamingPlatformDescription = view.findViewById(R.id.streamingPlatformDescription)
        streamCategorySpinner = view.findViewById(R.id.streamCategorySpinner)
        addButton = view.findViewById(R.id.btn_add_streaming_platform)

        imageView.setOnClickListener {
            openGallery()
        }

        // Fetch and populate spinner items from the database
        fetchCategoriesAndPopulateSpinner()

        addButton.setOnClickListener {
            val name = streamingPlatformName.text.toString()
            val category = streamCategorySpinner.selectedItem.toString()
            val description = streamingPlatformDescription.text.toString()

            uploadStreamingPlatformToFirestore(name, category, description)
        }

        return view
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun fetchCategoriesAndPopulateSpinner() {
        // Replace "forms" with the appropriate collection name for your categories in Firestore
        db.collection("forms")
            .get()
            .addOnSuccessListener { result ->
                val categories = mutableListOf<String>()
                for (document in result) {
                    val categoryName = document.getString("Category Name")
                    categoryName?.let { categories.add(it) }
                }

                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                streamCategorySpinner.adapter = adapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error fetching categories", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadStreamingPlatformToFirestore(name: String, category: String, description: String) {
        val storageRef = storage.reference
        val imageFileName = UUID.randomUUID().toString()
        val imageRef = storageRef.child("images/$imageFileName.jpg")

        val uploadTask = imageRef.putFile(selectedImageUri)

        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                imageRef.downloadUrl.addOnSuccessListener { imageUrl ->
                    val data = hashMapOf(
                        "Streaming Platform Name" to name,
                        "Streaming Platform Category" to category,
                        "Streaming Platform Description" to description,
                        "ImageUrl" to imageUrl.toString()
                    )

                    // Replace "streamingPlatforms" with the appropriate collection name in your Firestore database
                    db.collection("streamingPlatforms")
                        .add(data)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Streaming platform added successfully!", Toast.LENGTH_SHORT).show()
                            // Clear input fields after successful upload
                            streamingPlatformName.text.clear()
                            streamingPlatformDescription.text.clear()
                            imageView.setImageResource(R.drawable.user_image) // Reset the image
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "Error uploading streaming platform", Toast.LENGTH_SHORT).show()
                        }

                }.addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error uploading image to Storage", Toast.LENGTH_SHORT).show()
                }
            } else {
                val exception = task.exception
                Toast.makeText(requireContext(), "Error uploading image to Storage", Toast.LENGTH_SHORT).show()
                exception?.let { Log.e("ImageUpload", "Error uploading image to Storage", it) }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data!!
            imageView.setImageURI(selectedImageUri)
        }
    }
}

package tochi.learning.streammaster.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import tochi.learning.streammaster.R
import tochi.learning.streammaster.activities.MainActivityAdmin
import java.util.*

class UpdateStreamCategory : Fragment() {

    private lateinit var selectedImageUri: Uri
    private lateinit var selectedCategoryDocumentId: String

    companion object {
        private const val PICK_IMAGE_REQUEST = 123
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_update_stream_category_form, container, false)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val backButton: ImageButton = view.findViewById(R.id.back_button)
        val updateCategoryButton: AppCompatButton = view.findViewById(R.id.btn_update_stream_category)
        val categoryName: EditText = view.findViewById(R.id.streamCategoryName)
        val categoryDescription: EditText = view.findViewById(R.id.streamCategoryDescription)
        val categorySpinner: Spinner = view.findViewById(R.id.streamCategorySpinner)

        val categoryNamesList = mutableListOf<String>()
        val categoryIdsList = mutableListOf<String>()

        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryNamesList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = spinnerAdapter

        val db = FirebaseFirestore.getInstance()

        // Populate spinner with category names and store corresponding document IDs
        db.collection("categories")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val categoryName = document.getString("Category Name")
                    if (categoryName != null) {
                        categoryNamesList.add(categoryName)
                        categoryIdsList.add(document.id)
                    }
                }
                spinnerAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting category names", exception)
            }

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategoryDocumentId = categoryIdsList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected
            }
        }

        imageView.setOnClickListener {
            openImagePicker()
        }

        updateCategoryButton.setOnClickListener {
            val name = categoryName.text.toString()
            val description = categoryDescription.text.toString()

            if (name.isNotBlank() && description.isNotBlank() && ::selectedImageUri.isInitialized) {
                updateCategoryDetails(name, description)
            } else {
                showToast("Please fill in all fields and select an image.")
            }
        }

        val mainActivity = requireActivity() as MainActivityAdmin

        backButton.setOnClickListener {
            val streamCategories = StreamCategories() // Replace with your appropriate fragment instance
            mainActivity.switchFragment(streamCategories)
        }

        return view
    }

    private fun updateCategoryDetails(name: String, description: String) {
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
                        // Add other fields as needed
                    )

                    db.collection("categories").document(selectedCategoryDocumentId)
                        .update(data as Map<String, Any>)
                        .addOnSuccessListener {
                            showToast("Category details updated successfully!")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error updating category details", e)
                            showToast("Error updating category details")
                        }

                    val mainActivity = requireActivity() as MainActivityAdmin
                    val streamCategories = StreamCategories() // Replace with your appropriate fragment instance
                    mainActivity.switchFragment(streamCategories)
                }
            } else {
                val exception = task.exception
                Log.e("ImageUpload", "Error uploading image to Storage", exception)
                showToast("Error uploading image to Storage")
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data!!
            val imageView: ImageView = requireView().findViewById(R.id.imageView)
            imageView.setImageURI(selectedImageUri)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

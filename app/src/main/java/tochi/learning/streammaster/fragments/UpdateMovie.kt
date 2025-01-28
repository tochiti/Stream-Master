package tochi.learning.streammaster.fragments

import Movies
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

class UpdateMovie : Fragment() {

    private lateinit var selectedImageUri: Uri
    private lateinit var selectedMovieId: String // To store the selected movie's document ID

    companion object {
        private const val PICK_IMAGE_REQUEST = 123
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_update_movie, container, false)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val backButton: ImageButton = view.findViewById(R.id.back_button)
        val updateMovieButton: AppCompatButton = view.findViewById(R.id.updateMovieButton)
        val movieName: EditText = view.findViewById(R.id.movieName)
        val movieDescription: EditText = view.findViewById(R.id.movieDescription)
        val movieSpinner: Spinner = view.findViewById(R.id.movieSpinner)

        // Set up spinner with movie names
        val movieNamesList = mutableListOf<String>() // Store movie names
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, movieNamesList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        movieSpinner.adapter = spinnerAdapter

        // Populate spinner with movie names from Firestore
        val db = FirebaseFirestore.getInstance()
        db.collection("movies")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val movieName = document.getString("Movie Name")
                    if (movieName != null) {
                        movieNamesList.add(movieName)
                    }
                }
                spinnerAdapter.notifyDataSetChanged() // Notify spinner adapter about data change
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting movie names", exception)
            }

        movieSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Get the selected movie's document ID based on the selected movie name
                val selectedMovieName = movieNamesList[position]
                fetchDocumentId(selectedMovieName) // Fetch and store the document ID
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected
            }
        }

        imageView.setOnClickListener {
            openImagePicker()
        }

        updateMovieButton.setOnClickListener {
            val name = movieName.text.toString()
            val description = movieDescription.text.toString()

            if (name.isNotBlank() && description.isNotBlank() && ::selectedImageUri.isInitialized) {
                updateMovieDetails(name, description)
            } else {
                showToast("Please fill in all fields and select an image.")
            }
        }

        val mainActivity = requireActivity() as MainActivityAdmin

        backButton.setOnClickListener {
            val Movies = Movies() // Replace with your appropriate fragment instance
            mainActivity.switchFragment(Movies)
        }

        return view
    }

    private fun fetchDocumentId(movieName: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("movies")
            .whereEqualTo("Movie Name", movieName)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    selectedMovieId = documents.documents[0].id // Store the document ID
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching document ID", exception)
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

    private fun updateMovieDetails(name: String, description: String) {
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
                        "Movie Name" to name,
                        "Movie Description" to description,
                        "ImageUrl" to imageUrl.toString()
                    )
                    // Update the movie data in Firestore using selectedMovieId
                    db.collection("movies").document(selectedMovieId).update(data as Map<String, Any>)
                        .addOnSuccessListener {
                            showToast("Movie details updated successfully!")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error updating movie details", e)
                            showToast("Error updating movie details")
                        }
                    val mainActivity = requireActivity() as MainActivityAdmin
                    val Movies = Movies() // Replace with your appropriate fragment instance
                    mainActivity.switchFragment(Movies)
                }
            } else {
                val exception = task.exception
                Log.e("ImageUpload", "Error uploading image to Storage", exception)
                showToast("Error uploading image to Storage")
            }
        }
    }
}

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
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import tochi.learning.streammaster.R
import tochi.learning.streammaster.activities.MainActivityAdmin
import java.util.*

class addMovie : Fragment() {

    private lateinit var selectedImageUri: Uri

    companion object {
        private const val PICK_IMAGE_REQUEST = 123
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_add_movie, container, false)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val backButton: ImageButton = view.findViewById(R.id.back_button)
        val addMovieButton: AppCompatButton = view.findViewById(R.id.addMovieButton)
        val movieName: EditText = view.findViewById(R.id.movieName)
        val movieDescription: EditText = view.findViewById(R.id.movieDescription)

        imageView.setOnClickListener {
            openImagePicker()
        }

        addMovieButton.setOnClickListener {
            val name = movieName.text.toString()
            val description = movieDescription.text.toString()

            if (name.isNotBlank() && description.isNotBlank() && ::selectedImageUri.isInitialized) {
                uploadImageAndMovie(name, description)
            } else {
                showToast("Please fill in all fields and select an image.")
            }
        }

        val mainActivity = requireActivity() as MainActivityAdmin

        backButton.setOnClickListener {
            val Movies = Movies()
            mainActivity.switchFragment(Movies)
            val movieManagerDialog = MovieManager()
            movieManagerDialog.show(parentFragmentManager, "MovieManagerDialog")
        }

        return view
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

    private fun uploadImageAndMovie(name: String, description: String) {
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
                    db.collection("movies")
                        .add(data)
                        .addOnSuccessListener {
                            showToast("Data uploaded successfully!")

                            val mainActivity = requireActivity() as MainActivityAdmin
                            val Movies = Movies()
                            mainActivity.switchFragment(Movies)
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error uploading data to Firestore", e)
                            showToast("Error uploading data to Firestore")
                        }
                }
            } else {
                val exception = task.exception
                Log.e("ImageUpload", "Error uploading image to Storage", exception)
                showToast("Error uploading image to Storage")
            }
        }
    }
}

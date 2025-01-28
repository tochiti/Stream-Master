package tochi.learning.streammaster.fragments

import Movies
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import tochi.learning.streammaster.R
import tochi.learning.streammaster.activities.MainActivityAdmin
import tochi.learning.streammaster.adapters.ImageMovieAdapter

class DeleteMovie : Fragment() {

    private lateinit var imageGridView: GridView
    private lateinit var deleteFab: FloatingActionButton
    private lateinit var selectedDocumentIds: MutableList<String>
    private val movieMap = mutableMapOf<String, String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_delete_movie, container, false)
        val backButton: ImageButton = view.findViewById(R.id.back_button)
        imageGridView = view.findViewById(R.id.imageGridView)
        deleteFab = view.findViewById(R.id.deleteFab)
        selectedDocumentIds = mutableListOf()

        val db = FirebaseFirestore.getInstance()

        db.collection("movies")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val imageUrl = document.getString("ImageUrl")
                    if (imageUrl != null) {
                        val documentId = document.id
                        movieMap[imageUrl] = documentId
                    }
                }
                setupGridView(movieMap.keys.toList())
            }
            .addOnFailureListener { exception ->
                showToast("Error fetching movie data")
            }

        backButton.setOnClickListener {
            val Movies = Movies()
            (requireActivity() as MainActivityAdmin).switchFragment(Movies)
        }

        deleteFab.setOnClickListener {
            if (selectedDocumentIds.isNotEmpty()) {
                deleteSelectedMovies(db, selectedDocumentIds)
            } else {
                showToast("No movies selected to delete")
            }
        }

        return view
    }

    private fun setupGridView(imageUrls: List<String>) {
        val adapter = ImageMovieAdapter(requireContext(), imageUrls, selectedDocumentIds)
        imageGridView.adapter = adapter

        imageGridView.setOnItemClickListener { _, _, position, _ ->
            val selectedUrl = imageUrls[position]
            val selectedDocumentId = movieMap[selectedUrl]
            if (selectedDocumentId != null) {
                if (selectedDocumentIds.contains(selectedDocumentId)) {
                    selectedDocumentIds.remove(selectedDocumentId)
                } else {
                    selectedDocumentIds.add(selectedDocumentId)
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun deleteSelectedMovies(db: FirebaseFirestore, documentIds: List<String>) {
        for (documentId in documentIds) {
            db.collection("movies").document(documentId)
                .delete()
                .addOnSuccessListener {
                    showToast("Movie deleted successfully!")
                }
                .addOnFailureListener { exception ->
                    showToast("Error deleting movie")
                }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

package tochi.learning.streammaster.fragments

import tochi.learning.streammaster.adapters.ImageAdapter
import Movies
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import tochi.learning.streammaster.Items.ImageData
import tochi.learning.streammaster.R
import tochi.learning.streammaster.activities.MainActivityAdmin


class AdminDashboard : Fragment() {

    private lateinit var rootView: View
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_admin_dashboard, container, false)
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        setupStreamCategoriesRecyclerView()
        setupStreamProvidersRecyclerView()
        setupMoviesRecyclerView()

        return rootView
    }

    private fun setupStreamCategoriesRecyclerView() {
        val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerViewStreamCategories)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Query Firestore collection "forms" and populate the images into the adapter
        firestore.collection("forms")
            .limit(6)
            .get()
            .addOnSuccessListener { documents ->

                val imageDataList = documents.mapNotNull { document ->
                    val imageUrl = document.getString("ImageUrl")
                    val name = document.getString("Category Name")
                    if (imageUrl != null && name != null) {
                        ImageData(imageUrl, name)
                    } else {
                        null
                    }
                }

                recyclerView.adapter = ImageAdapter(imageDataList)
            }

        val mainActivity = requireActivity() as MainActivityAdmin
        val viewAllTextView: TextView = rootView.findViewById(R.id.textViewViewAllCategories)
        viewAllTextView.setOnClickListener {
            val StreamCategories = StreamCategories()
            mainActivity.switchFragment(StreamCategories)
        }
    }

    private fun setupStreamProvidersRecyclerView() {
        val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerViewStreamProviders)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Query Firestore collection "forms" and populate the images into the adapter
        firestore.collection("streamingPlatforms")
            .limit(6)
            .get()
            .addOnSuccessListener { documents ->


                val imageDataList = documents.mapNotNull { document ->
                    val imageUrl = document.getString("ImageUrl")
                    val name = document.getString("Streaming Platform Name")
                    if (imageUrl != null && name != null) {
                        ImageData(imageUrl, name)
                    } else {
                        null
                    }
                }

                recyclerView.adapter = ImageAdapter(imageDataList)
            }

        val mainActivity = requireActivity() as MainActivityAdmin
        val viewAllTextView: TextView = rootView.findViewById(R.id.textViewViewAllProviders)
        viewAllTextView.setOnClickListener {
            val StreamingPlatforms = StreamingPlatforms()
            mainActivity.switchFragment(StreamingPlatforms)
        }
    }

    private fun setupMoviesRecyclerView() {
        val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerViewMovies)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Query Firestore collection "forms" and populate the images into the adapter
        firestore.collection("movies")
            .limit(6)
            .get()
            .addOnSuccessListener { documents ->

                val imageDataList = documents.mapNotNull { document ->
                    val imageUrl = document.getString("ImageUrl")
                    val name = document.getString("Movie Name")
                    if (imageUrl != null && name != null) {
                        ImageData(imageUrl, name)
                    } else {
                        null
                    }
                }

                recyclerView.adapter = ImageAdapter(imageDataList)
            }

        val mainActivity = requireActivity() as MainActivityAdmin
        val viewAllTextView: TextView = rootView.findViewById(R.id.textViewViewAllMovies)
        viewAllTextView.setOnClickListener {
            val movies = Movies()
            mainActivity.switchFragment(movies)
        }
    }
}

package tochi.learning.streammaster.fragments

import tochi.learning.streammaster.adapters.SubscriptionAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import tochi.learning.streammaster.Items.StreamingPlatform
import tochi.learning.streammaster.R

class AddSubscriptions : Fragment() {

    private lateinit var subscriptionAdapter: SubscriptionAdapter
    private val streamingPlatforms = mutableListOf<StreamingPlatform>() // Populate this list
    private val filteredPlatforms = mutableListOf<StreamingPlatform>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_subscriptions, container, false)

        subscriptionAdapter = SubscriptionAdapter(filteredPlatforms)
        val subscriptionsRecyclerView = view.findViewById<RecyclerView>(R.id.subscriptionsRecyclerView)
        subscriptionsRecyclerView.adapter = subscriptionAdapter
        subscriptionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        fetchStreamingPlatforms()

        val searchView = view.findViewById<SearchView>(R.id.search_bar)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    searchStreamingPlatforms(it)
                }
                return false
            }
        })

        val filterButton = view.findViewById<ImageButton>(R.id.filterButton)
        filterButton.setOnClickListener {
            showFilterDialog()
        }

        return view
    }

    private fun fetchStreamingPlatforms() {
        db.collection("streamingPlatforms")
            .get()
            .addOnSuccessListener { result ->
                streamingPlatforms.clear()
                for (document in result) {
                    val imageUrl = document.getString("ImageUrl")
                    val name = document.getString("Streaming Platform Name")
                    val category = document.getString("Streaming Platform Category")
                    imageUrl?.let { streamingPlatforms.add(StreamingPlatform(it, name ?: "", category ?: "")) }
                }
                filteredPlatforms.clear()
                filteredPlatforms.addAll(streamingPlatforms)
                subscriptionAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                // Handle error
            }
    }

    private fun searchStreamingPlatforms(query: String) {
        filteredPlatforms.clear()
        filteredPlatforms.addAll(streamingPlatforms.filter {
            it.name.contains(query, ignoreCase = true) || it.category.contains(query, ignoreCase = true)
        })
        subscriptionAdapter.notifyDataSetChanged()
    }

    private fun filterStreamingPlatformsByCategory(category: String) {
        filteredPlatforms.clear()
        filteredPlatforms.addAll(streamingPlatforms.filter {
            it.category.equals(category, ignoreCase = true)
        })
        subscriptionAdapter.notifyDataSetChanged()
    }

    private fun resetFilteredPlatforms() {
        filteredPlatforms.clear()
        filteredPlatforms.addAll(streamingPlatforms)
        subscriptionAdapter.notifyDataSetChanged()
    }

    private fun showFilterDialog() {
        val categories = streamingPlatforms.map { it.category }.distinct()
        val dialogItems = categories.toTypedArray()

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Filter by Category")
            .setItems(dialogItems) { dialog, which ->
                val selectedCategory = dialogItems[which]
                if (selectedCategory == "All") {
                    resetFilteredPlatforms()
                } else {
                    filterStreamingPlatformsByCategory(selectedCategory)
                }
            }
            .create()

        alertDialog.show()
    }


}

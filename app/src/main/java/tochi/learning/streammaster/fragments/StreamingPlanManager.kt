package tochi.learning.streammaster.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import tochi.learning.streammaster.R
import tochi.learning.streammaster.activities.MainActivityAdmin
import tochi.learning.streammaster.adapters.StreamingPlanAdapter
import tochi.learning.streammaster.models.StreamingPlanModel

class StreamingPlanManager : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var streamingPlansRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_streaming_plan_manager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageUrl = arguments?.getString("imageUrl")
        val name = arguments?.getString("name")
        val description = arguments?.getString("description")
        val streamCategoryName = arguments?.getString("streamCategoryName")

        val imageView: ImageView = view.findViewById(R.id.imageStreamPlatform)
        val textName: TextView = view.findViewById(R.id.textStreamPlatformName)
        val textDescription: TextView = view.findViewById(R.id.textStreamPlatformDescription)
        val textCategory: TextView = view.findViewById(R.id.textStreamCategory)

        if (imageUrl != null) {
            Glide.with(requireContext())
                .load(imageUrl)
                .into(imageView)
        }
        textName.text = name
        textDescription.text = description
        textCategory.text = streamCategoryName

        val addStreamingPlatform: FloatingActionButton = view.findViewById(R.id.fabOpenStreamingPlansDialog)
        streamingPlansRecyclerView = view.findViewById(R.id.recyclerViewStreamingPlans)

        addStreamingPlatform.setOnClickListener {
            openStreamingPlansDialog(name, streamCategoryName)
        }

        // Fetch and display streaming plans
        fetchAndDisplayStreamingPlans(name)
    }

    private fun openStreamingPlansDialog(streamingPlatformName: String?, streamCategoryName: String?) {
        val bundle = Bundle()
        bundle.putString("streamingPlatformName", streamingPlatformName)
        bundle.putString("streamCategoryName", streamCategoryName)

        val streamingPlansDialog = StreamingPlansDialog()
        streamingPlansDialog.arguments = bundle

        val fragmentManager = childFragmentManager
        streamingPlansDialog.show(fragmentManager, "StreamingPlansDialog")
    }

    private fun fetchAndDisplayStreamingPlans(streamingPlatformName: String?) {
        db.collection("plans")
            .whereEqualTo("Streaming Platform", streamingPlatformName)
            .get()
            .addOnSuccessListener { result ->
                val streamingPlans = mutableListOf<StreamingPlanModel>()
                for (document in result) {
                    val planName = document.getString("Plan Name")
                    val planDuration = document.getLong("Plan Duration")
                    val planPrice = document.getDouble("Plan Price")

                    if (planName != null && planDuration != null && planPrice != null) {
                        streamingPlans.add(StreamingPlanModel(planName, planDuration.toInt(), planPrice))
                    }
                }

                val adapter = StreamingPlanAdapter(streamingPlans)
                streamingPlansRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                streamingPlansRecyclerView.adapter = adapter
            }
            .addOnFailureListener { e ->
                showFailureToFetchPlansMessage()
            }
    }

    private fun showFailureToFetchPlansMessage() {
        val errorMessage = "Failed to fetch streaming plans"
        // Display the error message as needed
    }
}

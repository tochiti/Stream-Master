package tochi.learning.streammaster.fragments

import StreamingPlatformAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import tochi.learning.streammaster.Items.StreamingPlatformModel
import tochi.learning.streammaster.R
import tochi.learning.streammaster.activities.MainActivityAdmin

class StreamingPlatforms : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_streaming_platforms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addStreamingPlatform: FloatingActionButton = view.findViewById(R.id.addStreamingPlatform)

        addStreamingPlatform.setOnClickListener {
            val dialogFragment = AddStreamingPlatformManager()
            dialogFragment.show(childFragmentManager, "MyDialogFragment")
        }

        recyclerView = view.findViewById(R.id.recyclerViewPlatforms)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3) // 3 items per row

        fetchAndDisplayStreamingPlatforms()
    }

    private fun fetchAndDisplayStreamingPlatforms() {
        db.collection("streamingPlatforms")
            .get()
            .addOnSuccessListener { result ->
                val streamingPlatforms = mutableListOf<StreamingPlatformModel>()
                for (document in result) {
                    val imageUrl = document.getString("ImageUrl")
                    val name = document.getString("Streaming Platform Name")
                    val description = document.getString("Streaming Platform Description")
                    val streamCategoryName = document.getString("Streaming Platform Category") // Update this line

                    if (imageUrl != null && name != null && description != null && streamCategoryName != null) {
                        streamingPlatforms.add(StreamingPlatformModel(imageUrl, name, description, streamCategoryName))
                    }
                }

                val adapter = StreamingPlatformAdapter(streamingPlatforms) { streamingPlatform ->
                    val bundle = Bundle()
                    bundle.putString("imageUrl", streamingPlatform.imageUrl)
                    bundle.putString("name", streamingPlatform.name)
                    bundle.putString("description", streamingPlatform.description)
                    bundle.putString("streamCategoryName", streamingPlatform.streamCategoryName)

                    val streamingPlanManagerFragment = StreamingPlanManager()
                    streamingPlanManagerFragment.arguments = bundle

                    val mainActivity = requireActivity() as MainActivityAdmin
                    mainActivity.switchFragment(streamingPlanManagerFragment)
                }
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to fetch streaming platforms", Toast.LENGTH_SHORT).show()
            }
    }
}

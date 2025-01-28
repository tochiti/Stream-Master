package tochi.learning.streammaster.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import tochi.learning.streammaster.R
import android.widget.*
import java.util.*

class AddStreamingPlan : Fragment() {

    private lateinit var planName: EditText
    private lateinit var planDuration: EditText
    private lateinit var planPrice: EditText
    private lateinit var addButton: Button
    private lateinit var streamCategoryText: TextView
    private lateinit var streamingPlatformText: TextView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_streaming_plan, container, false)

        planName = view.findViewById(R.id.planName)
        planDuration = view.findViewById(R.id.planDuration)
        planPrice = view.findViewById(R.id.planPrice)
        addButton = view.findViewById(R.id.btn_add_plan)
        streamCategoryText = view.findViewById(R.id.streamCategory)
        streamingPlatformText = view.findViewById(R.id.streamingPlatform)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val streamingPlatformName = arguments?.getString("streamingPlatformName")
        val streamCategoryName = arguments?.getString("streamCategoryName") // Correct key here

        streamCategoryText.text = streamCategoryName ?: ""
        streamingPlatformText.text = streamingPlatformName ?: ""

        addButton.setOnClickListener {
            val name = planName.text.toString()
            val duration = planDuration.text.toString()
            val price = planPrice.text.toString()

            if (name.isNotEmpty() && duration.isNotEmpty() && price.isNotEmpty()) {
                // Call function to add plan to Firestore
                addPlanToFirestore(name, duration.toInt(), price.toDouble(), streamingPlatformName, streamCategoryName)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addPlanToFirestore(name: String, duration: Int, price: Double, streamingPlatformName: String?, streamCategoryName: String?) {
        // Replace with your Firestore collection and fields
        val data = hashMapOf(
            "Plan Name" to name,
            "Plan Duration" to duration,
            "Plan Price" to price,
            "Stream Category" to streamCategoryName, // Correct key here
            "Streaming Platform" to streamingPlatformName
        )

        db.collection("plans")
            .add(data)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Plan added successfully!", Toast.LENGTH_SHORT).show()
                clearFields()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error adding plan", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFields() {
        planName.text.clear()
        planDuration.text.clear()
        planPrice.text.clear()
    }
}

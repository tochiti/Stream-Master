package tochi.learning.streammaster.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import tochi.learning.streammaster.R
import tochi.learning.streammaster.activities.MainActivityAdmin

class StreamingPlansDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = LayoutInflater.from(requireContext())
        val dialogView: View = inflater.inflate(R.layout.fragment_stream_plan_dialog, null)

        val addStreamingPlan: LinearLayout = dialogView.findViewById(R.id.addStreamingPlan)
        val updateStreamingPlan: LinearLayout = dialogView.findViewById(R.id.updateStreamingPlan)
        val deleteStreamingPlan: LinearLayout = dialogView.findViewById(R.id.deleteStreamingPlan)

        val mainActivity = requireActivity() as MainActivityAdmin

        addStreamingPlan.setOnClickListener {
            val streamingPlatformName = arguments?.getString("streamingPlatformName")
            val streamingCategoryName = arguments?.getString("streamCategoryName") // Corrected the argument name

            val addStreamingPlanFragment = AddStreamingPlan().apply {
                arguments = Bundle().apply {
                    putString("streamingPlatformName", streamingPlatformName)
                    putString("streamCategoryName", streamingCategoryName)
                }
            }
            mainActivity.switchFragment(addStreamingPlanFragment)
            dismiss()
        }

        // Rest of your code for update and delete

        builder.setView(dialogView)

        return builder.create()
    }
}

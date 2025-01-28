package tochi.learning.streammaster.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import tochi.learning.streammaster.R
import tochi.learning.streammaster.activities.MainActivityAdmin


class AddStreamingPlatformManager : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = LayoutInflater.from(requireContext())
        val dialogView: View = inflater.inflate(R.layout.fragment_add_streaming_platform_manager, null)

        val addStreamingPlatform: LinearLayout = dialogView.findViewById(R.id.addStreamingPlatform)
        val updateStreamingPlatform: LinearLayout = dialogView.findViewById(R.id.updateStreamingPlatform)
        val deleteStreamingPlatform: LinearLayout = dialogView.findViewById(R.id.deleteStreamingPlatform)

        val mainActivity = requireActivity() as MainActivityAdmin

        addStreamingPlatform.setOnClickListener {
            val addStreamingPlatformFragment = AddStreamingPlatform()
            mainActivity.switchFragment(addStreamingPlatformFragment)
            dismiss()
        }

        updateStreamingPlatform.setOnClickListener {
            val updateStreamCategoryFragment = UpdateStreamCategory()
            mainActivity.switchFragment(updateStreamCategoryFragment)
            dismiss()
        }

        deleteStreamingPlatform.setOnClickListener {
            val deleteStreamCategoryFragment = DeleteStreamCategory()
            mainActivity.switchFragment(deleteStreamCategoryFragment)
            dismiss()
        }

        builder.setView(dialogView)

        return builder.create()
    }

}
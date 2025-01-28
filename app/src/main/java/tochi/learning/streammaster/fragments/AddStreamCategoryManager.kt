package tochi.learning.streammaster.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import tochi.learning.streammaster.R
import tochi.learning.streammaster.activities.MainActivityAdmin

class AddStreamCategoryManager : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = LayoutInflater.from(requireContext())
        val dialogView: View = inflater.inflate(R.layout.fragment_add_stream_category, null)

        val addStreamCategory: LinearLayout = dialogView.findViewById(R.id.addStreamCategory)
        val updateStreamCategory: LinearLayout = dialogView.findViewById(R.id.updateStreamCategory)
        val deleteStreamCategory: LinearLayout = dialogView.findViewById(R.id.deleteStreamCategory)

        val mainActivity = requireActivity() as MainActivityAdmin

        addStreamCategory.setOnClickListener {
            val addStreamCategoryFragment = AddStreamCategory()
            mainActivity.switchFragment(addStreamCategoryFragment)
            dismiss()
        }

        updateStreamCategory.setOnClickListener {
            val updateStreamCategoryFragment = UpdateStreamCategory()
            mainActivity.switchFragment(updateStreamCategoryFragment)
            dismiss()
        }

        deleteStreamCategory.setOnClickListener {
            val deleteStreamCategoryFragment = DeleteStreamCategory()
            mainActivity.switchFragment(deleteStreamCategoryFragment)
            dismiss()
        }

        builder.setView(dialogView)

        return builder.create()
    }
}




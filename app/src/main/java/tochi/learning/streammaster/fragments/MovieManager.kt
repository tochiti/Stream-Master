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

class MovieManager : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = LayoutInflater.from(requireContext())
        val dialogView: View = inflater.inflate(R.layout.fragment_movie_manager, null)

        val addMovie: LinearLayout = dialogView.findViewById(R.id.addMovie)
        val updateMovie: LinearLayout = dialogView.findViewById(R.id.updateMovie)
        val deleteMovie: LinearLayout = dialogView.findViewById(R.id.deleteMovie)

        val mainActivity = requireActivity() as MainActivityAdmin

        addMovie.setOnClickListener {
            val addMovieFragment = addMovie()
            mainActivity.switchFragment(addMovieFragment)
            dismiss()
        }

        updateMovie.setOnClickListener {
            val updateMovieFragment = UpdateMovie()
            mainActivity.switchFragment(updateMovieFragment)
            dismiss()
        }

        deleteMovie.setOnClickListener {
            val deleteMovieFragment = DeleteMovie()
            mainActivity.switchFragment(deleteMovieFragment)
            dismiss()
        }




        // Rest of your code for update and delete

        builder.setView(dialogView)

        return builder.create()
    }
}
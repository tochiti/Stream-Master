package tochi.learning.streammaster.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tochi.learning.streammaster.R
import tochi.learning.streammaster.databinding.FragmentAdminDashboardBinding
import tochi.learning.streammaster.databinding.FragmentCategoriesBinding


class Categories : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)



        return binding.root
    }

}
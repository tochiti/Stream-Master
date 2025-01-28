package tochi.learning.streammaster.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tochi.learning.streammaster.R
import tochi.learning.streammaster.databinding.FragmentAddStreamCategoryBinding
import tochi.learning.streammaster.databinding.FragmentAdminDashboardBinding
import tochi.learning.streammaster.databinding.FragmentAdminSettingsBinding


class AdminSettings : Fragment() {

    private lateinit var binding: FragmentAdminSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminSettingsBinding.inflate(inflater, container, false)



        return binding.root
    }


}
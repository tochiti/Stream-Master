package tochi.learning.streammaster.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tochi.learning.streammaster.R
import tochi.learning.streammaster.databinding.FragmentActiveSubscriptionsBinding
import tochi.learning.streammaster.databinding.FragmentAdminDashboardBinding


class ActiveSubscriptions : Fragment() {
    private lateinit var binding: FragmentActiveSubscriptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentActiveSubscriptionsBinding.inflate(inflater, container, false)



        return binding.root
    }

}
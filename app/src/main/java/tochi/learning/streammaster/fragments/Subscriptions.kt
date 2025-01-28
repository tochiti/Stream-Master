package tochi.learning.streammaster.fragments

import SubscriptionsAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import tochi.learning.streammaster.R



class Subscriptions : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_subscriptions, container, false)

        val viewPager: ViewPager = view.findViewById(R.id.viewPager)
        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)

        val adapter = SubscriptionsAdapter(childFragmentManager) // Use childFragmentManager if this is a child fragment
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        return view
    }

    }
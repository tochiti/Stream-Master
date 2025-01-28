package tochi.learning.streammaster.activities

import Movies
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import tochi.learning.streammaster.R
import tochi.learning.streammaster.databinding.ActivityMainAdminBinding
import tochi.learning.streammaster.fragments.AdminSettings
import tochi.learning.streammaster.fragments.StreamingPlatforms
import tochi.learning.streammaster.fragments.AdminDashboard
import tochi.learning.streammaster.fragments.StreamCategories

class MainActivityAdmin : AppCompatActivity() {

    private lateinit var binding: ActivityMainAdminBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()

        // Set default selection
        navView.setCheckedItem(R.id.Dashboard)
        setFragment(AdminDashboard(), getString(R.string.dashboard))

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Dashboard -> setFragment(AdminDashboard(), menuItem.title.toString())
                R.id.streamCategories -> setFragment(StreamCategories(), menuItem.title.toString())
                R.id.streamingPlatforms -> setFragment(StreamingPlatforms(), menuItem.title.toString())
                R.id.Movies -> setFragment(Movies(), menuItem.title.toString())
                R.id.adminSettings -> setFragment(AdminSettings(), menuItem.title.toString())
            }

            drawerLayout.closeDrawers()
            true
        }


        binding.toggleButton.setOnClickListener {
            if (drawerLayout.isDrawerOpen(navView)) {
                drawerLayout.closeDrawer(navView)
            } else {
                drawerLayout.openDrawer(navView)
            }
        }
    }

    private fun setupViews() {
        drawerLayout = binding.drawerLayout
        navView = binding.navView

        toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.nav_open, R.string.nav_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setFragment(fragment: Fragment, title: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_content, fragment)
            .commit()
        binding.titleTextview.text = title
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_content, fragment)
            .commit()
    }

    //...
}

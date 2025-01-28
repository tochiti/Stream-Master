package tochi.learning.streammaster.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import tochi.learning.streammaster.R
import tochi.learning.streammaster.adapters.OnboardingAdapter
import tochi.learning.streammaster.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var mViewPager: ViewPager2
    private lateinit var btnCreateAccount: Button
    private lateinit var btnLogIn: Button

    private lateinit var binding: ActivityOnboardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        btnCreateAccount = binding.btnCreateAccount
        btnCreateAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogIn = binding.btnLogIn
        btnLogIn.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
            finish()
        }

        mViewPager = findViewById(R.id.viewPager)
        mViewPager.adapter = OnboardingAdapter(this, this)
        TabLayoutMediator(binding.pageIndicator, mViewPager) { _, _ -> }.attach()
        mViewPager.offscreenPageLimit = 1
    }
}
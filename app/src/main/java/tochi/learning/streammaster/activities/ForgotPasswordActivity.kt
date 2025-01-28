package tochi.learning.streammaster.activities

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import tochi.learning.streammaster.R
import tochi.learning.streammaster.databinding.ActivityForgotPasswordBinding



class ForgotPasswordActivity : AppCompatActivity() {


    private lateinit var backBtn: ImageButton
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        backBtn = binding.backButtonForgotPasswordPage
        backBtn.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}
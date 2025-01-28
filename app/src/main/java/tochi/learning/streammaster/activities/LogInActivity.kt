package tochi.learning.streammaster.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import tochi.learning.streammaster.R
import tochi.learning.streammaster.databinding.ActivityLogInBinding

class tLogInActivity : AppCompatActivity() {


    // Initializing variables
    private lateinit var btnForgotPassword: TextView
    private lateinit var backBtn: ImageButton
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var btnLogin: Button
    private lateinit var registerRedirect: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


    private lateinit var binding: ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        btnLogin = findViewById(R.id.btn_Log_in)
        registerRedirect = findViewById(R.id.registerRedirect)
        db = Firebase.firestore

        auth = Firebase.auth

        btnLogin.setOnClickListener {
            login()
        }

        btnForgotPassword = binding.forgotPassword
        btnForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        registerRedirect = binding.registerRedirect
        registerRedirect.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        backBtn = binding.backButtonLoginPage
        backBtn.setOnClickListener {
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun login() {
        val email = email.text.toString()
        val password = password.text.toString()

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                val db = FirebaseFirestore.getInstance()
                db.collection("users").document(user!!.uid)
                    .get()
                    .addOnSuccessListener { document ->
                        val isAdmin = document.getBoolean("isAdmin") ?: false

                        if (isAdmin) {
                            val adminIntent = Intent(this, MainActivityAdmin::class.java)
                            startActivity(adminIntent)
                        } else {
                            val userIntent = Intent(this, MainActivityUser::class.java)
                            startActivity(userIntent)
                        }

                        finish()
                    }
                    .addOnFailureListener { exception ->
                        // Handle error if fetching user role fails
                        Toast.makeText(
                            this,
                            "Error fetching user role: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(
                    this,
                    "Log in unsuccessful,\nCheck Email or Password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
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
import tochi.learning.streammaster.databinding.ActivityRegisterBinding
import javax.security.auth.login.LoginException

class RegisterActivity : AppCompatActivity() {

    // Initializing Variables
    private lateinit var backBtn: ImageButton
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var loginRedirect: TextView
    private lateinit var email: EditText
    private lateinit var name: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var btnRegister: Button

    // Creating Firebase Authentication Objects
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        email = binding.email
        name = binding.name
        password = binding.password
        confirmPassword = binding.confirmPassword
        btnRegister = binding.btnCreateAccount
        loginRedirect = binding.loginRedirect

        auth = Firebase.auth
        db = Firebase.firestore

        btnRegister.setOnClickListener {
            registerUser()
        }

        loginRedirect.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
            finish()
        }

        backBtn = binding.backButtonRegisterPage
        backBtn.setOnClickListener {
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // ... (rest of your imports and class definition)

    private fun registerUser() {
        val username = name.text.toString()
        val userEmail = email.text.toString()
        val userPassword = password.text.toString()
        val userConfirmPassword = confirmPassword.text.toString()

        // Check for empty fields
        if (userEmail.isBlank() || userPassword.isBlank() || userConfirmPassword.isBlank() || username.isBlank()) {
            Toast.makeText(this, "Username, Email, and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        // Password Confirmation
        if (userPassword != userConfirmPassword) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show()
            return
        }

        // If all Credentials are correct
        // we call createUserWithEmailAndPassword
        auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null) {
                    // Create a user document with the username in the "users" collection
                    val userDocument = db.collection("users").document(user.uid)
                    val userData = hashMapOf(
                        "username" to username,
                        "email" to userEmail
                    )

                    userDocument.set(userData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LogInActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            // Print the actual error message for debugging
                            e.printStackTrace()
                            Toast.makeText(this, "Error storing user data: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "Account Creation Not Successful,\n Please Try Again", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

package tochi.learning.streammaster.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import tochi.learning.streammaster.databinding.FragmentHomeBinding
import java.util.Calendar




class Home : Fragment() {


    // ViewBinding
    private var binding: FragmentHomeBinding? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var welcome: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // Inflate binding
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        db = Firebase.firestore
        auth = Firebase.auth
        welcome = binding!!.welcome

        val userId = Firebase.auth.currentUser?.uid

        // Get username
//        val user = auth.currentUser
        val docRef = userId?.let { db.collection("users").document(it) }
        docRef?.get()?.addOnSuccessListener {
            val username = it.get("username") as String

            displayPersonalizedGreeting(username)
        }




        return binding!!.root
    }

        private fun displayPersonalizedGreeting(username: String) {
        val calendar = Calendar.getInstance()
        val timeOfDay = calendar[Calendar.HOUR_OF_DAY]

        val greeting = when(timeOfDay){
            in 0..11 -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            in 16..20 -> "Good Evening"
            else -> "Good Night"
        }

        welcome.text = "$greeting $username!"

    }
        // ... greeting logic



}
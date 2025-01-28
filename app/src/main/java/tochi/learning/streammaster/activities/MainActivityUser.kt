package tochi.learning.streammaster.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import tochi.learning.streammaster.R
import tochi.learning.streammaster.databinding.ActivityMainBinding

class MainActivityUser : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fab:FloatingActionButton
//    private lateinit var auth: FirebaseAuth
//    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        db = Firebase.firestore
//        auth = Firebase.auth
        fab = findViewById(R.id.fab)

//        val userId = Firebase.auth.currentUser?.uid

        // Get username
//        val docRef = userId?.let { db.collection("users").document(it) }
//        if (docRef != null) {
//            docRef.get().addOnSuccessListener {
//                val username = it.get("username") as String
//
//                displayPersonalizedGreeting(username)
//            }
//        }

        val navView: BottomNavigationView = binding.bottomNavigationView

        val navController = findNavController(R.id.fragment_container)


        navView.setupWithNavController(navController)

        fab.setOnClickListener {
            addSubscription()
        }

    }

    private fun addSubscription() {
        val sheetView = layoutInflater.inflate(R.layout.fragment_add_subscriptions, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(sheetView)
        dialog.show()
    }


}
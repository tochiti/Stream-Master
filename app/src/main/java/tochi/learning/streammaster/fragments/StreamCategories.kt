package tochi.learning.streammaster.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import tochi.learning.streammaster.Items.CategoryItem
import tochi.learning.streammaster.R
import tochi.learning.streammaster.adapters.CategoryAdapter


class StreamCategories : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private val categoriesCollection = db.collection("forms")

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(tochi.learning.streammaster.R.layout.fragment_stream_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var addStreamCategoryOptions: FloatingActionButton = view.findViewById(R.id.addStreamCategoryOptions)

        addStreamCategoryOptions.setOnClickListener {
            val dialogFragment = AddStreamCategoryManager()
            dialogFragment.show(childFragmentManager, "MyDialogFragment")

        }

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        categoryAdapter = CategoryAdapter(requireContext())
        recyclerView.adapter = categoryAdapter

        fetchCategories()




    }
    private fun fetchCategories() {
        categoriesCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val categories = ArrayList<CategoryItem>()

                for (document in querySnapshot.documents) {
                    val categoryName = document.getString("Category Name") ?: ""
                    val categoryImageUrl = document.getString("ImageUrl") ?: ""
                    val categoryDescription = document.getString("Category Description") ?: ""
                    categories.add(CategoryItem(categoryImageUrl, categoryName, categoryDescription))
                }

                categoryAdapter.setItems(categories)
            }
            .addOnFailureListener { exception ->
                // Handle error
            }
    }
}


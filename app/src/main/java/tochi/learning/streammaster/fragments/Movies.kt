import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import tochi.learning.streammaster.Items.Movie
import tochi.learning.streammaster.R // Replace with your R file package
import tochi.learning.streammaster.fragments.AddStreamCategoryManager
import tochi.learning.streammaster.fragments.MovieManager

class Movies : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private val moviesCollection = db.collection("movies")

    private lateinit var recyclerView: RecyclerView
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var addMovieManager: FloatingActionButton = view.findViewById(R.id.fabMovieManager)

        addMovieManager.setOnClickListener {
            val dialogFragment = MovieManager()
            dialogFragment.show(childFragmentManager, "MyDialogFragment")

        }

        recyclerView = view.findViewById(R.id.recyclerViewMovies)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3) // 3 items per row

        moviesAdapter = MoviesAdapter(emptyList())
        recyclerView.adapter = moviesAdapter

        fetchAndDisplayMovies()
    }

    private fun fetchAndDisplayMovies() {
        moviesCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val movies = mutableListOf<Movie>()

                for (document in querySnapshot.documents) {
                    val movieName = document.getString("Movie Name") ?: ""
                    val imageUrl = document.getString("ImageUrl") ?: ""
                    movies.add(Movie(movieName, imageUrl))
                }

                moviesAdapter = MoviesAdapter(movies)
                recyclerView.adapter = moviesAdapter
            }
            .addOnFailureListener { exception ->
                val errorMessage = "Failed to fetch movies: ${exception.message}"
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
    }
}


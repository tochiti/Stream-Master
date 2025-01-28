import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tochi.learning.streammaster.Items.Movie
import tochi.learning.streammaster.R

class MoviesAdapter(private val movies: List<Movie>) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieImage: ImageView = itemView.findViewById(R.id.movieImage)
        val movieName: TextView = itemView.findViewById(R.id.movieName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]

        // Load image using Glide or another image loading library
        Glide.with(holder.itemView.context)
            .load(movie.imageUrl)
            .placeholder(R.drawable.rounded_background) // Placeholder image
            .into(holder.movieImage)

        holder.movieName.text = movie.name
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}

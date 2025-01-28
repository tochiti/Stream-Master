import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tochi.learning.streammaster.Items.StreamingPlatformModel
import tochi.learning.streammaster.R

class StreamingPlatformAdapter(
    private val streamingPlatforms: List<StreamingPlatformModel>,
    private val onItemClickListener: (StreamingPlatformModel) -> Unit
) : RecyclerView.Adapter<StreamingPlatformAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_streaming_platform, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val streamingPlatform = streamingPlatforms[position]
        holder.bind(streamingPlatform)
    }

    override fun getItemCount(): Int {
        return streamingPlatforms.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageStreamPlatform)

        fun bind(streamingPlatform: StreamingPlatformModel) {
            Glide.with(itemView.context)
                .load(streamingPlatform.imageUrl)
                .into(imageView)

            itemView.setOnClickListener {
                onItemClickListener(streamingPlatform)
            }
        }
    }
}

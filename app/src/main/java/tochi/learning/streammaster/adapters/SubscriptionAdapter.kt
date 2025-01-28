package tochi.learning.streammaster.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tochi.learning.streammaster.Items.StreamingPlatform
import tochi.learning.streammaster.R

class SubscriptionAdapter(private val platforms: List<StreamingPlatform>) :
    RecyclerView.Adapter<SubscriptionAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       private val streamingPlatformImage: ImageView = itemView.findViewById(R.id.streamingPlatformImage)
       private val streamingPlatformName: TextView = itemView.findViewById(R.id.streamingPlatformName)
       private val streamingPlatformCategory: TextView = itemView.findViewById(R.id.streamingPlatformCategory)
        val addButton: Button = itemView.findViewById(R.id.addButton)

        fun bind(item: StreamingPlatform) {
            Glide.with(itemView)
                .load(item.imageUrl)
                .placeholder(R.drawable.placeholder_image) // Placeholder image
                .error(R.drawable.error_image) // Error image if loading fails
                .into(streamingPlatformImage)

            streamingPlatformName.text = item.name
            streamingPlatformCategory.text = item.category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_streaming_platform, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = platforms[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return platforms.size
    }
}

package tochi.learning.streammaster.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tochi.learning.streammaster.Items.ImageData
import tochi.learning.streammaster.R

class ImageAdapter(private val imageDataList: List<ImageData>) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageDashboard)
        val nameTextView: TextView = view.findViewById(R.id.nameDashboard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageData = imageDataList[position]

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(imageData.imageUrl)
            .into(holder.imageView)

        holder.nameTextView.text = imageData.name
    }

    override fun getItemCount(): Int {
        return imageDataList.size
    }
}

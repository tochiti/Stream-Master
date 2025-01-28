package tochi.learning.streammaster.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import tochi.learning.streammaster.R

class ImageMovieAdapter(
    private val context: Context,
    private val imageUrls: List<String>,
    private val selectedDocumentIds: List<String>
) : BaseAdapter() {

    override fun getCount(): Int {
        return imageUrls.size
    }

    override fun getItem(position: Int): Any {
        return imageUrls[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageUrl = imageUrls[position]
        val isSelected = selectedDocumentIds.contains(imageUrl)

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val gridViewItem = inflater.inflate(R.layout.grid_item_image, parent, false)
        val imageView = gridViewItem.findViewById<ImageView>(R.id.imageView)
        val selectedRing = gridViewItem.findViewById<ImageView>(R.id.selectedRing)

        if (isSelected) {
            selectedRing.visibility = View.VISIBLE // Show the selected ring
            Glide.with(context)
                .load(R.drawable.selected_placeholder)
                .into(imageView)
        } else {
            selectedRing.visibility = View.INVISIBLE // Hide the selected ring
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(imageView)
        }

        return gridViewItem
    }
}

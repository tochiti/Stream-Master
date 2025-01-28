package tochi.learning.streammaster.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tochi.learning.streammaster.Items.CategoryItem
import tochi.learning.streammaster.R

class CategoryAdapter(private val context: Context) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private val categories = ArrayList<CategoryItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]

        Glide.with(context)
            .load(category.imageUrl)
            .into(holder.categoryImage)

        holder.categoryName.text = category.categoryName
        holder.categoryDescription.text = category.categoryDescription

        holder.itemView.setOnClickListener {
            // Handle item click, navigate to StreamCategoriesFull with category details
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    fun setItems(newCategories: List<CategoryItem>) {
        categories.clear()
        categories.addAll(newCategories)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryImage: ImageView = itemView.findViewById(R.id.categoryImage)
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        val categoryDescription: TextView = itemView.findViewById(R.id.categoryDescription)
    }
}

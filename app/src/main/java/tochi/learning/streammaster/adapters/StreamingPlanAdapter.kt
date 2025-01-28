package tochi.learning.streammaster.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tochi.learning.streammaster.R
import tochi.learning.streammaster.models.StreamingPlanModel

class StreamingPlanAdapter(private val streamingPlans: List<StreamingPlanModel>) :
    RecyclerView.Adapter<StreamingPlanAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textPlanName: TextView = itemView.findViewById(R.id.textPlanName)
        val textPlanPrice: TextView = itemView.findViewById(R.id.textPlanPrice)
        val textPlanDuration: TextView = itemView.findViewById(R.id.textPlanDuration)

        fun bind(plan: StreamingPlanModel) {
            textPlanName.text = plan.name
            textPlanPrice.text = "$ ${plan.price}"
            textPlanDuration.text = "${plan.duration} days"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_streaming_plan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plan = streamingPlans[position]
        holder.bind(plan)
    }

    override fun getItemCount(): Int {
        return streamingPlans.size
    }
}

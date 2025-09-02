package com.tunisietelecom.agent.ui.technical

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tunisietelecom.agent.R
import com.tunisietelecom.agent.data.api.TechnicalInfo
import com.tunisietelecom.agent.databinding.ItemTechnicalInfoBinding

class TechnicalInfoAdapter(
    private val onItemClick: (TechnicalInfo) -> Unit
) : ListAdapter<TechnicalInfo, TechnicalInfoAdapter.TechnicalInfoViewHolder>(TechnicalInfoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechnicalInfoViewHolder {
        val binding = ItemTechnicalInfoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TechnicalInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TechnicalInfoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TechnicalInfoViewHolder(
        private val binding: ItemTechnicalInfoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(techInfo: TechnicalInfo) {
            binding.apply {
                textViewTitle.text = techInfo.title
                textViewContent.text = techInfo.content
                textViewLastUpdated.text = "Mis à jour: ${techInfo.lastUpdated}"

                // Set priority chip
                chipPriority.text = techInfo.priority
                chipPriority.setChipBackgroundColorResource(
                    when (techInfo.priority.lowercase()) {
                        "haute" -> R.color.red
                        "moyenne" -> R.color.orange
                        "basse" -> R.color.green
                        else -> R.color.blue
                    }
                )

                root.setOnClickListener {
                    onItemClick(techInfo)
                }
            }
        }
    }

    private class TechnicalInfoDiffCallback : DiffUtil.ItemCallback<TechnicalInfo>() {
        override fun areItemsTheSame(oldItem: TechnicalInfo, newItem: TechnicalInfo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TechnicalInfo, newItem: TechnicalInfo): Boolean {
            return oldItem == newItem
        }
    }
}

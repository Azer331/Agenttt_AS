package com.tunisietelecom.agent.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tunisietelecom.agent.R
import com.tunisietelecom.agent.data.models.Offer
import com.tunisietelecom.agent.databinding.ItemOfferBinding

class OffersAdapter(
    private val onOfferClick: (Offer) -> Unit
) : ListAdapter<Offer, OffersAdapter.OfferViewHolder>(OfferDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val binding = ItemOfferBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OfferViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class OfferViewHolder(
        private val binding: ItemOfferBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(offer: Offer) {
            binding.apply {
                textViewTitle.text = offer.title
                textViewDescription.text = offer.description
                textViewPrice.text = offer.price
                textViewValidUntil.text = "Valable jusqu'au: ${offer.validUntil}"
                textViewCategory.text = offer.category

                // Load image with Glide
                offer.imageUrl?.let { imageUrl ->
                    Glide.with(imageViewOffer.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_offer_placeholder)
                        .error(R.drawable.ic_offer_placeholder)
                        .into(imageViewOffer)
                }

                root.setOnClickListener {
                    onOfferClick(offer)
                }

                // Show active status
                chipActive.text = if (offer.isActive) "Active" else "Inactive"
                chipActive.setChipBackgroundColorResource(
                    if (offer.isActive) R.color.green else R.color.red
                )
            }
        }
    }

    private class OfferDiffCallback : DiffUtil.ItemCallback<Offer>() {
        override fun areItemsTheSame(oldItem: Offer, newItem: Offer): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Offer, newItem: Offer): Boolean {
            return oldItem == newItem
        }
    }
}

package com.tunisietelecom.agent.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tunisietelecom.agent.R
import com.tunisietelecom.agent.data.models.Client
import com.tunisietelecom.agent.data.models.ClientStatus
import com.tunisietelecom.agent.databinding.ItemClientBinding

class ClientsAdapter(
    private val onClientClick: (Client) -> Unit,
    private val onEditClick: (Client) -> Unit
) : ListAdapter<Client, ClientsAdapter.ClientViewHolder>(ClientDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val binding = ItemClientBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ClientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ClientViewHolder(
        private val binding: ItemClientBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(client: Client) {
            binding.apply {
                textViewName.text = client.name
                textViewEmail.text = client.email
                textViewPhone.text = client.phone
                textViewSubscription.text = client.subscriptionType
                textViewJoinDate.text = "Client depuis: ${client.joinDate}"

                // Set status chip
                chipStatus.text = when (client.status) {
                    ClientStatus.ACTIVE -> "Actif"
                    ClientStatus.INACTIVE -> "Inactif"
                    ClientStatus.SUSPENDED -> "Suspendu"
                    ClientStatus.PENDING -> "En attente"
                }

                chipStatus.setChipBackgroundColorResource(
                    when (client.status) {
                        ClientStatus.ACTIVE -> R.color.green
                        ClientStatus.INACTIVE -> R.color.orange
                        ClientStatus.SUSPENDED -> R.color.red
                        ClientStatus.PENDING -> R.color.blue
                    }
                )

                root.setOnClickListener {
                    onClientClick(client)
                }

                btnEdit.setOnClickListener {
                    onEditClick(client)
                }
            }
        }
    }

    private class ClientDiffCallback : DiffUtil.ItemCallback<Client>() {
        override fun areItemsTheSame(oldItem: Client, newItem: Client): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Client, newItem: Client): Boolean {
            return oldItem == newItem
        }
    }
}
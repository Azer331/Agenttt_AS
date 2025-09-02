package com.tunisietelecom.agent.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tunisietelecom.agent.databinding.FragmentAdminBinding

class AdminFragment : Fragment() {

    private var _binding: FragmentAdminBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdminViewModel by viewModels()
    private lateinit var clientsAdapter: ClientsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkAccess()
        setupUI()
        observeViewModel()
    }

    private fun checkAccess() {
        viewModel.checkSpecialAgentAccess()
    }

    private fun setupUI() {
        setupRecyclerView()
        setupSearchFunctionality()

        binding.fabAddClient.setOnClickListener {
            // Navigate to add client screen or show dialog
            showAddClientDialog()
        }

        binding.btnRefresh.setOnClickListener {
            viewModel.loadClients()
        }
    }

    private fun setupRecyclerView() {
        clientsAdapter = ClientsAdapter(
            onClientClick = { client ->
                // Navigate to client detail screen
                viewModel.selectClient(client)
            },
            onEditClick = { client ->
                // Show edit client dialog
                showEditClientDialog(client)
            }
        )

        binding.recyclerViewClients.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = clientsAdapter
        }
    }

    private fun setupSearchFunctionality() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchClients(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    viewModel.loadClients()
                } else {
                    viewModel.searchClients(newText)
                }
                return true
            }
        })
    }

    private fun observeViewModel() {
        viewModel.hasAccess.observe(viewLifecycleOwner, Observer { hasAccess ->
            if (hasAccess) {
                binding.layoutAccessDenied.visibility = View.GONE
                binding.layoutAdminContent.visibility = View.VISIBLE
                viewModel.loadClients()
            } else {
                binding.layoutAccessDenied.visibility = View.VISIBLE
                binding.layoutAdminContent.visibility = View.GONE
            }
        })

        viewModel.clients.observe(viewLifecycleOwner, Observer { clients ->
            clientsAdapter.submitList(clients)
            binding.textViewClientsCount.text = "Total: ${clients.size} clients"
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showAddClientDialog() {
        // Implementation for add client dialog
        Toast.makeText(context, "Ajouter un client - À implémenter", Toast.LENGTH_SHORT).show()
    }

    private fun showEditClientDialog(client: com.tunisietelecom.agent.data.models.Client) {
        // Implementation for edit client dialog
        Toast.makeText(context, "Modifier ${client.name} - À implémenter", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

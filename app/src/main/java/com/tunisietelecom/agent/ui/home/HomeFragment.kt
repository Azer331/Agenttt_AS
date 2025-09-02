package com.tunisietelecom.agent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tunisietelecom.agent.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var offersAdapter: OffersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        viewModel.loadOffers()
    }

    private fun setupRecyclerView() {
        offersAdapter = OffersAdapter { offer ->
            // Handle offer click
        }

        binding.recyclerViewOffers.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = offersAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.offers.observe(viewLifecycleOwner, Observer { offers ->
            offersAdapter.submitList(offers)
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            binding.textViewError.visibility = if (error != null) View.VISIBLE else View.GONE
            binding.textViewError.text = error
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

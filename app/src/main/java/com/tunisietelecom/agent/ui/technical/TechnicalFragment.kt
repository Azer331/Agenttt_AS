package com.tunisietelecom.agent.ui.technical

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tunisietelecom.agent.databinding.FragmentTechnicalBinding

class TechnicalFragment : Fragment() {

    private var _binding: FragmentTechnicalBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TechnicalViewModel by viewModels()
    private lateinit var technicalAdapter: TechnicalInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTechnicalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        viewModel.loadTechnicalInfo()
    }

    private fun setupRecyclerView() {
        technicalAdapter = TechnicalInfoAdapter { techInfo ->
            // Handle technical info item click
            // Could navigate to detail view or expand content
        }

        binding.recyclerViewTechnical.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = technicalAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.technicalInfo.observe(viewLifecycleOwner, Observer { techInfoList ->
            technicalAdapter.submitList(techInfoList)
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

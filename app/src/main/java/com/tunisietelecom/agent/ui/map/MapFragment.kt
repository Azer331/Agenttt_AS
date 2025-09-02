package com.tunisietelecom.agent.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tunisietelecom.agent.databinding.FragmentMapBinding

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup placeholder content
        binding.textViewPlaceholder.text = """
            Fonctionnalité Carte - À venir
            
            Cette section contiendra:
            • Localisation des clients
            • Points de vente Tunisie Telecom
            • Zones de couverture réseau
            • Navigation GPS
            
            L'intégration Google Maps sera ajoutée prochainement.
        """.trimIndent()

        binding.btnComingSoon.setOnClickListener {
            // Could show more details about upcoming features
            // or navigate to settings
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

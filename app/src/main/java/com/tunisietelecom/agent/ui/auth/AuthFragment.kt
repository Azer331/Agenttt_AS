package com.tunisietelecom.agent.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tunisietelecom.agent.R
import com.tunisietelecom.agent.databinding.FragmentAuthBinding

class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()
    private var isLoginMode = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.btnAuth.setOnClickListener {
            if (isLoginMode) {
                performLogin()
            } else {
                performRegister()
            }
        }

        binding.tvSwitchMode.setOnClickListener {
            switchMode()
        }
    }

    private fun performLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (validateLoginInput(email, password)) {
            viewModel.login(email, password)
        }
    }

    private fun performRegister() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val agentId = binding.etAgentId.text.toString().trim()
        val department = binding.etDepartment.text.toString().trim()

        if (validateRegisterInput(email, password, firstName, lastName, agentId, department)) {
            viewModel.register(email, password, firstName, lastName, agentId, department)
        }
    }

    private fun switchMode() {
        isLoginMode = !isLoginMode
        updateUIForMode()
    }

    private fun updateUIForMode() {
        if (isLoginMode) {
            binding.layoutRegisterFields.visibility = View.GONE
            binding.btnAuth.text = "Se connecter"
            binding.tvSwitchMode.text = "Créer un compte"
        } else {
            binding.layoutRegisterFields.visibility = View.VISIBLE
            binding.btnAuth.text = "S'inscrire"
            binding.tvSwitchMode.text = "J'ai déjà un compte"
        }
    }

    private fun observeViewModel() {
        viewModel.authResult.observe(viewLifecycleOwner, Observer { result ->
            binding.progressBar.visibility = View.GONE
            binding.btnAuth.isEnabled = true

            result.fold(
                onSuccess = {
                    findNavController().navigate(R.id.homeFragment)
                },
                onFailure = { error ->
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }
            )
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnAuth.isEnabled = !isLoading
        })
    }

    private fun validateLoginInput(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                binding.etEmail.error = "Email requis"
                false
            }
            password.isEmpty() -> {
                binding.etPassword.error = "Mot de passe requis"
                false
            }
            else -> true
        }
    }

    private fun validateRegisterInput(
        email: String, password: String, firstName: String,
        lastName: String, agentId: String, department: String
    ): Boolean {
        return when {
            email.isEmpty() -> {
                binding.etEmail.error = "Email requis"
                false
            }
            password.isEmpty() -> {
                binding.etPassword.error = "Mot de passe requis"
                false
            }
            firstName.isEmpty() -> {
                binding.etFirstName.error = "Prénom requis"
                false
            }
            lastName.isEmpty() -> {
                binding.etLastName.error = "Nom requis"
                false
            }
            agentId.isEmpty() -> {
                binding.etAgentId.error = "ID Agent requis"
                false
            }
            department.isEmpty() -> {
                binding.etDepartment.error = "Département requis"
                false
            }
            else -> true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

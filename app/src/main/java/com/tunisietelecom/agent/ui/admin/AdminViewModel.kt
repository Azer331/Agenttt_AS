package com.tunisietelecom.agent.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunisietelecom.agent.data.models.Client
import com.tunisietelecom.agent.data.models.User
import com.tunisietelecom.agent.data.models.UserRole
import com.tunisietelecom.agent.data.repository.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

class AdminViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _hasAccess = MutableLiveData<Boolean>()
    val hasAccess: LiveData<Boolean> = _hasAccess

    private val _clients = MutableLiveData<List<Client>>()
    val clients: LiveData<List<Client>> = _clients

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var allClients: List<Client> = emptyList()

    fun checkSpecialAgentAccess() {
        viewModelScope.launch {
            try {
                val token = repository.getUserToken()
                if (token != null) {
                    val response = repository.getUserProfile("Bearer $token")
                    if (response.isSuccessful) {
                        val user = response.body()
                        _hasAccess.value = user?.role in listOf(
                            UserRole.SPECIAL_AGENT,
                            UserRole.SUPERVISOR,
                            UserRole.ADMIN
                        )
                    } else {
                        _hasAccess.value = false
                    }
                } else {
                    _hasAccess.value = false
                }
            } catch (e: Exception) {
                _hasAccess.value = false
                _error.value = "Erreur de vérification d'accès: ${e.message}"
            }
        }
    }

    fun loadClients() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Mock data for demonstration - replace with actual API call
                val mockClients = generateMockClients()
                allClients = mockClients
                _clients.value = mockClients
            } catch (e: Exception) {
                _error.value = "Erreur lors du chargement des clients: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchClients(query: String) {
        val filteredClients = allClients.filter { client ->
            client.name.contains(query, ignoreCase = true) ||
                    client.email.contains(query, ignoreCase = true) ||
                    client.phone.contains(query, ignoreCase = true)
        }
        _clients.value = filteredClients
    }

    fun selectClient(client: Client) {
        // Handle client selection for detailed view
        _error.value = "Détails de ${client.name} - À implémenter"
    }

    private fun generateMockClients(): List<Client> {
        return listOf(
            Client(
                id = "1",
                name = "Ahmed Ben Ali",
                email = "ahmed.benali@email.com",
                phone = "+216 20 123 456",
                address = "Tunis, Tunisie",
                subscriptionType = "Fibre 100MB",
                status = com.tunisietelecom.agent.data.models.ClientStatus.ACTIVE,
                joinDate = "15/03/2023",
                lastContact = "20/08/2025"
            ),
            Client(
                id = "2",
                name = "Fatma Salah",
                email = "fatma.salah@email.com",
                phone = "+216 25 987 654",
                address = "Sfax, Tunisie",
                subscriptionType = "Mobile 20GB",
                status = com.tunisietelecom.agent.data.models.ClientStatus.ACTIVE,
                joinDate = "08/01/2024",
                lastContact = "18/08/2025"
            ),
            Client(
                id = "3",
                name = "Mohamed Trabelsi",
                email = "mohamed.trabelsi@email.com",
                phone = "+216 22 456 789",
                address = "Sousse, Tunisie",
                subscriptionType = "Fibre 50MB",
                status = com.tunisietelecom.agent.data.models.ClientStatus.SUSPENDED,
                joinDate = "22/11/2022",
                lastContact = "10/08/2025"
            ),
            Client(
                id = "4",
                name = "Leila Mansouri",
                email = "leila.mansouri@email.com",
                phone = "+216 24 321 098",
                address = "Monastir, Tunisie",
                subscriptionType = "Mobile 50GB",
                status = com.tunisietelecom.agent.data.models.ClientStatus.PENDING,
                joinDate = "05/08/2025",
                lastContact = null
            )
        )
    }
}
package com.tunisietelecom.agent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunisietelecom.agent.data.models.Offer
import com.tunisietelecom.agent.data.repository.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _offers = MutableLiveData<List<Offer>>()
    val offers: LiveData<List<Offer>> = _offers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadOffers() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = repository.getOffers()
                if (response.isSuccessful) {
                    _offers.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Erreur lors du chargement des offres"
                }
            } catch (e: Exception) {
                _error.value = "Erreur de connexion: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshOffers() {
        loadOffers()
    }
}
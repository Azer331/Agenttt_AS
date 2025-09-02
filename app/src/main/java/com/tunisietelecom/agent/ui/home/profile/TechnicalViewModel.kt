package com.tunisietelecom.agent.ui.technical

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunisietelecom.agent.data.api.TechnicalInfo
import com.tunisietelecom.agent.data.repository.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

class TechnicalViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _technicalInfo = MutableLiveData<List<TechnicalInfo>>()
    val technicalInfo: LiveData<List<TechnicalInfo>> = _technicalInfo

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadTechnicalInfo() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = repository.getTechnicalInfo()
                if (response.isSuccessful) {
                    _technicalInfo.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Erreur lors du chargement des informations techniques"
                }
            } catch (e: Exception) {
                _error.value = "Erreur de connexion: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshTechnicalInfo() {
        loadTechnicalInfo()
    }
}
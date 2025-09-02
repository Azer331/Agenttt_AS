package com.tunisietelecom.agent.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunisietelecom.agent.data.repository.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _authResult = MutableLiveData<Result<Unit>>()
    val authResult: LiveData<Result<Unit>> = _authResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.login(email, password)
                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        repository.saveUserToken(loginResponse.token)
                        _authResult.value = Result.success(Unit)
                    }
                } else {
                    _authResult.value = Result.failure(Exception("Identifiants incorrects"))
                }
            } catch (e: Exception) {
                _authResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(
        email: String, password: String, firstName: String,
        lastName: String, agentId: String, department: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.register(email, password, firstName, lastName, agentId, department)
                if (response.isSuccessful) {
                    // After successful registration, automatically login
                    login(email, password)
                } else {
                    _authResult.value = Result.failure(Exception("Erreur lors de l'inscription"))
                }
            } catch (e: Exception) {
                _authResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
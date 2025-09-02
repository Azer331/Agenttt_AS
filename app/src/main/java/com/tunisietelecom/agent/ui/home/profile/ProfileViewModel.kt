package com.tunisietelecom.agent.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunisietelecom.agent.data.models.User
import com.tunisietelecom.agent.data.repository.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    fun loadUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val token = repository.getUserToken()
                if (token != null) {
                    val response = repository.getUserProfile("Bearer $token")
                    if (response.isSuccessful) {
                        _user.value = response.body()
                    } else {
                        _message.value = "Erreur lors du chargement du profil"
                    }
                }
            } catch (e: Exception) {
                _message.value = "Erreur de connexion: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateProfile(firstName: String, lastName: String, department: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentUser = _user.value
                if (currentUser != null) {
                    val updatedUser = currentUser.copy(
                        firstName = firstName,
                        lastName = lastName,
                        department = department
                    )

                    val token = repository.getUserToken()
                    if (token != null) {
                        val response = repository.updateUserProfile("Bearer $token", updatedUser)
                        if (response.isSuccessful) {
                            _user.value = response.body()
                            _message.value = "Profil mis à jour avec succès"
                        } else {
                            _message.value = "Erreur lors de la mise à jour"
                        }
                    }
                }
            } catch (e: Exception) {
                _message.value = "Erreur: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateNotificationSettings(enabled: Boolean) {
        // Implementation for notification settings
        _message.value = if (enabled) "Notifications activées" else "Notifications désactivées"
    }

    fun updateThemeSettings(darkMode: Boolean) {
        // Implementation for theme settings
        _message.value = if (darkMode) "Mode sombre activé" else "Mode clair activé"
    }

    fun logout() {
        repository.logout()
    }
}
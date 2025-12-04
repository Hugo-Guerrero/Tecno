package com.hugoguerrero.tecno.ui.screens.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hugoguerrero.tecno.domain.use_case.GetCurrentUserUseCase
import com.hugoguerrero.tecno.domain.use_case.GetUserProfileUseCase
import com.hugoguerrero.tecno.domain.use_case.UpdateUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class EditProfileNavigation {
    object NavigateBack : EditProfileNavigation()
}

data class EditProfileState(
    val displayName: String = "",
    val bio: String = "",
    val paymentDetails: String = "",
    val photoUri: Uri? = null,
    val initialPhotoUrl: String? = null,
    val isLoading: Boolean = false,
    val isInitialLoading: Boolean = true,
    val userMessage: String? = null
)

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<EditProfileNavigation>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isInitialLoading = true)
            val user = getCurrentUserUseCase()
            if (user != null) {
                val profile = getUserProfileUseCase(user.uid)
                if (profile != null) {
                    _uiState.value = _uiState.value.copy(
                        displayName = profile.displayName,
                        bio = profile.bio ?: "",
                        paymentDetails = profile.paymentDetails ?: "",
                        initialPhotoUrl = profile.photoUrl,
                        isInitialLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isInitialLoading = false, userMessage = "No se pudo cargar el perfil.")
                }
            } else {
                _uiState.value = _uiState.value.copy(isInitialLoading = false, userMessage = "Error de autenticación.")
            }
        }
    }

    fun onDisplayNameChange(name: String) {
        _uiState.value = _uiState.value.copy(displayName = name)
    }

    fun onBioChange(bio: String) {
        _uiState.value = _uiState.value.copy(bio = bio)
    }

    fun onPaymentDetailsChange(details: String) {
        _uiState.value = _uiState.value.copy(paymentDetails = details)
    }

    fun onPhotoUriChange(uri: Uri?) {
        _uiState.value = _uiState.value.copy(photoUri = uri)
    }

    fun saveProfile() {
        viewModelScope.launch {
            val currentUser = getCurrentUserUseCase()
            if (currentUser == null) {
                _uiState.value = _uiState.value.copy(userMessage = "Error de autenticación. No se pudo guardar el perfil.")
                return@launch
            }

            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = updateUserProfileUseCase(
                uid = currentUser.uid,
                displayName = _uiState.value.displayName,
                bio = _uiState.value.bio,
                paymentDetails = _uiState.value.paymentDetails,
                photoUri = _uiState.value.photoUri
            )
            result.onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false, userMessage = "Perfil actualizado correctamente")
                _navigationEvent.emit(EditProfileNavigation.NavigateBack)
            }.onFailure {
                _uiState.value = _uiState.value.copy(isLoading = false, userMessage = it.message)
            }
        }
    }

    fun userMessageShown() {
        _uiState.value = _uiState.value.copy(userMessage = null)
    }
}

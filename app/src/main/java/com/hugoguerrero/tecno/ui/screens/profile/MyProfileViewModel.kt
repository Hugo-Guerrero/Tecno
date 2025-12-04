package com.hugoguerrero.tecno.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hugoguerrero.tecno.data.model.Project
import com.hugoguerrero.tecno.data.model.UserProfile
import com.hugoguerrero.tecno.domain.use_case.DeleteProjectUseCase
import com.hugoguerrero.tecno.domain.use_case.GetCurrentUserUseCase
import com.hugoguerrero.tecno.domain.use_case.GetProjectsByOwnerUseCase
import com.hugoguerrero.tecno.domain.use_case.GetUserProfileUseCase
import com.hugoguerrero.tecno.domain.use_case.SignOutUseCase
import com.hugoguerrero.tecno.ui.screens.login.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getProjectsByOwnerUseCase: GetProjectsByOwnerUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyProfileState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val user = getCurrentUserUseCase()
            if (user != null) {
                val profile = getUserProfileUseCase(user.uid)
                _uiState.value = _uiState.value.copy(profile = profile)
                profile?.let {
                    getProjectsByOwnerUseCase(it.uid).collect { projects ->
                        _uiState.value = _uiState.value.copy(projects = projects, isLoading = false)
                    }
                } ?: run {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = "No se pudo cargar el perfil.")
                }
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Error de autenticación.")
            }
        }
    }

    fun onDeleteProjectClicked(project: Project) {
        _uiState.value = _uiState.value.copy(projectToDelete = project)
    }

    fun confirmProjectDeletion() {
        viewModelScope.launch {
            _uiState.value.projectToDelete?.let {
                deleteProjectUseCase(it.id)
                _uiState.value = _uiState.value.copy(projectToDelete = null)
            }
        }
    }

    fun cancelProjectDeletion() {
        _uiState.value = _uiState.value.copy(projectToDelete = null)
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
            _navigationEvent.emit(NavigationEvent.NavigateToLogin)
        }
    }
}

data class MyProfileState(
    val profile: UserProfile? = null,
    val projects: List<Project> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val projectToDelete: Project? = null
)

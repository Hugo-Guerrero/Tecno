package com.hugoguerrero.tecno.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.hugoguerrero.tecno.data.model.Project
import com.hugoguerrero.tecno.data.model.UserProfile
import com.hugoguerrero.tecno.domain.use_case.GetProjectsByOwnerUseCase
import com.hugoguerrero.tecno.domain.use_case.GetUserProfileUseCase
import com.hugoguerrero.tecno.ui.components.ProjectCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicProfileScreen(
    navController: NavController,
    viewModel: PublicProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.userProfile?.displayName ?: "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        PublicProfileContent(modifier = Modifier.padding(padding), state = state, navController = navController)
    }
}

@Composable
private fun PublicProfileContent(modifier: Modifier, state: PublicProfileState, navController: NavController) {
    Box(modifier = modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (state.error != null) {
            Text(state.error, modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.error)
        } else if (state.userProfile != null) {
            LazyColumn(contentPadding = PaddingValues(vertical = 16.dp)) {
                item {
                    PublicProfileHeader(profile = state.userProfile)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Proyectos de ${state.userProfile.displayName}", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(horizontal = 16.dp))
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                }
                if (state.projects.isEmpty()) {
                    item {
                        Text(
                            "Este usuario aún no ha creado ningún proyecto.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(state.projects) { project ->
                        ProjectCard(project = project, onClick = { /* Lógica de navegación */ })
                    }
                }
            }
        }
    }
}

@Composable
private fun PublicProfileHeader(profile: UserProfile) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        AsyncImage(
            model = profile.photoUrl.ifEmpty { "https://via.placeholder.com/150" },
            contentDescription = "Foto de perfil",
            modifier = Modifier.size(120.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(profile.displayName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(profile.email, style = MaterialTheme.typography.bodyMedium)
        profile.bio?.let {
            Text(it, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@HiltViewModel
class PublicProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getProjectsByOwnerUseCase: GetProjectsByOwnerUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(PublicProfileState(isLoading = true))
    val uiState: StateFlow<PublicProfileState> = _uiState.asStateFlow()

    init {
        savedStateHandle.get<String>("userId")?.let { userId ->
            loadProfile(userId)
        } ?: run {
            _uiState.value = PublicProfileState(error = "ID de usuario no proporcionado")
        }
    }

    private fun loadProfile(userId: String) {
        viewModelScope.launch {
            val userProfile = getUserProfileUseCase(userId)
            if (userProfile != null) {
                _uiState.value = _uiState.value.copy(userProfile = userProfile)
                getProjectsByOwnerUseCase(userId).onEach { projects ->
                    _uiState.value = _uiState.value.copy(projects = projects, isLoading = false)
                }.catch { e ->
                    _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
                }.launchIn(viewModelScope)
            } else {
                _uiState.value = PublicProfileState(error = "No se pudo cargar el perfil del usuario")
            }
        }
    }
}

data class PublicProfileState(
    val userProfile: UserProfile? = null,
    val projects: List<Project> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

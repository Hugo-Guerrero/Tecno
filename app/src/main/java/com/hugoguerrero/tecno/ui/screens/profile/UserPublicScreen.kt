package com.hugoguerrero.tecno.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.hugoguerrero.tecno.data.model.Project
import com.hugoguerrero.tecno.data.model.UserProfile
import com.hugoguerrero.tecno.data.model.UserStats
import com.hugoguerrero.tecno.domain.model.UserType
import com.hugoguerrero.tecno.domain.use_case.*
import com.hugoguerrero.tecno.ui.navigation.AUTH_GRAPH_ROUTE
import com.hugoguerrero.tecno.ui.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    navController: NavController,
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.NavigateToAuth -> {
                    navController.navigate(AUTH_GRAPH_ROUTE) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Mi Perfil", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Tecno-Ciencia", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFB30059))
            )
        },
        containerColor = Color(0xFF0F0015)
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color(0xFF0F0015))
        ) {
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFFF66CC))
                }
            } else {
                state.userProfile?.let { user ->
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier.size(120.dp).clip(CircleShape).background(Color(0xFF9F0D2A)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(user.displayName.take(2).uppercase(), color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(user.displayName, color = Color(0xFFFF66CC), fontWeight = FontWeight.Bold, fontSize = 22.sp)
                        Text(
                            when (user.userType) {
                                UserType.STUDENT -> "👨‍🎓 Estudiante"
                                UserType.MANAGER -> "👨‍💼 Manager"
                                UserType.SPONSOR -> "💰 Patrocinador"
                                else -> "👤 Usuario"
                            },
                            color = Color.LightGray, fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatItem(value = state.userProjects.size.toString(), label = "Proyectos", color = Color(0xFFFF66CC))
                            StatItem(value = state.userStats.donationsCount.toString(), label = "Donaciones", color = Color(0xFFFF66CC))
                            StatItem(value = "${state.userStats.successRate}%", label = "Éxito", color = Color(0xFFFF66CC))
                        }
                    }

                    Button(
                        onClick = { viewModel.signOut() },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Cerrar Sesión")
                    }

                    state.error?.let {
                        Text(text = it, color = Color.Red, modifier = Modifier.padding(horizontal = 16.dp), textAlign = TextAlign.Center)
                    }

                    PostsSection(
                        projects = state.userProjects,
                        onProjectClick = { projectId ->
                            navController.navigate(Screens.ProjectDetail.route + "/$projectId")
                        }
                    )

                } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se pudo cargar el perfil.", color = Color.Gray)
                }
            }
        }
    }
}

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserProjectsUseCase: GetProjectsByOwnerUseCase,
    private val getUserStatsUseCase: GetUserStatsUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserProfileState())
    val uiState: StateFlow<UserProfileState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _uiState.value = UserProfileState(isLoading = true)
            val currentUserId = getCurrentUserUseCase()?.uid

            if (currentUserId == null) {
                _uiState.value = UserProfileState(error = "No se ha iniciado sesión.")
                return@launch
            }

            val userProfile = getUserProfileUseCase(currentUserId)
            if (userProfile == null) {
                _uiState.value = UserProfileState(error = "No se pudo cargar el perfil.")
                return@launch
            }
            _uiState.value = _uiState.value.copy(userProfile = userProfile)

            getUserProjectsUseCase(currentUserId).onEach { projects ->
                _uiState.value = _uiState.value.copy(userProjects = projects)
            }.catch { e ->
                _uiState.value = _uiState.value.copy(error = e.message)
            }.launchIn(viewModelScope)

            getUserStatsUseCase(currentUserId).onEach { result ->
                result.onSuccess {
                    _uiState.value = _uiState.value.copy(userStats = it)
                }
            }.launchIn(viewModelScope)

            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
            _navigationEvent.emit(NavigationEvent.NavigateToAuth)
        }
    }
}

data class UserProfileState(
    val userProfile: UserProfile? = null,
    val userProjects: List<Project> = emptyList(),
    val userStats: UserStats = UserStats(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class NavigationEvent {
    object NavigateToAuth : NavigationEvent()
}

@Composable
fun StatItem(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(label, color = Color.LightGray, fontSize = 12.sp)
    }
}

@Composable
fun PostsSection(projects: List<Project>, onProjectClick: (String) -> Unit) {
    if (projects.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("📝", fontSize = 48.sp, modifier = Modifier.padding(bottom = 16.dp))
                Text("Aún no tienes proyectos", color = Color(0xFFFF66CC), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Crea tu primer proyecto para comenzar", color = Color.LightGray, fontSize = 14.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp))
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(projects) { project ->
                ProjectPostItem(project = project, onClick = { onProjectClick(project.id) })
            }
        }
    }
}

@Composable
fun ProjectPostItem(project: Project, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A001F)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = project.title, color = Color(0xFFFF66CC), fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = project.description, color = Color(0xFFD1B2FF), fontSize = 14.sp, maxLines = 2)
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { project.progress / 100f },
                color = Color(0xFFB30059),
                trackColor = Color(0xFF4B0054),
                modifier = Modifier.fillMaxWidth().height(8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${project.progress.toInt()}% completado", color = Color(0xFFD1B2FF), fontSize = 12.sp)
                Text("$${project.currentFunding.toInt()} / $${project.fundingGoal.toInt()}", color = Color(0xFFD1B2FF), fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                project.tags.take(2).forEach { tag ->
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Color(0xFF330033)).padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = tag, color = Color(0xFFFF66CC), fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

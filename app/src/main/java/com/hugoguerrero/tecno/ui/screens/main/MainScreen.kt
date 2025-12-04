package com.hugoguerrero.tecno.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.hugoguerrero.tecno.data.model.Project
import com.hugoguerrero.tecno.domain.use_case.GetProjectsUseCase
import com.hugoguerrero.tecno.ui.components.BottomNavigationBar
import com.hugoguerrero.tecno.ui.components.ProjectCard
import com.hugoguerrero.tecno.ui.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Proyectos Recientes") }) },
        bottomBar = { BottomNavigationBar(navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screens.UploadProject.route) }) {
                Icon(Icons.Default.Add, contentDescription = "Subir Proyecto")
            }
        }
    ) { padding ->
        MainContent(
            modifier = Modifier.padding(padding),
            state = state,
            onProjectClick = { projectId ->
                navController.navigate(Screens.ProjectDetail.route + "/${projectId}")
            },
            onUploadClick = { navController.navigate(Screens.UploadProject.route) }
        )
    }
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    state: MainState,
    onProjectClick: (String) -> Unit,
    onUploadClick: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            state.error != null -> {
                Text(
                    text = state.error,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
            state.projects.isEmpty() -> {
                EmptyState(onUploadClick)
            }
            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.projects) { project ->
                        ProjectCard(project = project, onClick = { onProjectClick(project.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState(onUploadClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.ListAlt, contentDescription = null, modifier = Modifier.size(64.dp))
        Text(
            text = "Aún no hay proyectos disponibles. ¡Sé el primero en crear uno!",
            modifier = Modifier.padding(top = 16.dp),
            textAlign = TextAlign.Center
        )
        Button(onClick = onUploadClick, modifier = Modifier.padding(top = 24.dp)) {
            Text("Crear un Proyecto")
        }
    }
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getProjectsUseCase: GetProjectsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainState(isLoading = true))
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

    init {
        getProjectsUseCase()
            .onEach { projects ->
                _uiState.value = MainState(projects = projects)
            }
            .catch { exception ->
                _uiState.value = MainState(error = exception.message)
            }
            .launchIn(viewModelScope)
    }
}

data class MainState(
    val projects: List<Project> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

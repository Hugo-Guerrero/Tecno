package com.hugoguerrero.tecno.ui.screens.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.hugoguerrero.tecno.data.model.Donation
import com.hugoguerrero.tecno.data.model.Project
import com.hugoguerrero.tecno.data.model.UserProfile
import com.hugoguerrero.tecno.domain.use_case.GetDonationsUseCase
import com.hugoguerrero.tecno.domain.use_case.ObserveProjectByIdUseCase
import com.hugoguerrero.tecno.domain.use_case.GetUserProfileUseCase
import com.hugoguerrero.tecno.ui.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class DonorInfo(val profile: UserProfile, val donation: Donation)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    navController: NavController,
    viewModel: ProjectDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.project?.title ?: "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            state.project?.let {
                ExtendedFloatingActionButton(
                    onClick = { navController.navigate(Screens.Donation.route + "/${it.id}") },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Apoyar") },
                    text = { Text("Apoyar este proyecto") },
                    elevation = FloatingActionButtonDefaults.elevation(8.dp)
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                state.error != null -> {
                    Text(text = state.error!!, modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.error)
                }
                state.project != null -> {
                    ProjectDetailContent(navController, state) { uri ->
                        try {
                            uriHandler.openUri(uri)
                        } catch (e: Exception) { /* Opcional: Manejar error */ }
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectDetailContent(navController: NavController, state: ProjectDetailState, onDocumentClick: (String) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(bottom = 80.dp)) {
        item { ProjectHeader(project = state.project!!) }
        item { ProjectAuthor(profile = state.authorProfile, navController = navController) }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { FundingInfo(project = state.project!!) }
        item { ProjectDescription(description = state.project!!.description) }

        if (state.project!!.documentUrls.isNotEmpty()) {
            item { SectionTitle(title = "Documentos Adjuntos", icon = Icons.Default.Description) }
            items(state.project.documentUrls) {
                DocumentItem(documentUrl = it, onClick = { onDocumentClick(it) })
            }
        }

        if (state.donors.isNotEmpty()) {
            item { SectionTitle(title = "Donantes Recientes", icon = Icons.Default.Group) }
            items(state.donors) {
                DonorItem(donorInfo = it, onClick = { navController.navigate(Screens.PublicProfile.route + "/${it.profile.uid}") })
            }
        }
    }
}

@Composable
fun ProjectHeader(project: Project) {
    AsyncImage(
        model = project.imageUrl,
        contentDescription = "Imagen del proyecto",
        modifier = Modifier.fillMaxWidth().height(220.dp).background(MaterialTheme.colorScheme.surfaceVariant),
        contentScale = ContentScale.Crop
    )
    Text(
        text = project.title,
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Composable
fun ProjectAuthor(profile: UserProfile?, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { profile?.let { navController.navigate(Screens.PublicProfile.route + "/${it.uid}") } },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = profile?.photoUrl,
            contentDescription = "Autor",
            modifier = Modifier.size(32.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant)
        )
        Text(
            text = "por ${profile?.displayName ?: "..."}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
fun FundingInfo(project: Project) {
    OutlinedCard(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            val formattedProgress = "%.1f".format(project.progress)
            Text("Recaudado: $${project.currentFunding.toInt()} de $${project.fundingGoal.toInt()}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = project.progress / 100f,
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                strokeCap = StrokeCap.Round
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("$formattedProgress% completado", style = MaterialTheme.typography.bodySmall, modifier = Modifier.align(Alignment.End))
        }
    }
}

@Composable
fun ProjectDescription(description: String) {
    Text(
        text = description,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun SectionTitle(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)) {
        Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.secondary)
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
fun DocumentItem(documentUrl: String, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text("Ver Documento", fontWeight = FontWeight.SemiBold) },
        leadingContent = { Icon(Icons.Default.Description, contentDescription = null) },
        modifier = Modifier.clickable(onClick = onClick).padding(horizontal = 8.dp)
    )
}

@Composable
fun DonorItem(donorInfo: DonorInfo, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(donorInfo.profile.displayName, fontWeight = FontWeight.SemiBold) },
        leadingContent = {
            AsyncImage(
                model = donorInfo.profile.photoUrl,
                contentDescription = "Foto de perfil de ${donorInfo.profile.displayName}",
                modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant)
            )
        },
        modifier = Modifier.clickable(onClick = onClick).padding(horizontal = 8.dp)
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProjectDetailViewModel @Inject constructor(
    private val observeProjectByIdUseCase: ObserveProjectByIdUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getDonationsUseCase: GetDonationsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectDetailState(isLoading = true))
    val uiState: StateFlow<ProjectDetailState> = _uiState.asStateFlow()

    init {
        savedStateHandle.get<String>("projectId")?.let { projectId ->
            val projectFlow = observeProjectByIdUseCase(projectId)
            val donationsFlow = getDonationsUseCase(projectId)

            projectFlow.flatMapLatest { project ->
                if (project == null) {
                    return@flatMapLatest flowOf(ProjectDetailState(error = "Proyecto no encontrado", isLoading = false))
                }

                val authorFlow = flow { emit(getUserProfileUseCase(project.ownerUid)) }

                val donorsFlow = donationsFlow.flatMapLatest { donations ->
                    if (donations.isEmpty()) {
                        flowOf(emptyList<DonorInfo>()) // Return empty list if no donors
                    } else {
                        val donorInfoFlows = donations.map { donation ->
                            flow {
                                emit(getUserProfileUseCase(donation.donorUid)?.let { profile ->
                                    DonorInfo(profile, donation)
                                })
                            }
                        }
                        combine(donorInfoFlows) { donorInfos ->
                            donorInfos.filterNotNull() // Filter out any null profiles
                        }
                    }
                }

                combine(authorFlow, donorsFlow) { author, donors ->
                    ProjectDetailState(project = project, authorProfile = author, donors = donors, isLoading = false)
                }
            }
            .onStart { _uiState.value = ProjectDetailState(isLoading = true) } 
            .catch { e -> _uiState.value = ProjectDetailState(error = e.localizedMessage ?: "Ocurrió un error", isLoading = false) }
            .onEach { state ->
                _uiState.value = state
            }
            .launchIn(viewModelScope)

        } ?: run {
            _uiState.value = ProjectDetailState(error = "ID de proyecto no proporcionado", isLoading = false)
        }
    }
}

data class ProjectDetailState(
    val project: Project? = null,
    val authorProfile: UserProfile? = null,
    val donors: List<DonorInfo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

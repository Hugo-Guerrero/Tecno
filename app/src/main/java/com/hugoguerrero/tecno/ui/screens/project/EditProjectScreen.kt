package com.hugoguerrero.tecno.ui.screens.project

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.hugoguerrero.tecno.domain.use_case.GetProjectByIdUseCase
import com.hugoguerrero.tecno.domain.use_case.UpdateProjectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProjectScreen(
    navController: NavController,
    viewModel: EditProjectViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> viewModel.onImageSelected(uri) }
    )

    val documentPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris -> viewModel.onDocumentsSelected(uris) }
    )

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onUserMessageShown()
        }
    }

    LaunchedEffect(state.isProjectUpdated) {
        if (state.isProjectUpdated) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { Text("Editar Proyecto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (state.initialLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            EditProjectForm(
                modifier = Modifier.padding(padding),
                state = state,
                viewModel = viewModel,
                onImageSelect = { imagePickerLauncher.launch("image/*") },
                onDocumentSelect = { documentPickerLauncher.launch("*/*") }
            )
        }
    }
}

@Composable
private fun EditProjectForm(
    modifier: Modifier,
    state: EditProjectState,
    viewModel: EditProjectViewModel,
    onImageSelect: () -> Unit,
    onDocumentSelect: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        OutlinedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                OutlinedTextField(value = state.title, onValueChange = viewModel::onTitleChange, label = { Text("Título del proyecto") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(value = state.description, onValueChange = viewModel::onDescriptionChange, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth(), minLines = 4)
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                CategoryDropdown(state.category, viewModel::onCategoryChange)
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(value = state.institution, onValueChange = viewModel::onInstitutionChange, label = { Text("Institución") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(value = state.fundingGoal, onValueChange = viewModel::onFundingGoalChange, label = { Text("Meta de financiamiento") }, modifier = Modifier.fillMaxWidth())
            }
        }

        Spacer(Modifier.height(16.dp))

        ImagePicker(state.newImageUri ?: state.currentImageUrl, onImageSelect)
        Spacer(Modifier.height(16.dp))
        DocumentPicker(state.newDocumentUris, onDocumentSelect)

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = viewModel::updateProject,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                Text("Guardar Cambios")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryDropdown(selectedCategory: String, onCategoryChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val categories = listOf("Tecnología", "Salud", "Educación", "Medio Ambiente", "Arte y Cultura", "Social")

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selectedCategory,
            onValueChange = {},
            readOnly = true,
            label = { Text("Categoría") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            categories.forEach { category ->
                DropdownMenuItem(text = { Text(category) }, onClick = { onCategoryChange(category); expanded = false })
            }
        }
    }
}

@Composable
private fun ImagePicker(image: Any?, onImageSelect: () -> Unit) {
    OutlinedButton(onClick = onImageSelect, modifier = Modifier.fillMaxWidth()) {
        Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
        Text("Cambiar Imagen de Portada")
    }
    if (image != null) {
        AsyncImage(model = image, contentDescription = "Imagen seleccionada", modifier = Modifier.fillMaxWidth().height(200.dp).padding(top = 8.dp))
    }
}

@Composable
private fun DocumentPicker(documentUris: List<Uri>, onDocumentSelect: () -> Unit) {
    OutlinedButton(onClick = onDocumentSelect, modifier = Modifier.fillMaxWidth()) {
        Icon(Icons.Default.AttachFile, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
        Text("Adjuntar Nuevos Documentos")
    }
    documentUris.forEach { uri ->
        Text("Nuevo: ${uri.lastPathSegment}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 16.dp, top = 4.dp))
    }
}

@HiltViewModel
class EditProjectViewModel @Inject constructor(
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val updateProjectUseCase: UpdateProjectUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProjectState())
    val uiState: StateFlow<EditProjectState> = _uiState.asStateFlow()

    private val projectId: String = savedStateHandle.get<String>("projectId")!!

    init {
        loadProjectDetails()
    }

    private fun loadProjectDetails() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(initialLoading = true)
            val project = getProjectByIdUseCase(projectId)
            if (project != null) {
                _uiState.value = _uiState.value.copy(
                    projectId = project.id,
                    title = project.title,
                    description = project.description,
                    category = project.category,
                    institution = project.institution,
                    fundingGoal = project.fundingGoal.toString(),
                    currentImageUrl = project.imageUrl,
                    initialLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(userMessage = "No se pudo cargar el proyecto.", initialLoading = false)
            }
        }
    }

    fun onTitleChange(title: String) { _uiState.value = _uiState.value.copy(title = title) }
    fun onDescriptionChange(description: String) { _uiState.value = _uiState.value.copy(description = description) }
    fun onCategoryChange(category: String) { _uiState.value = _uiState.value.copy(category = category) }
    fun onInstitutionChange(institution: String) { _uiState.value = _uiState.value.copy(institution = institution) }
    fun onFundingGoalChange(goal: String) { _uiState.value = _uiState.value.copy(fundingGoal = goal) }
    fun onImageSelected(uri: Uri?) { _uiState.value = _uiState.value.copy(newImageUri = uri) }
    fun onDocumentsSelected(uris: List<Uri>) { _uiState.value = _uiState.value.copy(newDocumentUris = uris) }

    fun updateProject() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.title.isBlank() || state.description.isBlank() || state.fundingGoal.toDoubleOrNull() == null) {
                _uiState.value = _uiState.value.copy(userMessage = "Por favor, completa todos los campos.")
                return@launch
            }

            _uiState.value = state.copy(isLoading = true)

            val result = updateProjectUseCase(
                projectId = state.projectId,
                title = state.title,
                description = state.description,
                category = state.category,
                institution = state.institution,
                fundingGoal = state.fundingGoal.toDouble(),
                newImageUri = state.newImageUri,
                newDocumentUris = state.newDocumentUris
            )

            result.onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false, isProjectUpdated = true)
            }.onFailure {
                _uiState.value = _uiState.value.copy(isLoading = false, userMessage = it.message ?: "Error desconocido")
            }
        }
    }

    fun onUserMessageShown() {
        _uiState.value = _uiState.value.copy(userMessage = null)
    }
}

data class EditProjectState(
    val projectId: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val institution: String = "",
    val fundingGoal: String = "",
    val currentImageUrl: String? = null,
    val newImageUri: Uri? = null,
    val newDocumentUris: List<Uri> = emptyList(),
    val isLoading: Boolean = false,
    val isProjectUpdated: Boolean = false,
    val userMessage: String? = null,
    val initialLoading: Boolean = true
)

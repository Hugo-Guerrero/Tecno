package com.hugoguerrero.tecno.ui.screens.project

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.hugoguerrero.tecno.domain.use_case.CreateProjectUseCase
import com.hugoguerrero.tecno.domain.use_case.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadProjectScreen(
    navController: NavController,
    viewModel: CreateProjectViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
            scope.launch { snackbarHostState.showSnackbar(it) }
            viewModel.onErrorShown()
        }
    }

    LaunchedEffect(state.isProjectCreated) {
        if (state.isProjectCreated) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { Text("Crear Nuevo Proyecto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        UploadProjectForm(
            modifier = Modifier.padding(padding),
            state = state,
            viewModel = viewModel,
            onImageSelect = { imagePickerLauncher.launch("image/*") },
            onDocumentSelect = { documentPickerLauncher.launch("*/*") }
        )
    }
}

@Composable
private fun UploadProjectForm(
    modifier: Modifier,
    state: CreateProjectState,
    viewModel: CreateProjectViewModel,
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
        
        ImagePicker(state.imageUri, onImageSelect)
        Spacer(Modifier.height(16.dp))
        DocumentPicker(state.documentUris, onDocumentSelect)
        
        Spacer(Modifier.height(32.dp))

        Button(
            onClick = viewModel::createProject,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if(state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                Text("Publicar Proyecto")
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
private fun ImagePicker(imageUri: Uri?, onImageSelect: () -> Unit) {
    OutlinedButton(onClick = onImageSelect, modifier = Modifier.fillMaxWidth()) {
        Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
        Text("Seleccionar Imagen de Portada")
    }
    if (imageUri != null) {
        AsyncImage(model = imageUri, contentDescription = "Imagen seleccionada", modifier = Modifier.fillMaxWidth().height(200.dp).padding(top = 8.dp).clip(RoundedCornerShape(12.dp)))
    }
}

@Composable
private fun DocumentPicker(documentUris: List<Uri>, onDocumentSelect: () -> Unit) {
    OutlinedButton(onClick = onDocumentSelect, modifier = Modifier.fillMaxWidth()) {
        Icon(Icons.Default.AttachFile, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
        Text("Adjuntar Documentos")
    }
    documentUris.forEach { uri ->
        Text("Archivo: ${uri.lastPathSegment}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 16.dp, top = 4.dp))
    }
}


@HiltViewModel
class CreateProjectViewModel @Inject constructor(
    private val createProjectUseCase: CreateProjectUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateProjectState())
    val uiState: StateFlow<CreateProjectState> = _uiState.asStateFlow()

    fun onTitleChange(title: String) { _uiState.value = _uiState.value.copy(title = title) }
    fun onDescriptionChange(description: String) { _uiState.value = _uiState.value.copy(description = description) }
    fun onCategoryChange(category: String) { _uiState.value = _uiState.value.copy(category = category) }
    fun onInstitutionChange(institution: String) { _uiState.value = _uiState.value.copy(institution = institution) }
    fun onFundingGoalChange(goal: String) { _uiState.value = _uiState.value.copy(fundingGoal = goal) }
    fun onImageSelected(uri: Uri?) { _uiState.value = _uiState.value.copy(imageUri = uri) }
    fun onDocumentsSelected(uris: List<Uri>) { _uiState.value = _uiState.value.copy(documentUris = uris) }

    fun createProject() {
        viewModelScope.launch {
            val state = _uiState.value
            val currentUser = getCurrentUserUseCase()

            if (currentUser == null) {
                _uiState.value = _uiState.value.copy(userMessage = "Error de autenticación.")
                return@launch
            }
            if (state.imageUri == null) {
                _uiState.value = _uiState.value.copy(userMessage = "Por favor, selecciona una imagen.")
                return@launch
            }
            if (state.title.isBlank() || state.description.isBlank() || state.fundingGoal.toDoubleOrNull() == null || state.category.isBlank()) {
                _uiState.value = _uiState.value.copy(userMessage = "Por favor, completa todos los campos.")
                return@launch
            }

            _uiState.value = state.copy(isLoading = true)

            val result = createProjectUseCase(
                title = state.title,
                description = state.description,
                category = state.category,
                institution = state.institution,
                fundingGoal = state.fundingGoal.toDouble(),
                ownerUid = currentUser.uid,
                imageUri = state.imageUri!!,
                documentUris = state.documentUris
            )

            result.onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false, isProjectCreated = true)
            }.onFailure {
                _uiState.value = _uiState.value.copy(isLoading = false, userMessage = it.message ?: "Error desconocido")
            }
        }
    }

    fun onErrorShown() {
        _uiState.value = _uiState.value.copy(userMessage = null)
    }
}

data class CreateProjectState(
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val institution: String = "",
    val fundingGoal: String = "",
    val imageUri: Uri? = null,
    val documentUris: List<Uri> = emptyList(),
    val isLoading: Boolean = false,
    val isProjectCreated: Boolean = false,
    val userMessage: String? = null
)

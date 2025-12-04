package com.hugoguerrero.tecno.ui.screens.complaint

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.hugoguerrero.tecno.data.model.Complaint
import com.hugoguerrero.tecno.domain.use_case.CreateComplaintUseCase
import com.hugoguerrero.tecno.domain.use_case.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplaintsScreen(
    navController: NavController,
    viewModel: ComplaintViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            viewModel.userMessageShown()
        }
    }

    LaunchedEffect(state.complaintSent) {
        if (state.complaintSent) {
            navController.popBackStack()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Reportar un Problema") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        ComplaintForm(
            modifier = Modifier.padding(padding),
            state = state,
            viewModel = viewModel
        )
    }
}

@Composable
private fun ComplaintForm(modifier: Modifier, state: ComplaintState, viewModel: ComplaintViewModel) {
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("¿Qué problema has encontrado?", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = state.reason,
                    onValueChange = viewModel::onReasonChange,
                    label = { Text("Motivo de la queja") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = state.description,
                    onValueChange = viewModel::onDescriptionChange,
                    label = { Text("Describe el problema en detalle") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 5
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { viewModel.submitComplaint() },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                Text("Enviar Queja")
            }
        }
    }
}

@HiltViewModel
class ComplaintViewModel @Inject constructor(
    private val createComplaintUseCase: CreateComplaintUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ComplaintState())
    val uiState = _uiState.asStateFlow()

    private val projectId: String? = savedStateHandle.get<String>("projectId")

    fun onReasonChange(reason: String) { _uiState.value = _uiState.value.copy(reason = reason) }
    fun onDescriptionChange(description: String) { _uiState.value = _uiState.value.copy(description = description) }

    fun submitComplaint() {
        viewModelScope.launch {
            val state = _uiState.value
            val currentUser = getCurrentUserUseCase()

            if (currentUser == null) {
                _uiState.value = state.copy(userMessage = "Error de autenticación.")
                return@launch
            }
            if (state.reason.isBlank() || state.description.isBlank()) {
                _uiState.value = state.copy(userMessage = "Por favor, completa todos los campos.")
                return@launch
            }

            _uiState.value = state.copy(isLoading = true)

            val complaint = Complaint(
                projectId = projectId ?: "N/A",
                reporterUid = currentUser.uid,
                reason = state.reason,
                description = state.description
            )

            val result = createComplaintUseCase(complaint)

            result.onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false, complaintSent = true, userMessage = "Queja enviada con éxito.")
            }.onFailure {
                _uiState.value = _uiState.value.copy(isLoading = false, userMessage = it.message ?: "Error desconocido")
            }
        }
    }
    
    fun userMessageShown() {
        _uiState.value = _uiState.value.copy(userMessage = null)
    }
}

data class ComplaintState(
    val reason: String = "",
    val description: String = "",
    val isLoading: Boolean = false,
    val complaintSent: Boolean = false,
    val userMessage: String? = null
)

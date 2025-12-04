package com.hugoguerrero.tecno.ui.screens.donation

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.storage.FirebaseStorage
import com.hugoguerrero.tecno.data.model.DonationStatus
import com.hugoguerrero.tecno.domain.use_case.CreateDonationUseCase
import com.hugoguerrero.tecno.domain.use_case.GetCurrentUserUseCase
import com.hugoguerrero.tecno.domain.use_case.GetProjectUseCase
import com.hugoguerrero.tecno.domain.use_case.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationScreen(
    navController: NavController,
    viewModel: DonationViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> viewModel.onProofUriChange(uri) }
    )

    LaunchedEffect(key1 = state.userMessage) {
        state.userMessage?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            viewModel.userMessageShown()
        }
    }

    LaunchedEffect(key1 = state.donationCompleted) {
        if (state.donationCompleted) {
            navController.popBackStack()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Apoyar Proyecto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        DonationForm(
            modifier = Modifier.padding(padding),
            state = state,
            viewModel = viewModel,
            onSelectProof = { imagePickerLauncher.launch("image/*") }
        )
    }
}

@Composable
private fun DonationForm(
    modifier: Modifier,
    state: DonationState,
    viewModel: DonationViewModel,
    onSelectProof: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (state.isLoadingProject) {
            CircularProgressIndicator()
        } else {
            OutlinedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Datos para la transferencia", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    Text(state.paymentDetails, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                }
            }
            
            OutlinedCard(Modifier.fillMaxWidth()) {
                 Column(Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = state.amount,
                        onValueChange = viewModel::onAmountChange,
                        label = { Text("Monto Donado") },
                        prefix = { Text("$") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.message,
                        onValueChange = viewModel::onMessageChange,
                        label = { Text("Mensaje (opcional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                 }
            }

            OutlinedButton(onClick = onSelectProof) {
                Text(if (state.proofUri != null) "Comprobante seleccionado" else "Seleccionar Comprobante")
            }

            Button(
                onClick = { viewModel.sendDonationWithProof() },
                enabled = !state.isDonating && (state.amount.toDoubleOrNull() ?: 0.0) > 0 && state.proofUri != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isDonating) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                    Text("Confirmar Donación")
                }
            }
        }
    }
}

@HiltViewModel
class DonationViewModel @Inject constructor(
    private val createDonationUseCase: CreateDonationUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getProjectUseCase: GetProjectUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val storage: FirebaseStorage,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(DonationState())
    val uiState = _uiState.asStateFlow()

    private val projectId: String = savedStateHandle.get<String>("projectId")!!

    init {
        loadProjectCreatorDetails()
    }

    private fun loadProjectCreatorDetails() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingProject = true)
            try {
                val project = getProjectUseCase(projectId)
                if (project == null) throw Exception("Proyecto no encontrado.")

                val creator = getUserUseCase(project.ownerUid)
                if (creator == null) throw Exception("Creador del proyecto no encontrado.")

                _uiState.value = _uiState.value.copy(
                    paymentDetails = creator.paymentDetails ?: "El creador no ha proporcionado datos de pago.",
                    isLoadingProject = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(userMessage = e.message ?: "Error al cargar la información.", isLoadingProject = false)
            }
        }
    }

    fun onAmountChange(amount: String) {
        _uiState.value = _uiState.value.copy(amount = amount.filter { it.isDigit() || it == '.' })
    }

    fun onMessageChange(message: String) {
        _uiState.value = _uiState.value.copy(message = message)
    }

    fun onProofUriChange(uri: Uri?) {
        _uiState.value = _uiState.value.copy(proofUri = uri)
    }

    fun sendDonationWithProof() {
        viewModelScope.launch {
            val state = _uiState.value
            val donor = getCurrentUserUseCase()
            val amount = state.amount.toDoubleOrNull()

            if (donor == null) {
                _uiState.value = state.copy(userMessage = "Error de autenticación.")
                return@launch
            }
            if (amount == null || amount <= 0) {
                _uiState.value = state.copy(userMessage = "Por favor, introduce un monto válido.")
                return@launch
            }
            if (state.proofUri == null) {
                _uiState.value = state.copy(userMessage = "Por favor, selecciona un comprobante.")
                return@launch
            }

            _uiState.value = state.copy(isDonating = true, userMessage = null)

            try {
                val proofFileName = "${UUID.randomUUID()}"
                val proofRef = storage.reference.child("donation_proofs/$proofFileName")
                val uploadTask = proofRef.putFile(state.proofUri).await()
                val proofUrl = uploadTask.storage.downloadUrl.await().toString()

                val result = createDonationUseCase(
                    projectId = projectId,
                    donorUid = donor.uid,
                    amount = amount,
                    message = state.message,
                    status = DonationStatus.PENDING,
                    proofImageUrl = proofUrl
                )

                result.onSuccess {
                    _uiState.value = _uiState.value.copy(isDonating = false, donationCompleted = true, userMessage = "¡Gracias por tu donación!")
                }.onFailure { throw it }

            } catch (e: Exception) {
                _uiState.value = state.copy(isDonating = false, userMessage = e.message ?: "Error al enviar la donación.")
            }
        }
    }
    
    fun userMessageShown() {
        _uiState.value = _uiState.value.copy(userMessage = null)
    }
}

data class DonationState(
    val isLoadingProject: Boolean = true,
    val isDonating: Boolean = false,
    val donationCompleted: Boolean = false,
    val paymentDetails: String = "",
    val amount: String = "",
    val message: String = "",
    val proofUri: Uri? = null,
    val userMessage: String? = null
)

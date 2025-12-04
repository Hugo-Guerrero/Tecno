package com.hugoguerrero.tecno.ui.screens.management

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.hugoguerrero.tecno.data.model.Donation
import com.hugoguerrero.tecno.data.model.DonationStatus
import com.hugoguerrero.tecno.domain.use_case.GetDonationsForProjectUseCase
import com.hugoguerrero.tecno.domain.use_case.UpdateDonationStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageDonationsScreen(
    navController: NavController,
    viewModel: ManageDonationsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestionar Donaciones") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        ManageDonationsContent(Modifier.padding(padding), state, viewModel)
    }
}

@Composable
private fun ManageDonationsContent(modifier: Modifier, state: ManageDonationsState, viewModel: ManageDonationsViewModel) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            state.error != null -> {
                Text(text = state.error, modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.error)
            }
            state.donations.isEmpty() -> {
                EmptyState()
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.donations, key = { it.id }) { donation ->
                        DonationManagementItem(
                            donation = donation,
                            isUpdating = state.updatingDonationId == donation.id,
                            onConfirm = { viewModel.updateStatus(donation, DonationStatus.COMPLETED) },
                            onReject = { viewModel.updateStatus(donation, DonationStatus.FAILED) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.surfaceVariant)
            Text(
                text = "Este proyecto aún no ha recibido donaciones para gestionar.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun DonationManagementItem(donation: Donation, isUpdating: Boolean, onConfirm: () -> Unit, onReject: () -> Unit) {
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        DonationItemHeader(donation)
        if (donation.proofImageUrl.isNotEmpty()) {
            DonationProofImage(donation.proofImageUrl)
        }
        DonationItemFooter(donation, isUpdating, onConfirm, onReject)
    }
}

@Composable
fun DonationItemHeader(donation: Donation) {
    val formattedDate = donation.createdAt?.let {
        SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(it)
    } ?: "Fecha desconocida"
    Column(Modifier.padding(16.dp)) {
        Text(
            text = "$${donation.amount}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = formattedDate,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (donation.message.isNotBlank()) {
            Text(
                text = "\"${donation.message}\"",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun DonationProofImage(imageUrl: String) {
    val context = LocalContext.current
    AsyncImage(
        model = imageUrl,
        contentDescription = "Comprobante de pago",
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl))
                context.startActivity(intent)
            },
        contentScale = ContentScale.Crop
    )
}

@Composable
fun DonationItemFooter(donation: Donation, isUpdating: Boolean, onConfirm: () -> Unit, onReject: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DonationStatusBadge(status = donation.status)

        if (donation.status == DonationStatus.PENDING) {
            if (isUpdating) {
                CircularProgressIndicator(modifier = Modifier.size(36.dp))
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = onReject) {
                        Text("Rechazar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onConfirm) {
                        Text("Aprobar")
                    }
                }
            }
        }
    }
}


@Composable
fun DonationStatusBadge(status: DonationStatus) {
    val (text, color, icon) = when (status) {
        DonationStatus.PENDING -> Triple("Pendiente", MaterialTheme.colorScheme.secondary, Icons.Default.HourglassEmpty)
        DonationStatus.COMPLETED -> Triple("Aprobada", Color(0xFF388E3C), Icons.Default.CheckCircle)
        DonationStatus.FAILED -> Triple("Rechazada", MaterialTheme.colorScheme.error, Icons.Default.Cancel)
    }

    Surface(
        shape = MaterialTheme.shapes.small,
        color = color.copy(alpha = 0.1f),
        contentColor = color,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
            Text(text = text, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        }
    }
}


@HiltViewModel
class ManageDonationsViewModel @Inject constructor(
    private val getDonationsForProjectUseCase: GetDonationsForProjectUseCase,
    private val updateDonationStatusUseCase: UpdateDonationStatusUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<ManageDonationsState> = MutableStateFlow(ManageDonationsState(isLoading = true))
    val uiState: StateFlow<ManageDonationsState> = _uiState.asStateFlow()

    private val projectId: String = savedStateHandle.get<String>("projectId")!!

    init {
        observeDonations()
    }

    private fun observeDonations() {
        getDonationsForProjectUseCase(projectId)
            .onEach { donations ->
                _uiState.value = _uiState.value.copy(
                    donations = donations, 
                    isLoading = false
                )
            }
            .catch { e ->
                _uiState.value = _uiState.value.copy(
                    error = e.message, 
                    isLoading = false
                )
            }
            .launchIn(viewModelScope)
    }

    fun updateStatus(donation: Donation, newStatus: DonationStatus) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(updatingDonationId = donation.id)
            val result = updateDonationStatusUseCase(donation.id, donation.projectId, donation.amount, newStatus)
            result.onFailure {
                _uiState.value = _uiState.value.copy(error = it.message)
            }
             // El Flow se encarga de quitar el ítem de la lista, 
             // solo necesitamos quitar el indicador de carga.
            _uiState.value = _uiState.value.copy(updatingDonationId = null)
        }
    }
}

data class ManageDonationsState(
    val isLoading: Boolean = false,
    val donations: List<Donation> = emptyList(),
    val updatingDonationId: String? = null,
    val error: String? = null
)

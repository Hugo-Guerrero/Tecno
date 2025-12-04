package com.hugoguerrero.tecno.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> viewModel.onPhotoUriChange(uri) }
    )

    LaunchedEffect(key1 = viewModel) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is EditProfileNavigation.NavigateBack -> navController.popBackStack()
            }
        }
    }

    state.userMessage?.let { message ->
        LaunchedEffect(message) {
            scope.launch { snackbarHostState.showSnackbar(message) }
            viewModel.userMessageShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        EditProfileContent(
            modifier = Modifier.padding(padding),
            state = state,
            onDisplayNameChange = viewModel::onDisplayNameChange,
            onBioChange = viewModel::onBioChange,
            onPaymentDetailsChange = viewModel::onPaymentDetailsChange,
            onImagePickerClick = { imagePickerLauncher.launch("image/*") },
            onSaveClick = viewModel::saveProfile
        )
    }
}

@Composable
private fun EditProfileContent(
    modifier: Modifier,
    state: EditProfileState,
    onDisplayNameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onPaymentDetailsChange: (String) -> Unit,
    onImagePickerClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    if (state.isInitialLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileImagePicker(state.photoUri, state.initialPhotoUrl, onImagePickerClick)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = state.displayName,
                        onValueChange = onDisplayNameChange,
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = state.bio,
                        onValueChange = onBioChange,
                        label = { Text("Biografía") },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        maxLines = 5
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = state.paymentDetails,
                        onValueChange = onPaymentDetailsChange,
                        label = { Text("Datos de Pago (CLABE, CBU, etc.)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Guardar Cambios")
                }
            }
        }
    }
}

@Composable
private fun ProfileImagePicker(selectedUri: Uri?, initialUrl: String?, onClick: () -> Unit) {
    Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.clickable(onClick = onClick)) {
        AsyncImage(
            model = selectedUri ?: initialUrl,
            contentDescription = "Foto de perfil",
            modifier = Modifier.size(140.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant),
            contentScale = ContentScale.Crop
        )
        Icon(
            imageVector = Icons.Default.PhotoCamera,
            contentDescription = "Cambiar foto",
            modifier = Modifier.background(MaterialTheme.colorScheme.primary, CircleShape).padding(8.dp),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

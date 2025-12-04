package com.hugoguerrero.tecno.ui.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.hugoguerrero.tecno.domain.model.UserType
import com.hugoguerrero.tecno.domain.use_case.RegisterUserUseCase
import com.hugoguerrero.tecno.ui.navigation.MAIN_GRAPH_ROUTE
import com.hugoguerrero.tecno.ui.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class NavigationEvent {
    object NavigateToMain : NavigationEvent()
    object NavigateToLogin : NavigationEvent()
}

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is NavigationEvent.NavigateToMain -> {
                    navController.navigate(MAIN_GRAPH_ROUTE) {
                        popUpTo(Screens.Login.route) { inclusive = true }
                    }
                }
                is NavigationEvent.NavigateToLogin -> {
                    navController.popBackStack()
                }
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            RegisterForm(state = state, viewModel = viewModel)
        }
    }
}

@Composable
private fun RegisterForm(state: RegisterState, viewModel: RegisterViewModel) {
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Crear Cuenta", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Únete a la comunidad científica", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(16.dp))

            state.error?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
            }

            UserTypeSelection(selectedType = state.userType, onTypeSelected = viewModel::onUserTypeChange)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                label = { Text("Confirmar contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { viewModel.register() },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Registrarse")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { viewModel.navigateToLogin() }) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserTypeSelection(selectedType: UserType, onTypeSelected: (UserType) -> Unit) {
    val userTypes = UserType.values()
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        userTypes.forEachIndexed { index, userType ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = userTypes.size),
                onClick = { onTypeSelected(userType) },
                selected = userType == selectedType,
                icon = { 
                    Icon(
                        imageVector = Icons.Default.Person, 
                        contentDescription = null, 
                        modifier = Modifier.size(SegmentedButtonDefaults.IconSize)
                    )
                }
            ) {
                Text(userType.name.lowercase().replaceFirstChar { it.titlecase() })
            }
        }
    }
}

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onNameChange(name: String) { _uiState.value = _uiState.value.copy(name = name) }
    fun onEmailChange(email: String) { _uiState.value = _uiState.value.copy(email = email) }
    fun onPasswordChange(password: String) { _uiState.value = _uiState.value.copy(password = password) }
    fun onConfirmPasswordChange(password: String) { _uiState.value = _uiState.value.copy(confirmPassword = password) }
    fun onUserTypeChange(userType: UserType) { _uiState.value = _uiState.value.copy(userType = userType) }

    fun register() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.password != state.confirmPassword) {
                _uiState.value = _uiState.value.copy(error = "Las contraseñas no coinciden.")
                return@launch
            }

            _uiState.value = state.copy(isLoading = true, error = null)

            registerUserUseCase(state.name, state.email, state.password, state.userType)
                .collect { result ->
                    result.onSuccess {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        viewModelScope.launch { _navigationEvent.emit(NavigationEvent.NavigateToMain) }
                    }.onFailure {
                        _uiState.value = _uiState.value.copy(isLoading = false, error = it.message ?: "Error desconocido")
                    }
                }
        }
    }

    fun navigateToLogin() {
        viewModelScope.launch { _navigationEvent.emit(NavigationEvent.NavigateToLogin) }
    }
}

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val userType: UserType = UserType.STUDENT,
    val isLoading: Boolean = false,
    val error: String? = null
)

package com.hugoguerrero.tecno.ui.screens.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.hugoguerrero.tecno.R
import com.hugoguerrero.tecno.domain.model.UserType
import com.hugoguerrero.tecno.domain.use_case.*
import com.hugoguerrero.tecno.ui.navigation.AUTH_GRAPH_ROUTE
import com.hugoguerrero.tecno.ui.navigation.MAIN_GRAPH_ROUTE
import com.hugoguerrero.tecno.ui.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val credential = Identity.getSignInClient(context).getSignInCredentialFromIntent(result.data)
                    val googleIdToken = credential.googleIdToken
                    if (googleIdToken != null) {
                        val googleCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                        viewModel.signInWithGoogle(googleCredential)
                    }
                } catch (e: ApiException) {
                    viewModel.showError(e.message ?: "Error de Google Sign-In")
                }
            }
        }
    )

    LaunchedEffect(key1 = viewModel) {
        viewModel.navigationEvent.collect { navigation ->
            when (navigation) {
                is LoginNavigation.MainScreen -> {
                    navController.navigate(MAIN_GRAPH_ROUTE) { popUpTo(AUTH_GRAPH_ROUTE) { inclusive = true } }
                }
                is LoginNavigation.RegisterScreen -> {
                    navController.navigate(Screens.Register.route)
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.checkAuthState()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AppLogo()
            Spacer(modifier = Modifier.height(24.dp))
            LoginForm(state = state, viewModel = viewModel, navController = navController)
            Spacer(modifier = Modifier.height(24.dp))
            SocialLoginButtons(state = state, onGoogleSignInClick = { 
                viewModel.startGoogleSignIn { intentSenderRequest -> googleSignInLauncher.launch(intentSenderRequest) } 
            })
        }
    }
}

@Composable
private fun AppLogo() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Tecno-Ciencia",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun LoginForm(state: LoginState, viewModel: LoginViewModel, navController: NavController) {
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Iniciar Sesión", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            state.error?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
            }

            OutlinedTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
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
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.signInWithEmail() },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Entrar")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { navController.navigate(Screens.Register.route) }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}

@Composable
private fun SocialLoginButtons(state: LoginState, onGoogleSignInClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("O continúa con", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = onGoogleSignInClick,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.google_logo),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Text("Google", modifier = Modifier.padding(start = 12.dp))
        }
    }
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val checkUserProfileExistsUseCase: CheckUserProfileExistsUseCase,
    private val createUserProfileUseCase: CreateUserProfileUseCase,
    private val application: android.app.Application
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<LoginNavigation>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onEmailChange(email: String) { _uiState.value = _uiState.value.copy(email = email) }
    fun onPasswordChange(password: String) { _uiState.value = _uiState.value.copy(password = password) }

    fun signInWithEmail() {
        viewModelScope.launch {
            val state = _uiState.value
            _uiState.value = state.copy(isLoading = true, error = null)
            signInWithEmailUseCase(state.email, state.password).collect { result ->
                result.onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _navigationEvent.emit(LoginNavigation.MainScreen)
                }.onFailure {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = it.message)
                }
            }
        }
    }
    
    fun startGoogleSignIn(launcher: (IntentSenderRequest) -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(application.getString(R.string.web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build()
                )
                .build()

            try {
                val result = Identity.getSignInClient(application).beginSignIn(signInRequest).await()
                launcher(IntentSenderRequest.Builder(result.pendingIntent.intentSender).build())
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun signInWithGoogle(credential: AuthCredential) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            signInWithGoogleUseCase(credential).collect { result ->
                result.onSuccess { user ->
                    if (!checkUserProfileExistsUseCase(user.uid)) {
                        createUserProfileUseCase(user, user.displayName ?: "Usuario", UserType.STUDENT)
                    }
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _navigationEvent.emit(LoginNavigation.MainScreen)
                }.onFailure { exception ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = exception.message)
                }
            }
        }
    }

    fun checkAuthState() {
        viewModelScope.launch {
            if (getCurrentUserUseCase() != null) {
                _navigationEvent.emit(LoginNavigation.MainScreen)
            }
        }
    }

    fun showError(message: String) {
        _uiState.value = _uiState.value.copy(error = message)
    }
}

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class LoginNavigation {
    object MainScreen : LoginNavigation()
    object RegisterScreen : LoginNavigation()
}

package com.hugoguerrero.tecno.ui.screens.profile

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.hugoguerrero.tecno.data.model.Project
import com.hugoguerrero.tecno.data.model.UserProfile
import com.hugoguerrero.tecno.ui.components.BottomNavigationBar
import com.hugoguerrero.tecno.ui.navigation.AUTH_GRAPH_ROUTE
import com.hugoguerrero.tecno.ui.navigation.Screens
import com.hugoguerrero.tecno.ui.screens.login.NavigationEvent
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    navController: NavController,
    viewModel: MyProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is NavigationEvent.NavigateToLogin -> {
                    navController.navigate(AUTH_GRAPH_ROUTE) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                actions = {
                    IconButton(onClick = { navController.navigate(Screens.EditProfile.route) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar Perfil")
                    }
                    IconButton(onClick = { viewModel.signOut() }) {
                        Icon(Icons.Default.Logout, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        MyProfileContent(modifier = Modifier.padding(padding), state = state, navController = navController, viewModel = viewModel)
    }
}

@Composable
private fun MyProfileContent(modifier: Modifier, state: MyProfileState, navController: NavController, viewModel: MyProfileViewModel) {
    Box(modifier = modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (state.error != null) {
            Text(text = state.error, modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.error)
        } else if (state.profile != null) {
            LazyColumn(contentPadding = PaddingValues(vertical = 16.dp)) {
                item {
                    ProfileHeader(profile = state.profile)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Mis Proyectos", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(horizontal = 16.dp))
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                }
                if (state.projects.isEmpty()) {
                    item {
                        Text(
                            "Aún no has creado ningún proyecto.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(state.projects) { project ->
                        ProjectItem(
                            project = project,
                            onUpdate = { navController.navigate(Screens.EditProject.route + "/${project.id}") },
                            onDelete = { viewModel.onDeleteProjectClicked(project) },
                            onManage = { navController.navigate(Screens.ManageDonations.route + "/${project.id}") }
                        )
                    }
                }
            }
        }

        if (state.projectToDelete != null) {
            DeleteConfirmationDialog(state, viewModel)
        }
    }
}

@Composable
private fun ProfileHeader(profile: UserProfile) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        AsyncImage(
            model = profile.photoUrl.ifEmpty { "https://via.placeholder.com/150" },
            contentDescription = "Foto de perfil",
            modifier = Modifier.size(120.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(profile.displayName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(profile.email, style = MaterialTheme.typography.bodyMedium)
        profile.createdAt?.let {
            val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(it)
            Text("Miembro desde: $formattedDate", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ProjectItem(project: Project, onUpdate: () -> Unit, onDelete: () -> Unit, onManage: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Column {
            AsyncImage(
                model = project.imageUrl,
                contentDescription = "Imagen de ${project.title}",
                modifier = Modifier.fillMaxWidth().height(150.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(project.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Meta: $${project.fundingGoal.toInt()} - Recaudado: $${project.currentFunding.toInt()}", style = MaterialTheme.typography.bodySmall)
                LinearProgressIndicator(progress = project.progress / 100f, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
            }
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onManage) {
                    Text("Gestionar")
                }
                IconButton(onClick = onUpdate) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar Proyecto")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar Proyecto")
                }
            }
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(state: MyProfileState, viewModel: MyProfileViewModel) {
    AlertDialog(
        onDismissRequest = { viewModel.cancelProjectDeletion() },
        title = { Text("Confirmar Eliminación") },
        text = { Text("¿Estás seguro de que quieres eliminar el proyecto '${state.projectToDelete?.title}'? Esta acción no se puede deshacer.") },
        confirmButton = {
            Button(
                onClick = { viewModel.confirmProjectDeletion() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            Button(onClick = { viewModel.cancelProjectDeletion() }) {
                Text("Cancelar")
            }
        }
    )
}

package com.hugoguerrero.tecno.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Créditos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text("Una aplicación desarrollada por:", style = MaterialTheme.typography.titleLarge)
            }
            item {
                CreditProfile(
                    name = "Hugo Emiliano Guerrero Campos",
                    bio = "Desarrollador de la aplicación Tecno-Ciencia, apasionado por la tecnología y la innovación. Estudiante de Ingeniería en Desarrollo de Software Multiplataforma.",
                    photoUrl = "https://firebasestorage.googleapis.com/v0/b/tecno-ea191.firebasestorage.app/o/project_credits%2Fhugo.png?alt=media&token=b4f4d585-6a49-45c0-9cdc-4068832833fe"
                )
            }
            item {
                CreditProfile(
                    name = "Jorge Alberto Carranco Ramírez",
                    bio = "Colaborador y tester de la aplicación. Estudiante de Ingeniero en Desarrollo de Software Multiplataforma.",
                    photoUrl = "https://firebasestorage.googleapis.com/v0/b/tecno-ea191.firebasestorage.app/o/project_credits%2Fjorge.jpg?alt=media&token=b0c82b42-c5ee-4234-b8a2-bc2d81b9a1c8"
                )
            }
        }
    }
}

@Composable
private fun CreditProfile(name: String, bio: String, photoUrl: String) {
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = photoUrl,
                contentDescription = "Foto de $name",
                modifier = Modifier.size(120.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
            Text(bio, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        }
    }
}

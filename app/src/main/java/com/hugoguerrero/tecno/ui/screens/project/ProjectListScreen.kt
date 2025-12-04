package com.hugoguerrero.tecno.ui.screens.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hugoguerrero.tecno.data.model.Project

@Composable
fun ProjectListScreen(
    projects: List<Project>,
    onProjectClick: (String) -> Unit,
    onCreateProject: () -> Unit,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A))
            .padding(16.dp)
    ) {
        // Título y descripción
        Text(
            text = "Descubre proyectos",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Apoya iniciativas o conviértete en manager",
            color = Color(0xFFB0BEC5),
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Lista de proyectos
        if (projects.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay proyectos disponibles",
                    color = Color(0xFFB0BEC5)
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(projects) { project ->
                    ProjectCard(
                        project = project,
                        onProjectClick = onProjectClick
                    )
                }
            }
        }
    }
}

@Composable
fun ProjectCard(
    project: Project,
    onProjectClick: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProjectClick(project.id) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = project.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = project.description,
                color = Color(0xFFB0BEC5),
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

        }
    }
}

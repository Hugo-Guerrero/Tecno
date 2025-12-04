package com.hugoguerrero.tecno.ui.screens.project

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCreditos() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Créditos",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* sin funcionalidad */ }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_media_previous),
                            contentDescription = "Atrás",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0D1117)
                )
            )
        },
        containerColor = Color(0xFF0D1117)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Cabecera
            Column {
                Text(
                    "Tecno-Ciencia",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    "Conectando estudiantes, managers y patrocinadores",
                    color = Color(0xFF9DA8B8),
                    fontSize = 13.sp
                )
            }

            // Equipo del proyecto
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Equipo del proyecto",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Hugo Guerrero — Producto y dirección • Líder", color = Color(0xFFB4B4B4))
                    Text("Jorge — Diseño UI/UX • Diseño", color = Color(0xFFB4B4B4))
                    Text("Natalia Estefania — iOS & Android • Mobile", color = Color(0xFFB4B4B4))
                    Text("Rafa — Backend & API • Ingeniería", color = Color(0xFFB4B4B4))
                }
            }

            //  Colaboradores
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Colaboradores",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Alex — QA y testing • QA", color = Color(0xFFB4B4B4))
                    Text("Emx — Comunidad • Comunidad", color = Color(0xFFB4B4B4))
                }
            }

            // Agradecimientos especiales
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Agradecimientos especiales",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Gracias a nuestra comunidad de estudiantes, managers y patrocinadores por su apoyo y retroalimentación constante.",
                        color = Color(0xFF9DA8B8),
                        fontSize = 13.sp
                    )
                }
            }

            // Tecnologías y recursos
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161B22)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Tecnologías y recursos",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Arquitectura: REST API, Realtime", color = Color(0xFFB4B4B4))
                    Text("• Mobile: Swift, Kotlin, React Native", color = Color(0xFFB4B4B4))
                    Text("• Backend: Node.js, PostgreSQL", color = Color(0xFFB4B4B4))
                    Text("• Diseño: Figma, Lucide Icons", color = Color(0xFFB4B4B4))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Esta app incluye logotipos e imágenes que pertenecen a sus respectivos propietarios y se utilizan únicamente con fines de referencia.",
                        color = Color(0xFF7C8592),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pie de página
            Text(
                "© 2025 Tecno-Ciencia. Todos los derechos reservados.",
                color = Color(0xFF6E7681),
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}
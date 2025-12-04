package com.hugoguerrero.tecno.ui.screens.donation

import android.R
import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationHistoryScreen(
    onBack: () -> Unit
) {
    // Datos de ejemplo para el historial
    val donationHistory = listOf(
        Donacion("Robótica para Ferias Científicas", "2023-10-15", 500.0),
        Donacion("BioSensor de Bajo Costo", "2023-09-20", 300.0),
        Donacion("Micro-red Solar", "2023-08-05", 1000.0)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Historial de Donaciones",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_media_previous),
                            contentDescription = "Atrás",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF35002E))
            )
        },
        containerColor = Color(0xFF0F0A13)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                "Tus donaciones",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(donationHistory) { donacion ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A0D26)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                donacion.proyecto,
                                color = Color(0xFFF1E6FF),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    donacion.fecha,
                                    color = Color(0xFFB497E5),
                                    fontSize = 14.sp
                                )
                                Text(
                                    "$${donacion.monto} MXN",
                                    color = Color(0xFF00BFA6),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class Donacion(
    val proyecto: String,
    val fecha: String,
    val monto: Double
)

@Preview(showBackground = true)
@Composable
fun PreviewDonationHistoryScreen() {
    DonationHistoryScreen(onBack = {})
}
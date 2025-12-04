package com.hugoguerrero.tecno.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val iconContentDescription: String
) {
    object Main : BottomNavItem(
        route = Screens.Main.route,
        title = "Proyectos",
        icon = Icons.Default.List,
        iconContentDescription = "Proyectos"
    )

    object Donation : BottomNavItem(
        route = Screens.Donation.route,
        title = "Donaciones",
        icon = Icons.Default.Favorite,
        iconContentDescription = "Donaciones"
    )

    object Profile : BottomNavItem(
        route = Screens.MyProfile.route, // Corregido aquí
        title = "Perfil",
        icon = Icons.Default.Person,
        iconContentDescription = "Perfil"
    )

    object Complaint : BottomNavItem(
        route = Screens.Complaint.route,
        title = "Soporte",
        icon = Icons.Default.Warning,
        iconContentDescription = "Soporte"
    )

    companion object {
        val items = listOf(Main, Donation, Profile, Complaint)

        // Función para encontrar el item por ruta
        fun fromRoute(route: String?): BottomNavItem? {
            return items.find { it.route == route }
        }
    }
}

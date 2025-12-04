package com.hugoguerrero.tecno.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hugoguerrero.tecno.ui.navigation.Screens

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        // Pestaña de Inicio
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = currentDestination?.hierarchy?.any { it.route == Screens.Main.route } == true,
            onClick = {
                navController.navigate(Screens.Main.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        // Pestaña de Crear Proyecto
        NavigationBarItem(
            icon = { Icon(Icons.Default.Add, contentDescription = "Crear Proyecto") },
            label = { Text("Crear") },
            selected = currentDestination?.hierarchy?.any { it.route == Screens.UploadProject.route } == true,
            onClick = {
                navController.navigate(Screens.UploadProject.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        // Pestaña de Perfil
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") },
            selected = currentDestination?.hierarchy?.any {
                it.route == Screens.MyProfile.route || it.route == Screens.PublicProfile.route + "/{userId}"
            } == true,
            onClick = {
                navController.navigate(Screens.MyProfile.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}

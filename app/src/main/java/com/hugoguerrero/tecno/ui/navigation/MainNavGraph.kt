package com.hugoguerrero.tecno.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.hugoguerrero.tecno.ui.screens.complaint.ComplaintsScreen
import com.hugoguerrero.tecno.ui.screens.donation.DonationScreen
import com.hugoguerrero.tecno.ui.screens.login.LoginScreen
import com.hugoguerrero.tecno.ui.screens.login.RegisterScreen
import com.hugoguerrero.tecno.ui.screens.main.MainScreen
import com.hugoguerrero.tecno.ui.screens.management.ManageDonationsScreen
import com.hugoguerrero.tecno.ui.screens.profile.CreditsScreen
import com.hugoguerrero.tecno.ui.screens.profile.EditProfileScreen
import com.hugoguerrero.tecno.ui.screens.profile.MyProfileScreen
import com.hugoguerrero.tecno.ui.screens.profile.PublicProfileScreen
import com.hugoguerrero.tecno.ui.screens.project.EditProjectScreen
import com.hugoguerrero.tecno.ui.screens.project.ProjectDetailScreen
import com.hugoguerrero.tecno.ui.screens.project.UploadProjectScreen


const val AUTH_GRAPH_ROUTE = "auth_graph"
const val MAIN_GRAPH_ROUTE = "main_graph"

@Composable
fun MainNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AUTH_GRAPH_ROUTE
    ) {
        // Grafo de Autenticación (Mundo Pre-Login)
        authGraph(navController)

        // Grafo Principal (Mundo Post-Login)
        mainGraph(navController)
    }
}

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        startDestination = Screens.Login.route,
        route = AUTH_GRAPH_ROUTE
    ) {
        composable(Screens.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screens.Register.route) {
            RegisterScreen(navController = navController)
        }
    }
}

fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation(
        startDestination = Screens.Main.route,
        route = MAIN_GRAPH_ROUTE
    ) {
        composable(Screens.Main.route) {
            MainScreen(navController = navController)
        }
        composable(Screens.UploadProject.route) {
            UploadProjectScreen(navController = navController)
        }
        composable(Screens.MyProfile.route) {
            MyProfileScreen(navController = navController)
        }
        composable(Screens.EditProfile.route) {
            EditProfileScreen(navController = navController)
        }
        composable(Screens.Credits.route) {
            CreditsScreen(navController = navController)
        }
        composable(
            route = Screens.EditProject.route + "/{projectId}",
            arguments = listOf(navArgument("projectId") { type = NavType.StringType })
        ) {
            EditProjectScreen(navController = navController)
        }
        composable(
            route = Screens.ManageDonations.route + "/{projectId}",
            arguments = listOf(navArgument("projectId") { type = NavType.StringType })
        ) {
            ManageDonationsScreen(navController = navController)
        }
        composable(
            route = Screens.PublicProfile.route + "/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            PublicProfileScreen(navController = navController)
        }
        composable(
            route = Screens.ProjectDetail.route + "/{projectId}",
            arguments = listOf(navArgument("projectId") { type = NavType.StringType })
        ) {
            ProjectDetailScreen(navController = navController)
        }
        composable(
            route = Screens.Donation.route + "/{projectId}",
            arguments = listOf(navArgument("projectId") { type = NavType.StringType })
        ) {
            DonationScreen(navController = navController)
        }
        composable(
            route = Screens.Complaint.route + "/{projectId}",
            arguments = listOf(navArgument("projectId") { type = NavType.StringType; nullable = true })
        ) {
            ComplaintsScreen(navController = navController)
        }
    }
}

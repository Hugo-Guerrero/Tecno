package com.hugoguerrero.tecno.ui.navigation

sealed class Screens(val route: String) {
    object Login : Screens("login")
    object Register : Screens("register")
    object Main : Screens("main")
    object UploadProject : Screens("upload_project")
    object MyProfile : Screens("my_profile")
    object PublicProfile : Screens("public_profile")
    object EditProfile : Screens("edit_profile")
    object Credits : Screens("credits") // Nueva ruta para los créditos
    object ProjectDetail : Screens("project_detail")
    object Donation : Screens("donation")
    object Complaint : Screens("complaint")
    object EditProject : Screens("edit_project")
    object ManageDonations : Screens("manage_donations")
}

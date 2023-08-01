package com.example.photoframesbaseapp.navigation

sealed class Screen(val route: String) {
    object Home : Screen(route = "home")
    object Albums : Screen(route = "albums")
    object MoreApps : Screen(route = "moreapps")
    object FramesList : Screen(route = "frameslist")
    object Crop : Screen(route = "crop")
    object Edit : Screen(route = "edit")
    object Share : Screen(route = "share")
}
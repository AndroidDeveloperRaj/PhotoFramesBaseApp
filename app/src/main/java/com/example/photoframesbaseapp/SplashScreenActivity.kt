package com.example.photoframesbaseapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.photoframesbaseapp.albums.AlbumsScreen
import com.example.photoframesbaseapp.crop.CropScreen
import com.example.photoframesbaseapp.edit.EditScreen
import com.example.photoframesbaseapp.frames.FramesListScreen
import com.example.photoframesbaseapp.home.HomeScreenWithTabs
import com.example.photoframesbaseapp.moreapps.MoreAppsScreen
import com.example.photoframesbaseapp.navigation.Screen
import com.example.photoframesbaseapp.share.ShareScreen
import com.example.photoframesbaseapp.splash.SplashViewModel
import com.example.photoframesbaseapp.ui.theme.PhotoFramesBaseAppTheme
import com.google.accompanist.pager.ExperimentalPagerApi

class SplashScreenActivity : ComponentActivity() {

    private val viewModel: SplashViewModel by viewModels()

    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { viewModel.isLoading.value }

        setContent {
            // create a nav host controller
            val navController = rememberNavController()

            PhotoFramesBaseAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = Screen.Crop.route) {
                        composable(Screen.Home.route) {
                            HomeScreenWithTabs(
                                navController
                            )
                        }
                        composable(Screen.MoreApps.route) {
                            MoreAppsScreen(
                                navController
                            )
                        }
                        composable(Screen.Albums.route) {
                            AlbumsScreen(
                                navController
                            )
                        }
                        composable(Screen.FramesList.route) {
                            FramesListScreen(
                                navController
                            )
                        }
                        composable(Screen.Crop.route) {
                            CropScreen(
                                navController
                            )
                        }
                        composable(Screen.Edit.route) {
                            EditScreen(
                                navController
                            )
                        }
                        composable(Screen.Share.route) {
                            ShareScreen(
                                navController
                            )
                        }
                    }
                }
            }
        }
    }
}
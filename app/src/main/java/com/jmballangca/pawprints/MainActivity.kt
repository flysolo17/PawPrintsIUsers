package com.jmballangca.pawprints

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jmballangca.pawprints.router.AppRouter
import com.jmballangca.pawprints.router.MainNavGraph
import com.jmballangca.pawprints.router.authNavGraph
import com.jmballangca.pawprints.screens.main.main.MainScreen
import com.jmballangca.pawprints.screens.main.main.MainViewModel
import com.jmballangca.pawprints.screens.starter.StarterScreen
import com.jmballangca.pawprints.ui.theme.PawPrintsTheme
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PawPrintsTheme {
                val windowSize = calculateWindowSizeClass(activity = this)
                PawPrintApp(windowSizeClass = windowSize)
            }
        }
    }
}

@Composable
fun PawPrintApp(windowSizeClass: WindowSizeClass) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppRouter.StarterScreen.route) {
        composable(
            route = AppRouter.StarterScreen.route
        ) {
            StarterScreen(
                navHostController = navController
            )
        }
        authNavGraph(navController)
        composable(
            route = AppRouter.MainScreen.route
        ) {
            val mainViewModel = hiltViewModel<MainViewModel>()
            MainScreen(
                mainNav = navController,
                state =  mainViewModel.state,
                events = mainViewModel::events
            )
        }

    }
}
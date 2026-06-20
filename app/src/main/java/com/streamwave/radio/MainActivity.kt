package com.streamwave.radio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.streamwave.radio.core.theme.StreamWaveTheme
import com.streamwave.radio.ui.admin.AdminScreen
import com.streamwave.radio.ui.favorites.FavoritesScreen
import com.streamwave.radio.ui.home.HomeScreen
import com.streamwave.radio.ui.personal.AddPersonalStationScreen
import com.streamwave.radio.ui.personal.MyStationsScreen
import com.streamwave.radio.ui.player.FullPlayerScreen
import com.streamwave.radio.ui.settings.SettingsScreen
import dagger.hilt.android.AndroidEntryPoint

object NavRoutes {
    const val HOME = "home"
    const val PLAYER = "player"
    const val FAVORITES = "favorites"
    const val SETTINGS = "settings"
    const val ADD_PERSONAL = "add_personal"
    const val MY_STATIONS = "my_stations"
    const val ADMIN = "admin"
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StreamWaveTheme {
                AppNavHost()
            }
        }
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(NavRoutes.HOME) {
            HomeScreen(
                onOpenFullPlayer = { navController.navigate(NavRoutes.PLAYER) },
                onOpenFavorites = { navController.navigate(NavRoutes.FAVORITES) },
                onOpenSettings = { navController.navigate(NavRoutes.SETTINGS) },
                onAddPersonalStation = { navController.navigate(NavRoutes.ADD_PERSONAL) }
            )
        }
        composable(NavRoutes.PLAYER) {
            FullPlayerScreen(onBack = { navController.popBackStack() })
        }
        composable(NavRoutes.FAVORITES) {
            FavoritesScreen(onBack = { navController.popBackStack() })
        }
        composable(NavRoutes.SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onOpenAdmin = { navController.navigate(NavRoutes.ADMIN) },
                onOpenMyStations = { navController.navigate(NavRoutes.MY_STATIONS) }
            )
        }
        composable(NavRoutes.ADD_PERSONAL) {
            AddPersonalStationScreen(onBack = { navController.popBackStack() })
        }
        composable(NavRoutes.MY_STATIONS) {
            MyStationsScreen(onBack = { navController.popBackStack() })
        }
        composable(NavRoutes.ADMIN) {
            AdminScreen(onBack = { navController.popBackStack() })
        }
    }
}

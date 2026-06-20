package com.streamwave.radio

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.streamwave.radio.core.localization.LanguageManager
import com.streamwave.radio.core.theme.StreamWaveTheme
import com.streamwave.radio.ui.admin.AdminScreen
import com.streamwave.radio.ui.favorites.FavoritesScreen
import com.streamwave.radio.ui.home.HomeScreen
import com.streamwave.radio.ui.personal.AddPersonalStationScreen
import com.streamwave.radio.ui.personal.MyStationsScreen
import com.streamwave.radio.ui.player.FullPlayerScreen
import com.streamwave.radio.ui.settings.SettingsScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

object NavRoutes {
    const val HOME = "home"; const val FULL_PLAYER = "full_player"
    const val FAVORITES = "favorites"; const val SETTINGS = "settings"
    const val ADD_PERSONAL = "add_personal"; const val MY_STATIONS = "my_stations"
    const val ADMIN = "admin"
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var languageManager: LanguageManager

    override fun attachBaseContext(newBase: Context) {
        val lang = try {
            (newBase.applicationContext as StreamWaveApp).languageManager.getInitialLanguage()
        } catch (e: Exception) { "en" }
        super.attachBaseContext(LanguageManager.wrapContextWithLocale(newBase, LanguageManager.getLocaleForCode(lang)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Fullscreen immersive mode — balken verdwijnen, swipe omhoog om te tonen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.apply {
                hide(android.view.WindowInsets.Type.systemBars())
                systemBarsBehavior = android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
                or android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
        }

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

    NavHost(navController = navController, startDestination = NavRoutes.HOME, modifier = Modifier.fillMaxSize()) {
        composable(NavRoutes.HOME) {
            HomeScreen(
                onOpenFullPlayer = { navController.navigate(NavRoutes.FULL_PLAYER) },
                onOpenFavorites = { navController.navigate(NavRoutes.FAVORITES) },
                onOpenSettings = { navController.navigate(NavRoutes.SETTINGS) },
                onAddPersonalStation = { navController.navigate(NavRoutes.ADD_PERSONAL) }
            )
        }
        composable(NavRoutes.FULL_PLAYER) { FullPlayerScreen(onBack = { navController.popBackStack() }) }
        composable(NavRoutes.FAVORITES) { FavoritesScreen(onBack = { navController.popBackStack() }) }
        composable(NavRoutes.SETTINGS) {
            SettingsScreen(onBack = { navController.popBackStack() },
                onOpenAdmin = { navController.navigate(NavRoutes.ADMIN) },
                onOpenMyStations = { navController.navigate(NavRoutes.MY_STATIONS) })
        }
        composable(NavRoutes.MY_STATIONS) { MyStationsScreen(onBack = { navController.popBackStack() }) }
        composable(NavRoutes.ADD_PERSONAL) { AddPersonalStationScreen(onBack = { navController.popBackStack() }) }
        composable(NavRoutes.ADMIN) { AdminScreen(onBack = { navController.popBackStack() }) }
    }
}

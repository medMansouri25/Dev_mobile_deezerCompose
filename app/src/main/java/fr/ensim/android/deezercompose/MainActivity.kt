package fr.ensim.android.deezercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.ensim.android.deezercompose.ui.screen.*
import fr.ensim.android.deezercompose.ui.theme.DeezerComposeTheme
import fr.ensim.android.deezercompose.viewmodel.DeezerViewModel
import androidx.compose.foundation.layout.systemBarsPadding

sealed class Screen(val route: String) {
    object Search : Screen("search")
    object Albums : Screen("albums/{artistId}/{artistName}") {
        fun createRoute(artistId: String, artistName: String) = "albums/$artistId/$artistName"
    }
    object Tracks : Screen("tracks/{albumId}") {
        fun createRoute(albumId: String) = "tracks/$albumId"
    }
    object Player : Screen("player/{albumId}/{trackId}") {
        fun createRoute(albumId: String, trackId: String) = "player/$albumId/$trackId"
    }
    object Favorites : Screen("favorites")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeezerComposeTheme {
                val navController = rememberNavController()
                val viewModel: DeezerViewModel = viewModel()
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                val showBottomBar = currentRoute in listOf(
                    Screen.Search.route,
                    Screen.Favorites.route
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize().systemBarsPadding(),
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar {
                                NavigationBarItem(
                                    selected = currentRoute == Screen.Search.route,
                                    onClick = {
                                        viewModel.clearSearch()
                                        navController.navigate(Screen.Search.route) {
                                            popUpTo(Screen.Search.route) { inclusive = true }
                                        }
                                    },
                                    icon = { Icon(Icons.Default.Home, contentDescription = "Accueil") },
                                    label = { Text("Accueil") }
                                )
                                NavigationBarItem(
                                    selected = currentRoute == Screen.Favorites.route,
                                    onClick = {
                                        navController.navigate(Screen.Favorites.route) {
                                            popUpTo(Screen.Search.route)
                                        }
                                    },
                                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoris") },
                                    label = { Text("Favoris") }
                                )
                            }
                        }
                    }
                ) { paddingValues ->
                    DeezerNavHost(
                        navController = navController,
                        viewModel = viewModel,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}

@Composable
fun DeezerNavHost(
    navController: NavHostController,
    viewModel: DeezerViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Search.route,
        modifier = modifier
    ) {
        composable(Screen.Search.route) {
            SearchArtistScreen(
                viewModel = viewModel,
                onArtistClick = { artistId, artistName ->
                    navController.navigate(Screen.Albums.createRoute(artistId, artistName))
                },
                onAlbumClick = { albumId ->
                    navController.navigate(Screen.Tracks.createRoute(albumId))
                }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(viewModel = viewModel)
        }

        composable(
            route = Screen.Albums.route,
            arguments = listOf(
                navArgument("artistId") { type = NavType.StringType },
                navArgument("artistName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val artistId = backStackEntry.arguments?.getString("artistId") ?: return@composable
            val artistName = backStackEntry.arguments?.getString("artistName") ?: ""
            AlbumScreen(
                artistId = artistId,
                artistName = artistName,
                viewModel = viewModel,
                onAlbumClick = { albumId ->
                    navController.navigate(Screen.Tracks.createRoute(albumId))
                }
            )
        }

        composable(
            route = Screen.Tracks.route,
            arguments = listOf(navArgument("albumId") { type = NavType.StringType })
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getString("albumId") ?: return@composable
            TrackScreen(
                albumId = albumId,
                viewModel = viewModel,
                onTrackClick = { trackId ->
                    navController.navigate(Screen.Player.createRoute(albumId, trackId))
                }
            )
        }

        composable(
            route = Screen.Player.route,
            arguments = listOf(
                navArgument("albumId") { type = NavType.StringType },
                navArgument("trackId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getString("albumId") ?: return@composable
            val trackId = backStackEntry.arguments?.getString("trackId") ?: return@composable
            PlayerScreen(
                trackId = trackId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
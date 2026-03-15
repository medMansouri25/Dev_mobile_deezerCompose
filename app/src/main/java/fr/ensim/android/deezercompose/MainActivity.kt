package fr.ensim.android.deezercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.ensim.android.deezercompose.ui.screen.AlbumScreen
import fr.ensim.android.deezercompose.ui.screen.SearchArtistScreen
import fr.ensim.android.deezercompose.ui.screen.TrackScreen
import fr.ensim.android.deezercompose.ui.theme.DeezerComposeTheme
import fr.ensim.android.deezercompose.viewmodel.DeezerViewModel
import androidx.compose.foundation.layout.systemBarsPadding

sealed class Screen(val route: String) {
    object Search : Screen("search")
    object Albums : Screen("albums/{artistId}") {
        fun createRoute(artistId: String) = "albums/$artistId"
    }
    object Tracks : Screen("tracks/{albumId}") {
        fun createRoute(albumId: String) = "tracks/$albumId"
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeezerComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize().systemBarsPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel: DeezerViewModel = viewModel()
                    DeezerNavHost(navController, viewModel)

                }
            }
        }
    }
}

@Composable
fun DeezerNavHost(
    navController: NavHostController,
    viewModel: DeezerViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Search.route
    ) {
        composable(Screen.Search.route) {
            SearchArtistScreen(
                viewModel = viewModel,
                onArtistClick = { artistId ->
                    navController.navigate(Screen.Albums.createRoute(artistId))
                },
                onAlbumClick = { albumId ->
                    navController.navigate(Screen.Tracks.createRoute(albumId))
                }
            )
        }

        composable(
            route = Screen.Albums.route,
            arguments = listOf(navArgument("artistId") { type = NavType.StringType })
        ) { backStackEntry ->
            val artistId = backStackEntry.arguments?.getString("artistId") ?: return@composable
            AlbumScreen(
                artistId = artistId,
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
                viewModel = viewModel
            )
        }
    }
}
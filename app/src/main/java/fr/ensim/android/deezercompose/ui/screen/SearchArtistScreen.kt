package fr.ensim.android.deezercompose.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import fr.ensim.android.deezercompose.service.model.Artist
import fr.ensim.android.deezercompose.viewmodel.DeezerViewModel

@Composable
fun SearchArtistScreen(
    viewModel: DeezerViewModel,
    onArtistClick: (String, String) -> Unit = { _, _ -> },
    onAlbumClick: (String) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    val artists by viewModel.artistsState.collectAsState()
    val albums by viewModel.albumsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPopularAlbums()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Rechercher un artiste") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.searchArtist(query) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Rechercher")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (artists.isNotEmpty()) {
            Text("Artistes", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(artists) { artist ->
                    ArtistItem(artist = artist, onClick = { onArtistClick(artist.id.toString(), artist.name) })
                }
            }
        }

        if (albums.isNotEmpty() && artists.isEmpty()) {
            Text("Albums populaires", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(albums) { album ->
                    AlbumItem(album = album, onClick = { onAlbumClick(album.id.toString()) })
                }
            }
        }
    }
}

@Composable
fun ArtistItem(artist: Artist, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(artist.picture_medium),
                contentDescription = artist.name,
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = artist.name, style = MaterialTheme.typography.titleMedium)
        }
    }
}
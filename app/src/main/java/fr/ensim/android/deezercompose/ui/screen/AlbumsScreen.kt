package fr.ensim.android.deezercompose.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import fr.ensim.android.deezercompose.service.model.Album
import fr.ensim.android.deezercompose.viewmodel.DeezerViewModel

@Composable
fun AlbumScreen(
    artistId: String,
    viewModel: DeezerViewModel,
    onAlbumClick: (String) -> Unit = {}
) {
    val albums by viewModel.albumsState.collectAsState()

    LaunchedEffect(artistId) {
        viewModel.searchArtistAlbums(artistId)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(albums) { album ->
            AlbumItem(
                album = album,
                onClick = { onAlbumClick(album.id.toString()) }
            )
        }
    }
}

@Composable
fun AlbumItem(
    album: Album,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(album.cover_medium),
                contentDescription = album.title,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.size(12.dp))

            Column {
                Text(text = album.title)
            }
        }
    }
}
package fr.ensim.android.deezercompose.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.ensim.android.deezercompose.service.model.Track
import fr.ensim.android.deezercompose.viewmodel.DeezerViewModel

@Composable
fun TrackScreen(
    albumId: String,
    viewModel: DeezerViewModel,
    onTrackClick: (String) -> Unit = {}
) {
    val tracks by viewModel.tracksState.collectAsState()
    val albumTitle by viewModel.albumTitleState.collectAsState()

    LaunchedEffect(albumId) {
        viewModel.searchAlbum(albumId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = albumTitle,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tracks) { track ->
                TrackItem(
                    track = track,
                    onPlayClick = { onTrackClick(track.id.toString()) }
                )
            }
        }
    }
}

@Composable
fun TrackItem(
    track: Track,
    onPlayClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPlayClick) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = track.title, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = formatDuration(track.duration),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
    HorizontalDivider()
}

fun formatDuration(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60
    return "%d:%02d".format(min, sec)
}
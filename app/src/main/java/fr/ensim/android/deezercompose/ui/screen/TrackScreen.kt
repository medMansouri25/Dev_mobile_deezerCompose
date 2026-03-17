package fr.ensim.android.deezercompose.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontStyle
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = albumTitle,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "${tracks.size} titres",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontStyle = FontStyle.Italic
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .width(60.dp)
                .height(3.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    ),
                    shape = RoundedCornerShape(2.dp)
                )
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            itemsIndexed(tracks) { index, track ->
                TrackItem(
                    track = track,
                    trackNumber = index + 1,
                    onPlayClick = { onTrackClick(track.id.toString()) }
                )
            }
        }
    }
}

@Composable
fun TrackItem(
    track: Track,
    trackNumber: Int,
    onPlayClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$trackNumber",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.width(28.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = track.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = formatDuration(track.duration),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onPlayClick) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

fun formatDuration(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60
    return "%d:%02d".format(min, sec)
}
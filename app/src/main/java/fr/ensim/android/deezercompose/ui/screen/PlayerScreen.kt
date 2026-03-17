package fr.ensim.android.deezercompose.ui.screen

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import fr.ensim.android.deezercompose.viewmodel.DeezerViewModel
import kotlinx.coroutines.delay

@Composable
fun PlayerScreen(
    trackId: String,
    viewModel: DeezerViewModel,
    onBack: () -> Unit
) {
    val track by viewModel.currentTrack.collectAsState()
    val albumCover by viewModel.albumCoverState.collectAsState()
    val albumTitle by viewModel.albumTitleState.collectAsState()

    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableFloatStateOf(0f) }
    var duration by remember { mutableFloatStateOf(0f) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    fun playTrack(previewUrl: String?) {
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
        currentPosition = 0f

        previewUrl?.let { url ->
            val player = MediaPlayer()
            player.setDataSource(url)
            player.prepareAsync()
            player.setOnPreparedListener {
                duration = it.duration.toFloat()
                it.start()
                isPlaying = true
            }
            player.setOnCompletionListener {
                isPlaying = false
                currentPosition = 0f
            }
            mediaPlayer = player
        }
    }

    LaunchedEffect(trackId) {
        viewModel.selectTrack(trackId)
    }

    LaunchedEffect(track) {
        track?.preview?.let { playTrack(it) }
    }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            try {
                mediaPlayer?.let {
                    if (it.isPlaying) {
                        currentPosition = it.currentPosition.toFloat()
                    }
                }
            } catch (e: IllegalStateException) {
                break
            }
            delay(500)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.surfaceVariant,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Barre du haut
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                mediaPlayer?.release()
                onBack()
            }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Retour",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Text(
                text = "EN LECTURE",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            track?.let { currentTrack ->
                IconButton(onClick = { viewModel.toggleFavorite(currentTrack) }) {
                    Icon(
                        imageVector = if (viewModel.isFavorite(currentTrack.id))
                            Icons.Default.Favorite
                        else Icons.Default.FavoriteBorder,
                        contentDescription = "Favori",
                        tint = if (viewModel.isFavorite(currentTrack.id))
                            MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Image album avec bordure dorée
        Box(
            modifier = Modifier
                .size(296.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                )
                .padding(3.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(albumCover),
                contentDescription = albumTitle,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(22.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Titre + album
        track?.let {
            Text(
                text = it.title,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = albumTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Barre de progression
        Slider(
            value = if (duration > 0) currentPosition / duration else 0f,
            onValueChange = { newValue ->
                try {
                    mediaPlayer?.let {
                        val newPosition = (newValue * it.duration).toInt()
                        it.seekTo(newPosition)
                        currentPosition = newPosition.toFloat()
                    }
                } catch (e: IllegalStateException) { }
            },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatDuration((currentPosition / 1000).toInt()),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = formatDuration((duration / 1000).toInt()),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Contrôles
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    val prev = viewModel.previousTrack()
                    prev?.preview?.let { playTrack(it) }
                },
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    Icons.Default.SkipPrevious,
                    contentDescription = "Précédent",
                    modifier = Modifier.size(36.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            // Bouton play rond doré
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        try {
                            if (isPlaying) {
                                mediaPlayer?.pause()
                                isPlaying = false
                            } else {
                                mediaPlayer?.start()
                                isPlaying = true
                            }
                        } catch (e: IllegalStateException) { }
                    }
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause
                        else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            IconButton(
                onClick = {
                    val next = viewModel.nextTrack()
                    next?.preview?.let { playTrack(it) }
                },
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    Icons.Default.SkipNext,
                    contentDescription = "Suivant",
                    modifier = Modifier.size(36.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
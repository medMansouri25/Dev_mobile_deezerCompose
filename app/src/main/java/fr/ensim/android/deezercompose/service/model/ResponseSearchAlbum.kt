package fr.ensim.android.deezercompose.service.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
@JsonIgnoreUnknownKeys
data class ResponseSearchAlbum(
    val id: Long,
    val title: String,
    val tracks: Tracks,
    val cover_medium: String? = null,
)

@Serializable
data class Tracks(
    val data: List<Track>

)

@Serializable
data class Track(
    val id: Long,
    val title: String,
    val duration: Int,
    val preview: String? = null
)
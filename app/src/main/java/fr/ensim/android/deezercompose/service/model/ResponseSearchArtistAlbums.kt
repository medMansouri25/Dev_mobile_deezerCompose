package fr.ensim.android.deezercompose.service.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
@JsonIgnoreUnknownKeys
data class ResponseSearchArtistAlbums(
    val data: List<Album>
)

@Serializable
data class Album(
    val id: Long,
    val title: String,
    val cover_medium: String? = null,
    val cover_large: String? = null
)
package fr.ensim.android.deezercompose.service.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
@JsonIgnoreUnknownKeys
data class ResponseSearchArtist(
    val data: List<Artist>
)

@Serializable
data class Artist(
    val id: Long,
    val name: String,
    val picture_medium: String? = null
)
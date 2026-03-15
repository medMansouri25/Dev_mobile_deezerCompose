package fr.ensim.android.deezercompose.repository

import fr.ensim.android.deezercompose.service.DeezerService
import fr.ensim.android.deezercompose.service.model.ResponseSearchAlbum
import fr.ensim.android.deezercompose.service.model.ResponseSearchArtist
import fr.ensim.android.deezercompose.service.model.ResponseSearchArtistAlbums

class DeezeRepository {
    suspend fun searchArtist(name: String): ResponseSearchArtist {
        return DeezerService.api.searchArtist(name)
    }

    suspend fun searchArtistAlbums(id: String): ResponseSearchArtistAlbums {
        return DeezerService.api.searchArtistAlbums(id)
    }

    suspend fun searchAlbum(id: String): ResponseSearchAlbum {
        return DeezerService.api.searchAlbum(id)
    }
    suspend fun getPopularAlbums(): ResponseSearchArtistAlbums {
        return DeezerService.api.getPopularAlbums()
    }
}
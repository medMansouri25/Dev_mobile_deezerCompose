package fr.ensim.android.deezercompose.service

import fr.ensim.android.deezercompose.service.model.ResponseSearchAlbum
import fr.ensim.android.deezercompose.service.model.ResponseSearchArtist
import fr.ensim.android.deezercompose.service.model.ResponseSearchArtistAlbums
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeezerRest {

    @GET("search/artist")
    suspend fun searchArtist(@Query("q") name: String): ResponseSearchArtist

    @GET("chart/0/albums")
    suspend fun getPopularAlbums(): ResponseSearchArtistAlbums

    @GET("artist/{id}/albums")
    suspend fun searchArtistAlbums(@Path("id") id: String): ResponseSearchArtistAlbums

    @GET("album/{id}")
    suspend fun searchAlbum(@Path("id") id: String): ResponseSearchAlbum
}
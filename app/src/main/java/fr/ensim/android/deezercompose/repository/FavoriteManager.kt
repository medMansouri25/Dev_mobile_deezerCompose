package fr.ensim.android.deezercompose.repository

import android.content.Context
import android.content.SharedPreferences
import fr.ensim.android.deezercompose.service.model.Track
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FavoritesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }

    fun saveFavorites(tracks: List<Track>) {
        val jsonString = json.encodeToString(tracks)
        prefs.edit().putString("favorite_tracks", jsonString).apply()
    }

    fun loadFavorites(): List<Track> {
        val jsonString = prefs.getString("favorite_tracks", null) ?: return emptyList()
        return try {
            json.decodeFromString(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
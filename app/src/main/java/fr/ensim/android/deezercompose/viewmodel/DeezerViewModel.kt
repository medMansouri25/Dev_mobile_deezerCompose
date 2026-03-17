package fr.ensim.android.deezercompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.ensim.android.deezercompose.repository.DeezeRepository
import fr.ensim.android.deezercompose.service.model.Album
import fr.ensim.android.deezercompose.service.model.Artist
import fr.ensim.android.deezercompose.service.model.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeezerViewModel : ViewModel() {
    private val repository = DeezeRepository()

    // Artists
    private val _artistsState = MutableStateFlow<List<Artist>>(emptyList())
    val artistsState: StateFlow<List<Artist>> = _artistsState

    fun searchArtist(name: String) {
        viewModelScope.launch {
            try {
                val response = repository.searchArtist(name)
                _artistsState.value = response.data
            } catch (e: Exception) {
                e.printStackTrace()
                _artistsState.value = emptyList()
            }
        }
    }

    // Albums
    private val _albumsState = MutableStateFlow<List<Album>>(emptyList())
    val albumsState: StateFlow<List<Album>> = _albumsState

    fun searchArtistAlbums(id: String) {
        viewModelScope.launch {
            try {
                val response = repository.searchArtistAlbums(id)
                _albumsState.value = response.data
            } catch (e: Exception) {
                e.printStackTrace()
                _albumsState.value = emptyList()
            }
        }
    }

    fun loadPopularAlbums() {
        viewModelScope.launch {
            try {
                val response = repository.getPopularAlbums()
                _albumsState.value = response.data
            } catch (e: Exception) {
                e.printStackTrace()
                _albumsState.value = emptyList()
            }
        }
    }

    // Tracks
    private val _tracksState = MutableStateFlow<List<Track>>(emptyList())
    val tracksState: StateFlow<List<Track>> = _tracksState

    private val _albumTitleState = MutableStateFlow("")
    val albumTitleState: StateFlow<String> = _albumTitleState

    private val _albumCoverState = MutableStateFlow<String?>(null)
    val albumCoverState: StateFlow<String?> = _albumCoverState

    fun searchAlbum(id: String) {
        viewModelScope.launch {
            try {
                val response = repository.searchAlbum(id)
                _tracksState.value = response.tracks.data
                _albumTitleState.value = response.title
                _albumCoverState.value = response.cover_medium
            } catch (e: Exception) {
                e.printStackTrace()
                _tracksState.value = emptyList()
            }
        }
    }

    // Player
    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack: StateFlow<Track?> = _currentTrack

    private val _currentTrackIndex = MutableStateFlow(0)
    val currentTrackIndex: StateFlow<Int> = _currentTrackIndex

    fun selectTrack(trackId: String) {
        val index = _tracksState.value.indexOfFirst { it.id.toString() == trackId }
        if (index >= 0) {
            _currentTrackIndex.value = index
            _currentTrack.value = _tracksState.value[index]
        }
    }

    fun nextTrack(): Track? {
        val tracks = _tracksState.value
        val nextIndex = _currentTrackIndex.value + 1
        if (nextIndex < tracks.size) {
            _currentTrackIndex.value = nextIndex
            _currentTrack.value = tracks[nextIndex]
            return tracks[nextIndex]
        }
        return null
    }

    fun previousTrack(): Track? {
        val prevIndex = _currentTrackIndex.value - 1
        if (prevIndex >= 0) {
            _currentTrackIndex.value = prevIndex
            _currentTrack.value = _tracksState.value[prevIndex]
            return _tracksState.value[prevIndex]
        }
        return null
    }

    // Favoris
    private val _favoriteTracks = MutableStateFlow<List<Track>>(emptyList())
    val favoriteTracks: StateFlow<List<Track>> = _favoriteTracks

    fun toggleFavorite(track: Track) {
        val current = _favoriteTracks.value.toMutableList()
        if (current.any { it.id == track.id }) {
            current.removeAll { it.id == track.id }
        } else {
            current.add(track)
        }
        _favoriteTracks.value = current
    }

    fun isFavorite(trackId: Long): Boolean {
        return _favoriteTracks.value.any { it.id == trackId }
    }
}
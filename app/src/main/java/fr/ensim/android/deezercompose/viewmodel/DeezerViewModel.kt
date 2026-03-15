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

class DeezerViewModel : androidx.lifecycle.ViewModel() {
    private val repository = DeezeRepository()

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

    // Tracks
    private val _tracksState = MutableStateFlow<List<Track>>(emptyList())
    val tracksState: StateFlow<List<Track>> = _tracksState

    private val _albumTitleState = MutableStateFlow("")
    val albumTitleState: StateFlow<String> = _albumTitleState

    fun searchAlbum(id: String) {
        viewModelScope.launch {
            try {
                val response = repository.searchAlbum(id)
                _tracksState.value = response.tracks.data
                _albumTitleState.value = response.title
            } catch (e: Exception) {
                e.printStackTrace()
                _tracksState.value = emptyList()
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
}
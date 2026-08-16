import com.baszczyk.mediaplayerapp.models.SongWithState

data class ListUiState(
    val songs: List<SongWithState> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
package co.touchlab.kampkit.data

sealed class SearchState {
    data class Success(val entries: List<HowLongToBeatEntry>): SearchState()
    data class Error(val error: Exception): SearchState()
    object Loading: SearchState()
}

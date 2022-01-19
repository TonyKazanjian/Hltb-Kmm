package co.touchlab.kampkit.data

data class SearchState(
    val entries: List<HowLongToBeatEntry> = emptyList(),
    val error: Exception? = null,
    val isLoading: Boolean = false
)

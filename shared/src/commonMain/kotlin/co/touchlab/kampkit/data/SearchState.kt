package co.touchlab.kampkit.data

data class SearchState(
    val query: String = "",
    val page: Int = 1,
    val entries: MutableList<HowLongToBeatEntry> = mutableListOf(),
    val error: Exception? = null,
    val isLoading: Boolean = false
)

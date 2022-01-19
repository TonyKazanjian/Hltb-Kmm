package co.touchlab.kampkit.data

import co.touchlab.kampkit.scraper.SearchInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SearchRepository(private val searchInteractor: SearchInteractor) {

    private var _query = MutableStateFlow("")
    val query: StateFlow<String>
        get() = _query.asStateFlow()

    fun setGameQuery(query: String) {
        _query.value = query
    }

    fun searchGames(page: Int): Flow<SearchState> {
        return _query
            .debounce(300)
            .filter { query -> query.isNotEmpty() }
            .flatMapLatest { query ->
                flow {
                    emit(launchSearch(query, page))
                }.flowOn(Dispatchers.Main)
            }
    }

    private suspend fun launchSearch(gameQuery: String, page: Int): SearchState {
        return try {
            val entries = searchInteractor.search(gameQuery, page).entryList
            SearchState(
                entries = entries
            )
        } catch (e: Exception){
            SearchState(
                error = e
            )
        }
    }
}
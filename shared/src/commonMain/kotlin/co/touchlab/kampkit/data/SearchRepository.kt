package co.touchlab.kampkit.data

import co.touchlab.kampkit.scraper.SearchInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SearchRepository(private val searchInteractor: SearchInteractor) {

    private var _searchState = MutableStateFlow(SearchState(isLoading = true))
    val searchState = _searchState.asStateFlow()

    fun setSearchState(searchState: SearchState){
        _searchState.value = searchState
    }

    fun searchGames(): Flow<SearchState> {
        return _searchState
            .flatMapLatest { state ->
                flow {
                    emit(launchSearch(state))
                }.flowOn(Dispatchers.Main)
            }
    }

    private suspend fun launchSearch(searchState: SearchState): SearchState {
        return try {
            val entries = searchInteractor.search(searchState.query, searchState.page).entryList
            searchState.entries.addAll(entries)
            searchState.copy(entries = searchState.entries)
        } catch (e: Exception){
            searchState.copy(error = e)
        }
    }
}
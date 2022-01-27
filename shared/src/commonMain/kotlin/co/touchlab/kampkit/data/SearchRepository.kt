package co.touchlab.kampkit.data

import co.touchlab.kampkit.scraper.SearchInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

class SearchRepository(private val searchInteractor: SearchInteractor) {

    fun searchGames(searchStateFlow: StateFlow<SearchState>): Flow<SearchState> {
        return searchStateFlow
            .debounce(300)
            // .filter { it.query.isNotEmpty() }
            .flatMapLatest { state ->
                flow {
                    emit(launchSearch(state))
                }.onStart {
                    if (state.page == 1) {
                        emit(SearchState(isLoading = true))
                    }
                }.flowOn(Dispatchers.Main)
            }
    }

    private suspend fun launchSearch(searchState: SearchState): SearchState {
        return try {
            val entries = searchInteractor.search(searchState.query, searchState.page).entryList
            searchState.entries.addAll(entries)
            searchState.copy(entries = searchState.entries, isLoading = false)
        } catch (e: Exception){
            searchState.copy(error = e, isLoading = false)
        }
    }
}
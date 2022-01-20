package co.touchlab.kampkit

import co.touchlab.kampkit.data.SearchRepository
import co.touchlab.kampkit.data.SearchState
import co.touchlab.kermit.Logger
import co.touchlab.stately.ensureNeverFrozen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class SearchViewModel(
    private val repository: SearchRepository,
    private val onSearchState: (SearchState) -> Unit
): KoinComponent {

    private val log: Logger by injectLogger("iOSSearchViewModel")
    private val scope = MainScope(Dispatchers.Main, log)

    private val searchState = repository.searchState

    init {
        ensureNeverFrozen()
        observeQueries()
    }

    @OptIn(FlowPreview::class)
    fun observeQueries() {
        scope.launch {
            log.v { "getting games"}
            repository.searchGames()
                .collect { state ->
                    onSearchState(state)
                }
        }
    }

    fun searchGamesByQuery(query: String){
        repository.setSearchState(SearchState(query = query, isLoading = true))
    }

    fun getNextPage(){
        repository.setSearchState(searchState.value.copy(page = searchState.value.page + 1))
    }

    fun onDestroy(){
        scope.onDestroy()
    }
}
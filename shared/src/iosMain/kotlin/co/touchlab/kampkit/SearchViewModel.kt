package co.touchlab.kampkit

import co.touchlab.kampkit.data.SearchRepository
import co.touchlab.kampkit.data.SearchState
import co.touchlab.kermit.Logger
import co.touchlab.stately.ensureNeverFrozen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import platform.Accelerate._SparseRefactorQR_Float

class SearchViewModel(
    private val repository: SearchRepository,
    private val onSearchState: (SearchState) -> Unit
): KoinComponent {

    private val log: Logger by injectLogger("iOSSearchViewModel")
    private val scope = MainScope(Dispatchers.Main, log)

    private val _searchStateFlow = MutableStateFlow(SearchState())


    init {
        ensureNeverFrozen()
        observeQueries(_searchStateFlow.value.query)
    }

    @OptIn(FlowPreview::class)
    fun observeQueries(query: String) {
        _searchStateFlow.value = SearchState(
            query = query,
            isLoading = true)
        scope.launch {
            log.v { "getting games"}
            repository.searchGames(_searchStateFlow.asStateFlow())
                .collect { state ->
                    onSearchState(state)
                }
        }
    }

    fun getNextPage(){
        _searchStateFlow.value = _searchStateFlow.value.copy(page = _searchStateFlow.value.page + 1)
        repository.searchGames(_searchStateFlow)    }

    fun onDestroy(){
        scope.onDestroy()
    }
}
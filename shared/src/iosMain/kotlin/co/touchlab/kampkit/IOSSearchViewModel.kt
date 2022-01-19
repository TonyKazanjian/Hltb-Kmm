package co.touchlab.kampkit

import co.touchlab.kampkit.data.SearchRepository
import co.touchlab.kampkit.data.SearchState
import co.touchlab.kermit.Logger
import co.touchlab.stately.ensureNeverFrozen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class IOSSearchViewModel(
    private val repository: SearchRepository,
    private val onSearchState: (SearchState) -> Unit
): KoinComponent {

    private val log: Logger by injectLogger("iOSSearchViewModel")
    private val scope = MainScope(Dispatchers.Main, log)

    init {
        ensureNeverFrozen()
        observeQueries()
    }

    @OptIn(FlowPreview::class)
    fun observeQueries() {
        scope.launch {
            log.v { "getting games"}
            repository.searchGames(1)
                .collect { state ->
                    onSearchState(state)
                }
        }
    }

    fun setQuery(query: String) {
        repository.setGameQuery(query)
    }

    fun onDestroy(){
        scope.onDestroy()
    }
}
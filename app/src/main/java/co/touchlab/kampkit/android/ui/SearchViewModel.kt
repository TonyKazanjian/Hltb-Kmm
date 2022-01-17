package co.touchlab.kampkit.android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kampkit.data.SearchRepository
import co.touchlab.kampkit.data.SearchState
import co.touchlab.kampkit.scraper.AndroidSearchInteractor
import co.touchlab.kampkit.scraper.SearchInteractor
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent

class SearchViewModel(private val repository: SearchRepository = SearchRepository(AndroidSearchInteractor())): ViewModel(), KoinComponent {

    val queryStateFlow = repository.query

    val searchStateFlow: StateFlow<SearchState> = repository.searchGames(1)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SearchState.Loading)

    fun getEntriesByQuery(query: String){
        repository.setGameQuery(query)
    }

}
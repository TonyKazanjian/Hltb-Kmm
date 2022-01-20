package co.touchlab.kampkit.android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kampkit.data.SearchRepository
import co.touchlab.kampkit.data.SearchState
import co.touchlab.kampkit.scraper.AndroidSearchInteractor
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent

class SearchViewModel(private val repository: SearchRepository = SearchRepository(AndroidSearchInteractor())): ViewModel(), KoinComponent {

    private val searchState = repository.searchState

    val searchStateFlow: StateFlow<SearchState> = repository.searchGames()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SearchState(page = searchState.value.page, isLoading = searchState.value.isLoading))

    fun searchGamesByQuery(query: String){
        repository.setSearchState(SearchState(query = query, isLoading = true))
    }

    fun getNextPage(){
        repository.setSearchState(searchState.value.copy(page = searchState.value.page + 1))
    }

}
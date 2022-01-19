package co.touchlab.kampkit.android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kampkit.data.SearchRepository
import co.touchlab.kampkit.data.SearchState
import co.touchlab.kampkit.scraper.AndroidSearchInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent

class SearchViewModel(private val repository: SearchRepository = SearchRepository(AndroidSearchInteractor())): ViewModel(), KoinComponent {

    private val searchState = MutableStateFlow(SearchState())

    val searchStateFlow: StateFlow<SearchState> = repository.searchGames()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SearchState(page = searchState.value.page, isLoading = searchState.value.isLoading))

    fun searchGamesByQuery(query: String){
        searchState.value = SearchState(query = query)
        repository.setSearchState(searchState.value)
    }

    fun getNextPage(){
        searchState.value = searchState.value.copy(page = searchState.value.page + 1)
        repository.setSearchState(searchState.value)
    }

}
package co.touchlab.kampkit.android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kampkit.data.SearchRepository
import co.touchlab.kampkit.data.SearchState
import co.touchlab.kampkit.scraper.AndroidSearchInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent

class SearchViewModel(private val repository: SearchRepository = SearchRepository(AndroidSearchInteractor())): ViewModel(), KoinComponent {
    
    private val _searchStateFlow = MutableStateFlow(SearchState())

    fun setSearchQuery(query: String) {
        _searchStateFlow.value = SearchState(
            query = query,
            isLoading = true)
        repository.searchGames(_searchStateFlow.asStateFlow())
    }

    val searchStateFlow: StateFlow<SearchState> = repository.searchGames(_searchStateFlow)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SearchState())


    fun getNextPage(){
        _searchStateFlow.value = searchStateFlow.value.copy(page = _searchStateFlow.value.page + 1)
        repository.searchGames(_searchStateFlow)
    }

}
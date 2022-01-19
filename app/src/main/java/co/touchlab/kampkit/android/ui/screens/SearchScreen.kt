package co.touchlab.kampkit.android.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.flowWithLifecycle
import co.touchlab.kampkit.android.R
import co.touchlab.kampkit.android.ui.SearchViewModel
import co.touchlab.kampkit.android.ui.components.GameList
import co.touchlab.kampkit.data.HowLongToBeatEntry
import co.touchlab.kampkit.data.SearchState
import co.touchlab.kermit.Logger
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    log: Logger
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val lifecycleAwareSearchFlow = remember(viewModel.searchStateFlow, lifecycleOwner) {
        viewModel.searchStateFlow.flowWithLifecycle(lifecycleOwner.lifecycle)
    }

    @SuppressLint("StateFlowValueCalledInComposition") // False positive lint check when used inside collectAsState()
    val searchState by lifecycleAwareSearchFlow.collectAsState(viewModel.searchStateFlow.value)

    SearchResultContent(
        searchState = searchState,
        onRefresh = { viewModel.getEntriesByQuery("Zelda")},
        onSuccess = { data -> log.v { "View updating with ${data.size} games" } },
        onError = { exception -> log.e { "Displaying error: $exception" } },
        // onFavorite = { viewModel.updateBreedFavorite(it) }
    )
}

@Composable
fun SearchResultContent(
    searchState: SearchState,
    onRefresh: () -> Unit = {},
    onSuccess: (List<HowLongToBeatEntry>) -> Unit = {},
    onError: (String) -> Unit = {},
    // onFavorite: (Breed) -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = searchState.isLoading),
            onRefresh = onRefresh
        ) {

            if (searchState.isLoading) {

            } else if (searchState.entries.isNotEmpty()){
                onSuccess(searchState.entries)
                Success(successData = searchState.entries)
            } else if (searchState.error != null) {
                onError(searchState.error?.message ?: "There was an error")
                Error(
                    error = searchState.error?.message ?: "There was an error"
                )
            }
        }
    }
}

@Composable
fun Empty() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(R.string.empty_breeds))
    }
}

@Composable
fun Error(error: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = error)
    }
}

@Composable
fun Success(
    successData: List<HowLongToBeatEntry>,
    // favoriteBreed: (Breed) -> Unit
) {
    GameList(games = successData)
}

@Preview
@Composable
fun MainScreenContentPreview_Success() {
    SearchResultContent(
        searchState = SearchState(
            entries = listOf(
                HowLongToBeatEntry("Yakuza 0", 0, "https://howlongtobeat.com/games/256px-Yakuza-sega.jpg", mapOf(
                    "Main Story" to "12 hours",
                    "Main Story + Extra" to "24 hours",
                    "Completionist" to "36 hours")),
                HowLongToBeatEntry("Yakuza Kiwami", 0, "", mapOf(
                    "Main Story" to "12 hours",
                    "Main Story + Extra" to "24 hours",
                    "Completionist" to "36 hours")),
                HowLongToBeatEntry("Yakuza Kiwami 2", 0, "", mapOf(
                    "Main Story" to "12 hours",
                    "Main Story + Extra" to "24 hours",
                    "Completionist" to "36 hours")),
            )
        )
    )
}
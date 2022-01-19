package co.touchlab.kampkit.android.ui.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.flowWithLifecycle
import co.touchlab.kampkit.android.ui.SearchViewModel
import co.touchlab.kampkit.android.ui.search.components.GameList
import co.touchlab.kampkit.android.ui.search.components.SearchAppBar
import co.touchlab.kampkit.data.HowLongToBeatEntry
import co.touchlab.kampkit.data.SearchState
import co.touchlab.kermit.Logger

@ExperimentalComposeUiApi
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

    val gameQuery = rememberSaveable{ mutableStateOf("")}

    Scaffold(
        topBar = {
            SearchAppBar(
                query = gameQuery.value,
                onQueryChanged = {
                    gameQuery.value = it
                },
                onExecuteSearch = {
                    viewModel.searchGamesByQuery(gameQuery.value)
                },
                onClearClick = {
                    gameQuery.value = ""
                })
        }
    ) {
        SearchResultContent(
            searchState = searchState,
            onTriggerNextPage = { viewModel.getNextPage() },
            onItemClicked = {}
            // onFavorite = { viewModel.updateBreedFavorite(it) }
        )
    }
}

@Composable
fun SearchResultContent(
    searchState: SearchState,
    onTriggerNextPage: () -> Unit,
    onItemClicked: (HowLongToBeatEntry) -> Unit
    // onFavorite: (Breed) -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            when {
                searchState.isLoading -> {
                    //TODO - shimmer loading items
                    CircularProgressIndicator(Modifier.size(48.dp))
                }
                searchState.entries.isNotEmpty() -> {
                    Success(
                        successData = searchState.entries,
                        onTriggerNextPage = onTriggerNextPage) {
                        onItemClicked(it)
                    }
                }
                searchState.error != null -> {
                    Error(
                        error = searchState.error?.message ?: "There was an error"
                    )
                }
            }
        }

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
    onTriggerNextPage: () -> Unit,
    onItemClicked: (HowLongToBeatEntry) -> Unit
) {
    GameList(
        games = successData,
        onTriggerNextPage = onTriggerNextPage) {
        onItemClicked(it)
    }
}

@Preview
@Composable
fun MainScreenContentPreview_Success() {
    SearchResultContent(
        searchState = SearchState(
            page = 1,
            entries = mutableListOf(
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
        ),
        onTriggerNextPage = {},
        onItemClicked = {}
    )
}
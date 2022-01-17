package co.touchlab.kampkit.android.ui

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.flowWithLifecycle
import co.touchlab.kampkit.android.R
import co.touchlab.kampkit.data.HowLongToBeatEntry
import co.touchlab.kampkit.data.SearchState
import co.touchlab.kampkit.db.Breed
import co.touchlab.kermit.Logger
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun MainScreen(
    viewModel: SearchViewModel,
    log: Logger
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val lifecycleAwareSearchFlow = remember(viewModel.searchStateFlow, lifecycleOwner) {
        viewModel.searchStateFlow.flowWithLifecycle(lifecycleOwner.lifecycle)
    }

    @SuppressLint("StateFlowValueCalledInComposition") // False positive lint check when used inside collectAsState()
    val searchState by lifecycleAwareSearchFlow.collectAsState(viewModel.searchStateFlow.value)

    MainScreenContent(
        searchState = searchState,
        onRefresh = { viewModel.getEntriesByQuery("Zelda")},
        onSuccess = { data -> log.v { "View updating with ${data.size} games" } },
        onError = { exception -> log.e { "Displaying error: $exception" } },
        // onFavorite = { viewModel.updateBreedFavorite(it) }
    )
}

@Composable
fun MainScreenContent(
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
            state = rememberSwipeRefreshState(isRefreshing = searchState is SearchState.Loading),
            onRefresh = onRefresh
        ) {

            when (searchState) {
                is SearchState.Success -> {
                    onSuccess(searchState.entries)
                    Success(successData = searchState.entries)
                }
                is SearchState.Error -> {
                    onError(searchState.error.message ?: "There was an error")
                }
                is SearchState.Loading -> {
                }
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
    GameList(breeds = successData)
}

@Composable
fun GameList(breeds: List<HowLongToBeatEntry>, onItemClick: (HowLongToBeatEntry) -> Unit = {}) {
    LazyColumn {
        items(breeds) { breed ->
            GameRow(breed) {
                onItemClick(it)
            }
            Divider()
        }
    }
}

@Composable
fun GameRow(game: HowLongToBeatEntry, onClick: (HowLongToBeatEntry) -> Unit) {
    Row(
        Modifier
            .clickable { onClick(game) }
            .padding(10.dp)
    ) {
        Text(game.title!!, Modifier.weight(1F))
    }
}

@Composable
fun FavoriteIcon(breed: Breed) {
    Crossfade(
        targetState = breed.favorite == 0L,
        animationSpec = TweenSpec(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        )
    ) { fav ->
        if (fav) {
            Image(
                painter = painterResource(id = R.drawable.ic_favorite_border_24px),
                contentDescription = stringResource(R.string.favorite_breed, breed.name)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_favorite_24px),
                contentDescription = stringResource(R.string.unfavorite_breed, breed.name)
            )
        }
    }
}

// @Preview
// @Composable
// fun MainScreenContentPreview_Success() {
//     MainScreenContent(
//         dogsState = DataState(
//             data = ItemDataSummary(
//                 longestItem = null,
//                 allItems = listOf(
//                     Breed(0, "appenzeller", 0),
//                     Breed(1, "australian", 1)
//                 )
//             )
//         )
//     )
// }

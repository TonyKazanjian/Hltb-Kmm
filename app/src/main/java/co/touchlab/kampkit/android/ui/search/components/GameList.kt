package co.touchlab.kampkit.android.ui.search.components

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.touchlab.kampkit.data.HowLongToBeatEntry
import co.touchlab.kampkit.data.SEARCH_RESPONSE_PAGE_SIZE
import co.touchlab.kampkit.data.SearchState

@Composable
fun GameList(
    searchState: SearchState,
    listState: LazyListState,
    onTriggerNextPage: () -> Unit,
    onItemClick: (HowLongToBeatEntry) -> Unit) {

    LazyColumn(state = listState) {
        itemsIndexed(items = searchState.entries) { index, game ->
            if ((index + 1) >= (searchState.page * SEARCH_RESPONSE_PAGE_SIZE)) {
                onTriggerNextPage()
            }
            GameRow(game = game, modifier = Modifier.fillParentMaxWidth().wrapContentHeight()){
                onItemClick(it)
            }
        }
    }
}
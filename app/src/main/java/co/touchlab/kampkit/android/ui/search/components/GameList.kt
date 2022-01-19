package co.touchlab.kampkit.android.ui.search.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.touchlab.kampkit.data.HowLongToBeatEntry

@Composable
fun GameList(
    games: List<HowLongToBeatEntry>,
    onTriggerNextPage: () -> Unit,
    onItemClick: (HowLongToBeatEntry) -> Unit) {
    LazyColumn {
        itemsIndexed(items = games) { index, game ->
            if (index + 1 == games.size) {
                onTriggerNextPage()
            }
            GameRow(game = game, modifier = Modifier.fillParentMaxWidth().wrapContentHeight()){
                onItemClick(it)
            }
        }
    }
}
package co.touchlab.kampkit.android.ui.search.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.touchlab.kampkit.data.HowLongToBeatEntry

@Composable
fun GameList(games: List<HowLongToBeatEntry>, onItemClick: (HowLongToBeatEntry) -> Unit = {}) {
    LazyColumn {
        items(games) { breed ->
            GameRow(breed, Modifier.fillParentMaxWidth().wrapContentHeight()) {
                onItemClick(it)
            }
            Divider()
        }
    }
}
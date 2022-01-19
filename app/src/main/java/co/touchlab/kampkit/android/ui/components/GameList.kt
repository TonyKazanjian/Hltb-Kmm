package co.touchlab.kampkit.android.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.touchlab.kampkit.data.HowLongToBeatEntry

@Composable
fun GameList(games: List<HowLongToBeatEntry>, onItemClick: (HowLongToBeatEntry) -> Unit = {}) {
    LazyColumn {
        items(games) { breed ->
            GameRow(breed, Modifier.wrapContentHeight().fillMaxWidth()) {
                onItemClick(it)
            }
            Divider()
        }
    }
}
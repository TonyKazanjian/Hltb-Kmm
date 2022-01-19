package co.touchlab.kampkit.android.ui.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import co.touchlab.kampkit.data.HowLongToBeatEntry
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun GameRow(game: HowLongToBeatEntry, modifier: Modifier, onClick: (HowLongToBeatEntry) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .clickable { onClick(game) }
            .padding(10.dp)
    ) {
        GameItem(game = game)
    }
}

@Composable
fun GameItem(game: HowLongToBeatEntry){
    game.imageUrl?.let {
        GameImage(imageUrl = it)
    }

    Column {
        Text(text = game.title!!)
        game.timeLabels?.forEach { labelMap ->
            val labelText = "${labelMap.key}: ${labelMap.value}"
            Text(text = labelText)
        }
    }
}

@Composable
fun GameImage(imageUrl: String){
    Box(modifier = Modifier.size(150.dp).requiredWidth(125.dp)){
        CoilImage(
            imageModel = imageUrl,
            contentScale = ContentScale.Fit)
    }
}
package co.touchlab.kampkit.android.ui.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.touchlab.kampkit.data.HowLongToBeatEntry
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun GameRow(game: HowLongToBeatEntry, modifier: Modifier, onClick: (HowLongToBeatEntry) -> Unit) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .clickable { onClick(game) },
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp
    ) {
        Box(modifier = Modifier
            .requiredHeight(150.dp)){
            val gradient = Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color.Black),
                startY = 0f,  // 1/3
                endY = 400.toFloat(),
                tileMode = TileMode.Clamp
            )
            CoilImage(
                imageModel = game.imageUrl,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .align(Alignment.Center)
                    .graphicsLayer(
                        scaleX = 4f,
                        scaleY = 4f
                    ))
            Box(modifier = Modifier.matchParentSize().background(gradient))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = modifier
                    .padding(10.dp)
            ) {
                GameItem(game = game)
            }
        }
    }
}

@Composable
fun GameItem(game: HowLongToBeatEntry){
    game.imageUrl?.let {
        GameImage(imageUrl = it)
    }

    Column {
        Text(text = game.title!!, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.subtitle1.copy(
            shadow = Shadow(
                color = Color.DarkGray,
                offset = Offset(4f, 4f),
                blurRadius = 8f
            )
        ))
        game.timeLabels?.forEach { labelMap ->
            val labelText = "${labelMap.key}: ${labelMap.value}"
            Text(text = labelText, color = Color.White)
        }
    }
}

@Composable
fun GameImage(imageUrl: String){
    Box(modifier = Modifier
        .size(150.dp)
        .requiredWidth(125.dp)){
        CoilImage(
            imageModel = imageUrl,
            contentScale = ContentScale.Fit)
    }
}
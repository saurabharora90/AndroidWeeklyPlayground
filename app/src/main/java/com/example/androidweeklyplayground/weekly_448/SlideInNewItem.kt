package com.example.androidweeklyplayground.weekly_448

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.androidweeklyplayground.helpers.Content
import com.example.androidweeklyplayground.helpers.getDefaultContent
import kotlinx.collections.immutable.ImmutableList

@Composable
private fun NewsItem(content: Content, index: Int, modifier: Modifier = Modifier) {
    val listItemAnimation = rememberListItemAnimation(index)

    Card(modifier = modifier
        .offset {
            IntOffset(
                x = listItemAnimation.offset.roundToPx(),
                y = 0
            )
        }
        .graphicsLayer {
            this.alpha = listItemAnimation.alpha
        }) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = content.title)
            Text(
                text = content.body,
                modifier = Modifier.padding(vertical = 12.dp),
                maxLines = 7,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    LaunchedEffect(key1 = index, block = {
        listItemAnimation.animateToEnd()
    })
}

@Composable
fun ItemAnimatedList(
    content: ImmutableList<Content> = getDefaultContent(LocalContext.current),
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = {
            itemsIndexed(items = content) { index, content ->
                NewsItem(
                    content = content,
                    index = index,
                )
            }
        })
}
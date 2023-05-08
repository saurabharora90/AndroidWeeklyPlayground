package com.example.androidweeklyplayground.weekly_534

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.androidweeklyplayground.helpers.Content
import com.example.androidweeklyplayground.helpers.getDefaultContent
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ExpandableListScreen(
    modifier: Modifier = Modifier,
    content: ImmutableList<Content> = getDefaultContent(LocalContext.current)
) {
    Surface(modifier = modifier.fillMaxSize(), color = Color.Red) {
        ExpandableList(content = content)
    }
}

@Composable
private fun ExpandableList(
    content: ImmutableList<Content>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = {
            items(items = content, key = { it.title }) { item ->
                var isExpanded by rememberSaveable(item) { mutableStateOf(false) }

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { isExpanded = !isExpanded }) {

                    HeaderText(header = item.title)
                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                        exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
                    ) {
                        BodyView(body = item.body)
                    }
                }
            }
        }
    )
}

@Composable
private fun HeaderText(header: String, modifier: Modifier = Modifier) {
    Text(text = header, modifier = modifier.padding(horizontal = 16.dp))
}

@Composable
private fun BodyView(body: String, modifier: Modifier = Modifier) {
    Text(
        text = body,
        modifier = modifier
            .padding(top = 8.dp)
            .background(color = Color.Magenta)
            .padding(vertical = 8.dp, horizontal = 16.dp)
    )
}
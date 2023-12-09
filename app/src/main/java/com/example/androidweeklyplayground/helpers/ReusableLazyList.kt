package com.example.androidweeklyplayground.helpers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun getDefaultList() : ImmutableList<String> {
    val list = mutableListOf<String>()
    repeat(30) {
        list.add("Lorum Ipsum #$it")
    }
    return list.toImmutableList()
}

@Composable
fun ReusableVerticalLazyList(modifier: Modifier = Modifier,
                             scrollState: LazyListState = rememberLazyListState(),
                             content : ImmutableList<String> = getDefaultList()) {
    LazyColumn(
        modifier =modifier,
        state =scrollState,
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        //horizontalAlignment =,
        content = {
            items(items = content) {
                Text(text = it, modifier = Modifier.fillMaxWidth())
            }
        })
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReusableLazyListPreview() {
    ReusableVerticalLazyList()
}
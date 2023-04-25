package com.example.androidweeklyplayground.helpers

import android.content.Context
import com.example.androidweeklyplayground.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class Content(val title: String, val body: String)

fun getDefaultContent(context: Context): ImmutableList<Content> {
    val list = mutableListOf<Content>()
    repeat(20) {
        list.add(
            Content(
                "Item #$it",
                "Content for item $it : " + context.resources.getString(R.string.lorem_ipsum)
            )
        )
    }

    return list.toImmutableList()
}

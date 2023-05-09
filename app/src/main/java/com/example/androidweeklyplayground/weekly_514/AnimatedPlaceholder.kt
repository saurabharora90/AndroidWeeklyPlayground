package com.example.androidweeklyplayground.weekly_514

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedPlaceHolderScreen(modifier: Modifier = Modifier
    .fillMaxWidth()
    .padding(16.dp)) {
    var entry by remember { mutableStateOf("") }
    TextField(
        value = entry,
        onValueChange = { entry = it },
        modifier = modifier,
        placeholder = {
            AnimatedPlaceholder(
                listOf(
                    "Search with author's name",
                    "Search with author's title",
                    "Search your favourite topic",
                )
            )
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        },
        trailingIcon = {},
        supportingText = {},
        singleLine = true,
        shape = RoundedCornerShape(25.dp),
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedPlaceholder(hints: List<String>) {
    var placeholderState by remember { mutableStateOf(hints[0]) }
    AnimatedContent(
        targetState = placeholderState,
        label = "placeholder",
        transitionSpec = {
            ContentTransform(
                targetContentEnter = slideInVertically(
                    animationSpec = tween(easing = LinearEasing),
                    initialOffsetY = { it / 2 })
                        + fadeIn(tween(easing = LinearEasing)),
                initialContentExit = slideOutVertically(
                    tween(
                        durationMillis = 100,
                        easing = LinearEasing
                    )
                )
                        + fadeOut(tween(100, easing = LinearEasing))
            )
        }
    ) {
        Text(text = it)
    }

    LaunchedEffect(hints) {
        var index = 0
        while (true) {
            delay(2.seconds)
            index++
            if (index >= hints.size)
                index = 0

            placeholderState = hints[index]
        }
    }
}
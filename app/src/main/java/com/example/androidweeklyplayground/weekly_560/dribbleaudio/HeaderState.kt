package com.example.androidweeklyplayground.weekly_560.dribbleaudio

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val INITIAL_IMAGE_SIZE = 140.0f
private const val FINAL_IMAGE_SIZE = 80.0f

class HeaderState(
    private val coroutineScope: CoroutineScope
) {
    private val headerAnimatable = Animatable(0.0f)
    private val imageAnimatable = Animatable(INITIAL_IMAGE_SIZE)

    val headerElevation: Dp
        get() = headerAnimatable.value.dp

    val imageSize: Dp
        get() = imageAnimatable.value.dp

    val nestedScrollConnection = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val delta = available.y
            if (delta < 0 && imageAnimatable.value == INITIAL_IMAGE_SIZE && !imageAnimatable.isRunning) {
                coroutineScope.launch {
                    imageAnimatable.animateTo(
                        FINAL_IMAGE_SIZE,
                        animationSpec = tween()
                    )
                }
                coroutineScope.launch {
                    headerAnimatable.animateTo(1.0f, tween(600))
                }
            }
            return Offset.Zero
        }

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset {
            val delta = available.y
            if (delta > 0 && imageAnimatable.value == FINAL_IMAGE_SIZE && !imageAnimatable.isRunning) {
                coroutineScope.launch {
                    imageAnimatable.animateTo(
                        INITIAL_IMAGE_SIZE,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
                    )
                }
                coroutineScope.launch {
                    headerAnimatable.animateTo(0.0f)
                }
            }
            return Offset.Zero
        }
    }
}

@Composable
internal fun rememberHeaderState(
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): HeaderState {
    return remember { HeaderState(coroutineScope) }
}
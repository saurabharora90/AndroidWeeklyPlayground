package com.example.androidweeklyplayground.weekly_448

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal class ListItemAnimation(
    private val animationState: MutableTransitionState<ListItemAnimationState>,
    private val offsetState: State<Dp>,
    private val alphaState: State<Float>
) {

    val offset
        get() = offsetState.value

    val alpha
        get() = alphaState.value

    fun animateToEnd() {
        animationState.targetState = ListItemAnimationState.FINAL
    }
}

enum class ListItemAnimationState {
    INITIAL, FINAL
}

@Composable
internal fun rememberListItemAnimation(
    key: Any,
    initialState: ListItemAnimationState = ListItemAnimationState.INITIAL
): ListItemAnimation {
    val transitionState = remember { MutableTransitionState(initialState) }
    val transition = updateTransition(targetState = transitionState, label = "")

    val offsetState = transition.animateDp(transitionSpec = { tween() }, label = "") {
        when (it.targetState) {
            ListItemAnimationState.INITIAL -> 180.dp
            ListItemAnimationState.FINAL -> 0.dp
        }
    }

    val alphaState =
        transition.animateFloat(transitionSpec = { tween() }, label = "") {
            when (it.targetState) {
                ListItemAnimationState.INITIAL -> 0.0f
                ListItemAnimationState.FINAL -> 1.0f
            }
        }

    return remember(key) { ListItemAnimation(transitionState, offsetState, alphaState) }
}
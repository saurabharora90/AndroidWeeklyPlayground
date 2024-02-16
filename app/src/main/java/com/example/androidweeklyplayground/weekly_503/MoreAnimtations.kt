package com.example.androidweeklyplayground.weekly_503

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidweeklyplayground.ui.theme.AndroidWeeklyPlaygroundTheme
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Destination
@Composable
fun Weekly503Animations() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        StepperAnimation()
        HeartAnimation()
        ProgressAnimation()
        WaveAnimation()
    }
}

@Composable
private fun StepperAnimation(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    var counter by remember { mutableIntStateOf(0) }
    val rotation = remember { Animatable(0.0f) }
    Card(modifier = modifier
        .size(150.dp, 70.dp)
        .graphicsLayer {
            this.rotationY = rotation.value
        }
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                val startTargetBox = size.width * 0.15
                val endTargetBox = size.width * 0.85
                if (it.x <= startTargetBox) {
                    counter--
                    coroutineScope.launch {
                        rotation.animateTo(-35.0f)
                        rotation.animateTo(0.0f)
                    }
                } else if (it.x >= endTargetBox) {
                    counter++
                    coroutineScope.launch {
                        rotation.animateTo(35.0f)
                        rotation.animateTo(0.0f)
                    }
                }
            })
        }) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = counter.toString(), style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
private fun HeartAnimation(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val animationSpec = infiniteRepeatable<Float>(
        animation = tween(800, easing = LinearEasing),
        repeatMode = RepeatMode.Reverse
    )

    val translationY by infiniteTransition.animateFloat(
        initialValue = 0.0f,
        targetValue = -50.0f,
        animationSpec = animationSpec,
        label = ""
    )
    val width by infiniteTransition.animateFloat(
        initialValue = 70f,
        targetValue = 20f,
        animationSpec = animationSpec,
        label = ""
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.75f,
        targetValue = 0.2f,
        animationSpec = animationSpec,
        label = ""
    )

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = "Heart",
            modifier = Modifier
                .size(96.dp)
                .graphicsLayer {
                    this.translationY = translationY
                })

        Box(
            modifier = Modifier
                .size(width = width.dp, height = 10.dp)
                .alpha(alpha)
        ) {
            Box(
                modifier
                    .fillMaxSize()
                    .background(Color.White, shape = CircleShape)
            )
        }
    }
}

@Composable
private fun ProgressAnimation(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "ball")
    val finalValue = -40.0f
    val animationValues = (1..3).map {
        transition.animateFloat(
            initialValue = 0.0f,
            targetValue = finalValue,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 800),
                repeatMode = RepeatMode.Reverse,
                initialStartOffset = StartOffset(it * 70)
            ),
            label = "ball $it"
        )
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        animationValues.forEach {
            Ball(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .requiredSize(20.dp)
                    .graphicsLayer {
                        translationY = it.value
                    }
            )
        }
    }
}

@Composable
private fun Ball(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier,
        onDraw = {
            drawCircle(color = Color.LightGray)
        })
}

@Composable
private fun WaveAnimation(modifier: Modifier = Modifier) {
    val waves = listOf(
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) },
    )

    val animationSpec = infiniteRepeatable<Float>(
        animation = tween(4000, easing = FastOutLinearInEasing),
        repeatMode = RepeatMode.Restart,
    )

    waves.forEachIndexed { index, animatable ->
        LaunchedEffect(animatable) {
            delay(index * 1000L)
            animatable.animateTo(
                targetValue = 1f, animationSpec = animationSpec
            )
        }
    }

    val dys = waves.map { it.value }


    Box(modifier = modifier) {
        dys.forEach { dy ->
            Canvas(modifier = Modifier
                .size(30.dp)
                .graphicsLayer {
                    scaleX = dy * 4 + 1
                    scaleY = dy * 4 + 1
                    alpha = 1 - dy
                }, onDraw = {
                drawCircle(Color.White)
            })
        }
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            tint = Color.Blue,
            contentDescription = "Heart",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ScreenPreview() {
    AndroidWeeklyPlaygroundTheme {
        Weekly503Animations()
    }
}
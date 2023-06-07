package com.example.androidweeklyplayground.weekly_519

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidweeklyplayground.ui.theme.AndroidWeeklyPlaygroundTheme
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Destination
@Composable
fun CanvasArcProgressScreen(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    Column(modifier = modifier) {
        var sliderPercentage by remember {
            mutableStateOf(0f)
        }
        val percentage = remember { Animatable(0f) }

        CanvasArcProgressBar(
            progressPercentage = percentage.value, modifier = Modifier
                .padding(90.dp)
                .requiredSize(300.dp)
        )

        Text(text = sliderPercentage.toString(), modifier = Modifier.padding(horizontal = 16.dp))
        Slider(
            value = sliderPercentage, onValueChange = { sliderPercentage = it },
            modifier = Modifier.padding(horizontal = 16.dp),
            onValueChangeFinished = {
                coroutineScope.launch {
                    percentage.animateTo(
                        sliderPercentage,
                        animationSpec = tween(durationMillis = 500)
                    )
                }
            },
        )
    }
}

@Composable
internal fun CanvasArcProgressBar(
    progressPercentage: Float,
    modifier: Modifier = Modifier
) {
    val progressDegrees by remember(progressPercentage) {
        mutableStateOf(270.0f * progressPercentage)
    }

    Canvas(modifier = modifier.rotate(135f), onDraw = {
        drawArc(
            color = Color.Cyan,
            startAngle = 0f,
            sweepAngle = progressDegrees,
            useCenter = false,
            style = Stroke(width = 15.dp.toPx(), cap = StrokeCap.Round),
        )

        drawArc(
            color = Color.Red,
            startAngle = progressDegrees,
            sweepAngle = 270 - progressDegrees,
            useCenter = false,
            style = Stroke(width = 15.dp.toPx(), cap = StrokeCap.Round),
        )

        val radius = (size.height / 2)
        val radians = Math.toRadians(progressDegrees.toDouble() - 95)
        val cosRadians = cos(radians)
        val sinRadians = sin(radians)
        val rotatedX = (radius * sinRadians).toFloat()
        val rotatedY = (radius * cosRadians).toFloat()
        val x = size.width / 2 - rotatedX
        val y = size.height / 2 + rotatedY

        if (progressPercentage > 0.0f && progressPercentage < 1.0f) {
            Dot(radius = 10.0f, drawCentre = Offset(x, y))
        }
    })
}

internal fun DrawScope.Dot(radius: Float, drawCentre: Offset = center) {
    drawCircle(color = Color.Black, radius = radius, center = drawCentre)
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CanvasProgressBarPreview() {
    AndroidWeeklyPlaygroundTheme {
        CanvasArcProgressScreen()
    }
}
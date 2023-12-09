package com.example.androidweeklyplayground.weekly_594

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

private val INDICATOR_LENGTH = 14.dp
private val MAJOR_INDICATOR_LENGTH = 18.dp
private val INDICATOR_INITIAL_OFFSET = 5.dp

@Destination
@Composable
fun SpeedometerScreen(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    Column(modifier = modifier) {
        val speed = remember { Animatable(0f) }

        Speedometer(
            currentSpeed = speed.value,
            modifier = Modifier
                .padding(90.dp)
                .requiredSize(360.dp)
        )

        Slider(
            value = speed.value,
            valueRange = 0f..240f,
            onValueChange = {
                coroutineScope.launch {
                    speed.animateTo(
                        it,
                        animationSpec = tween(durationMillis = 50, easing = LinearOutSlowInEasing)
                    )
                }
            },
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Composable
private fun Speedometer(
    @FloatRange(from = 0.0, to = 240.0) currentSpeed: Float,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val textColor = MaterialTheme.colorScheme.onSurface
    val indicatorColor = MaterialTheme.colorScheme.onSurface
    Canvas(modifier = modifier, onDraw = {
        drawArc(
            color = Color.Red,
            startAngle = 30f,
            sweepAngle = -240f,
            useCenter = false,
            style = Stroke(width = 2.0.dp.toPx())
        )

        for (angle in 300 downTo 60 step 2) {
            val speed = 300 - angle

            val startOffset =
                pointOnCircle(
                    thetaInDegrees = angle.toDouble(),
                    radius = size.height / 2 - INDICATOR_INITIAL_OFFSET.toPx(),
                    cX = center.x,
                    cY = center.y
                )

            if (speed % 20 == 0) {
                val markerOffset = pointOnCircle(
                    thetaInDegrees = angle.toDouble(),
                    radius = size.height / 2 - MAJOR_INDICATOR_LENGTH.toPx(),
                    cX = center.x,
                    cY = center.y
                )
                speedMarker(startOffset, markerOffset, SolidColor(indicatorColor), 4.dp.toPx())
                speedText(
                    speed = speed,
                    angle = angle,
                    textMeasurer = textMeasurer,
                    textColor = textColor
                )
            } else if (speed % 10 == 0) {
                val endOffset = pointOnCircle(
                    thetaInDegrees = angle.toDouble(),
                    radius = size.height / 2 - INDICATOR_LENGTH.toPx(),
                    cX = center.x,
                    cY = center.y
                )
                speedMarker(startOffset, endOffset, SolidColor(Color.Blue), 2.dp.toPx())
            } else {
                val endOffset = pointOnCircle(
                    thetaInDegrees = angle.toDouble(),
                    radius = size.height / 2 - INDICATOR_LENGTH.toPx(),
                    cX = center.x,
                    cY = center.y
                )
                speedMarker(startOffset, endOffset, SolidColor(Color.Blue), 1.dp.toPx())
            }
        }

        speedIndicator(speedAngle = 300 - currentSpeed)
    })
}

private fun DrawScope.speedMarker(
    startPoint: Offset,
    endPoint: Offset,
    brush: Brush,
    strokeWidth: Float
) {
    drawLine(brush = brush, start = startPoint, end = endPoint, strokeWidth = strokeWidth)
}

private fun DrawScope.speedText(
    speed: Int,
    angle: Int,
    textColor: Color,
    textMeasurer: TextMeasurer
) {
    val textLayoutResult = textMeasurer.measure(
        text = speed.toString(),
        style = TextStyle.Default.copy(lineHeight = TextUnit(0.0f, TextUnitType.Sp))
    )
    val textWidth = textLayoutResult.size.width
    val textHeight = textLayoutResult.size.height

    val textOffset = pointOnCircle(
        thetaInDegrees = angle.toDouble(),
        radius = size.height / 2 - MAJOR_INDICATOR_LENGTH.toPx() - textWidth / 2 - INDICATOR_INITIAL_OFFSET.toPx(), // Adjusting radius with text width
        cX = center.x,
        cY = center.y
    )

    drawContext.canvas.save()
    // Translate to the text offset point, adjusting for vertical centering.
    drawContext.canvas.translate(
        textOffset.x - textWidth / 2,
        textOffset.y - textHeight / 2
    )

    drawText(textLayoutResult, color = textColor)

    drawContext.canvas.restore()
}

private fun DrawScope.speedIndicator(
    speedAngle: Float
) {
    val endOffset = pointOnCircle(
        thetaInDegrees = speedAngle.toDouble(),
        radius = size.height / 2 - INDICATOR_LENGTH.toPx(),
        cX = center.x,
        cY = center.y
    )

    drawLine(
        color = Color.Magenta,
        start = center,
        end = endOffset,
        strokeWidth = 6.dp.toPx(),
        cap = StrokeCap.Round,
        alpha = 0.5f
    )
}

private fun pointOnCircle(
    thetaInDegrees: Double,
    radius: Float,
    cX: Float = 0f,
    cY: Float = 0f
): Offset {
    val x = cX + (radius * sin(Math.toRadians(thetaInDegrees)).toFloat())
    val y = cY + (radius * cos(Math.toRadians(thetaInDegrees)).toFloat())

    return Offset(x, y)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SpeedometerPreview() {
    SpeedometerScreen()
}
package com.example.androidweeklyplayground.weekly_x

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.androidweeklyplayground.ui.theme.AndroidWeeklyPlaygroundTheme
import kotlin.math.cos
import kotlin.math.sin

private val INDICATOR_LENGTH = 14.dp
private val MAJOR_INDICATOR_LENGTH = 18.dp
private val INDICATOR_INITIAL_OFFSET = 5.dp

@OptIn(ExperimentalTextApi::class)
@Composable
fun Speedometer(currentSpeed: Float, modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()
    val canvasModifier = modifier
        .requiredSize(360.dp)
    Canvas(modifier = canvasModifier, onDraw = {
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
                speedMarker(startOffset, markerOffset, SolidColor(Color.Black), 4.dp.toPx())
                speedText(speed = speed, angle = angle, textMeasurer = textMeasurer)
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
    })

    SpeedIndicator(speedAngle = 300 - currentSpeed, modifier = canvasModifier)
}

private fun DrawScope.speedMarker(
    startPoint: Offset,
    endPoint: Offset,
    brush: Brush,
    strokeWidth: Float
) {
    drawLine(brush = brush, start = startPoint, end = endPoint, strokeWidth = strokeWidth)
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.speedText(
    speed: Int,
    angle: Int,
    textMeasurer: TextMeasurer
) {
    val textLayoutResult = textMeasurer.measure(
        text = speed.toString(),
        style = TextStyle.Default.copy(lineHeight = TextUnit(0.0f, TextUnitType.Sp))
    )
    val textWidth = textLayoutResult.size.width
    val textHeight = textLayoutResult.size.height
    val angleSpan = (textWidth / (size.height / 2 - MAJOR_INDICATOR_LENGTH.toPx())).toDouble()
    val textAngle = angle - angleSpan / 2

    val textOffset = pointOnCircle(
        thetaInDegrees = textAngle,
        radius = size.height / 2 - MAJOR_INDICATOR_LENGTH.toPx() - textWidth / 2 - INDICATOR_INITIAL_OFFSET.toPx(), // Adjusting radius with text width
        cX = center.x,
        cY = center.y
    )

    drawContext.canvas.save()
    drawContext.canvas.translate(
        textOffset.x - textWidth / 2,
        textOffset.y - textHeight / 2 // Adjust the y position for vertical centering
    )

    drawText(textLayoutResult)

    drawContext.canvas.restore()
}

@Composable
private fun SpeedIndicator(
    speedAngle: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier, onDraw = {
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
    })
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
    AndroidWeeklyPlaygroundTheme {
        Speedometer(currentSpeed = 120f)
    }
}
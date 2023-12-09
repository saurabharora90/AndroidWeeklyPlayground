package com.example.androidweeklyplayground.weekly_475

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidweeklyplayground.ui.theme.AndroidWeeklyPlaygroundTheme
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

private const val MIN_DIGITS = 8

@Destination
@Composable
fun OdometerCounter() {
    var count by remember { mutableIntStateOf(1243) }
    val output = count.toString().padStart(MIN_DIGITS, '0')
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in output.indices) {
            SingleDigitText(digit = output.get(i))
        }
    }


    LaunchedEffect(key1 = Unit, block = {
        while (true) {
            delay(1.seconds)
            count++
        }
    })
}

@Composable
private fun SingleDigitText(digit: Char, modifier: Modifier = Modifier) {
    AnimatedContent(targetState = digit, transitionSpec = {
        if (initialState < targetState) {
            ContentTransform(
                targetContentEnter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                initialContentExit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
            )
        } else {
            ContentTransform(
                targetContentEnter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                initialContentExit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            )
        }
    }, label = "") {
        Text(
            text = it.toString(),
            style = MaterialTheme.typography.headlineMedium,
            modifier = modifier
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun OdometerCounterPreview() {
    AndroidWeeklyPlaygroundTheme {
        Surface {
            OdometerCounter()
        }
    }
}
package com.example.androidweeklyplayground.weekly_479

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidweeklyplayground.ui.theme.AndroidWeeklyPlaygroundTheme
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Duration.Companion.seconds

private const val STORY_VISIBLE_DURATION_SECOND = 4.0

@Composable
@Destination
fun InstagramStories(
    storiesList: ImmutableList<Story> = stories
) {
    var currentStoryIndex by remember { mutableIntStateOf(0) }
    var isPaused by remember { mutableStateOf(false) }
    val progress = remember(currentStoryIndex) { Animatable(0.0f) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(key1 = Unit) {
                detectTapGestures(onPress = {
                    isPaused = true
                    awaitRelease()
                    isPaused = false
                }, onTap = {
                    if (it.x > size.width * 0.75
                        && currentStoryIndex != storiesList.size - 1
                    ) {
                        currentStoryIndex++
                    } else if (it.x < size.width * 0.25 && currentStoryIndex != 0) {
                        currentStoryIndex--
                    }
                })
            }
    ) {
        TextOnlyStory(
            story = storiesList[currentStoryIndex],
            modifier = Modifier.fillMaxSize()
        )
        val spacingBwIndicators = 8.dp
        val sizeForEachProgressIndicator =
            ((maxWidth - spacingBwIndicators) / stories.size) - spacingBwIndicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = spacingBwIndicators)
        ) {
            for (i in 0 until stories.size) {
                ProgressIndicator(
                    modifier = Modifier.size(sizeForEachProgressIndicator, 2.dp),
                    progress = if (currentStoryIndex == i) progress.value else if (i < currentStoryIndex) 1.0f else 0.0f
                )
                Spacer(modifier = Modifier.size(width = spacingBwIndicators, height = 2.dp))
            }
        }

        LaunchedEffect(key1 = progress, key2 = isPaused) {
            if (!isPaused) {
                val percentageCompleted = progress.value
                progress.animateTo(
                    targetValue = 1.0f,
                    animationSpec = TweenSpec(
                        durationMillis = (STORY_VISIBLE_DURATION_SECOND
                                * (1.0 - percentageCompleted)).seconds.inWholeMilliseconds.toInt(),
                        delay = 0,
                        easing = LinearEasing
                    )
                )
                if (currentStoryIndex < storiesList.size - 1)
                    currentStoryIndex++
            } else {
                progress.stop()
            }
        }
    }
}

@Composable
private fun ProgressIndicator(
    modifier: Modifier,
    progress: Float,
) {
    LinearProgressIndicator(
        progress = progress,
        modifier = modifier,
        color = Color.White,
        trackColor = Color.White.copy(alpha = 0.5f),
        strokeCap = StrokeCap.Round
    )
}

@Composable
private fun TextOnlyStory(story: Story, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(story.background),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = story.title,
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = story.content,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

data class Story(val title: String, val content: String, val background: Brush)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InstagramStoriesPreview() {
    AndroidWeeklyPlaygroundTheme {
        InstagramStories(stories)
    }
}

val stories = persistentListOf(
    Story(
        "Story 1", "Excellent. Swipe for Next", Brush.linearGradient(
            0.0f to Color.Red,
            0.3f to Color.Green,
            1.0f to Color.Blue,
        )
    ),
    Story(
        "Story 2", "Can tap for next and also previous", Brush.linearGradient(
            0.0f to Color.Red,
            0.3f to Color.Green,
            1.0f to Color.Blue,
        )
    ),
    Story(
        "Story 3", "Excellent. You have reached story number 4", Brush.linearGradient(
            0.0f to Color.Red,
            0.3f to Color.Green,
            1.0f to Color.Blue,
        )
    ),
    Story(
        "Story 4", "One more story left", Brush.linearGradient(
            0.0f to Color.Red,
            0.3f to Color.Green,
            1.0f to Color.Blue,
        )
    ),
    Story(
        "Story 5", "Last Story. Can only tap for previous", Brush.linearGradient(
            0.0f to Color.Red,
            0.3f to Color.Green,
            1.0f to Color.Blue,
        )
    ),
)
package com.example.androidweeklyplayground.weekly_560.dribbleaudio

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.androidweeklyplayground.R
import com.example.androidweeklyplayground.helpers.ReusableVerticalLazyList
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun DribbleScreen(modifier: Modifier = Modifier) {
    val headerState = rememberHeaderState()
    Scaffold(modifier = modifier,
        topBar = {
            Column(modifier = Modifier.shadow(headerState.headerElevation)) {
                Toolbar()
                PlayBackInfo(
                    modifier = Modifier.padding(top = 32.dp),
                    imageSize = headerState.imageSize
                )
            }
        },
        bottomBar = {
            PlaybackControlsBottomBar(Modifier.height(110.dp))
        }) {
        ReusableVerticalLazyList(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
                .nestedScroll(headerState.nestedScrollConnection)
        )
    }
}

@Composable
private fun PlayBackInfo(
    modifier: Modifier = Modifier,
    imageSize: Dp
) {
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.cat),
            contentDescription = "Artist label",
            modifier = Modifier
                .size(imageSize)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "Running",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text(
            text = "Aurora",
            modifier = Modifier.padding(bottom = 4.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Toolbar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(modifier = modifier,
        title = {
            Text(
                text = "Now Playing",
                style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }, actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
            }
        })
}

@Composable
private fun PlaybackControlsBottomBar(
    modifier: Modifier = Modifier,
    onPlayClicked: () -> Unit = {}
) {
    BottomAppBar(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp)),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.ThumbUp, contentDescription = "Up")
                }
                IconButton(
                    onClick = onPlayClicked,
                    Modifier
                        .clip(CircleShape)
                        .background(Color.Red)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "Down",
                        modifier = Modifier.rotate(180f)
                    )
                }
            }
        }
    }
}
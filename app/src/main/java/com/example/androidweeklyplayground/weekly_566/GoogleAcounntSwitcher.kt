package com.example.androidweeklyplayground.weekly_566

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FixedThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.androidweeklyplayground.R
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

private val accountInfo = listOf(
    AccountInfo(R.drawable.horse),
    AccountInfo(R.drawable.dog),
    AccountInfo(R.drawable.cat),
)

@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleAccountSwitcherScreen(accountInfos: List<AccountInfo> = accountInfo) {
    // A surface container using the 'background' color from the theme
    Surface {
        Scaffold(topBar = {
            var currentlySelectedAccountIndex by remember { mutableStateOf(0) }
            GoogleAccountSwitcherWithSwipeable(
                accountInfo = accountInfos[currentlySelectedAccountIndex],
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp),
                onNextAccount = {
                    if (currentlySelectedAccountIndex == accountInfos.size - 1) {
                        currentlySelectedAccountIndex = 0
                    } else {
                        currentlySelectedAccountIndex++
                    }
                },
                onPreviousAccount = {
                    if (currentlySelectedAccountIndex == 0) {
                        currentlySelectedAccountIndex = accountInfos.size - 1
                    } else {
                        currentlySelectedAccountIndex--
                    }
                }
            )
        }, content = {
            var currentlySelectedAccountIndex by remember { mutableStateOf(0) }
            GoogleAccountSwitcherWithPointerInput(
                accountInfo = accountInfos[currentlySelectedAccountIndex], modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
                    .padding(horizontal = 8.dp, vertical = 16.dp)
            ) {
                if (currentlySelectedAccountIndex == accountInfos.size - 1) {
                    currentlySelectedAccountIndex = 0
                } else {
                    currentlySelectedAccountIndex++
                }
            }
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun GoogleAccountSwitcherWithSwipeable(
    accountInfo: AccountInfo,
    modifier: Modifier = Modifier,
    onNextAccount: () -> Unit,
    onPreviousAccount: () -> Unit
) {
    val context = LocalContext.current
    val imageSize = 32.dp
    val imageSizePx = with(LocalDensity.current) { imageSize.toPx() }
    val swipeableState = rememberSwipeableState(initialValue = "B")
    val transition = remember { Animatable(1.0f) }

    TextField(
        value = "",
        onValueChange = {},
        modifier = modifier,
        label = {
            Text(text = "Search in Mail")
        },
        leadingIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
            }

        },
        trailingIcon = {

            Image(
                painter = painterResource(id = accountInfo.accountImage),
                contentDescription = "Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(imageSize)
                    .swipeable(
                        state = swipeableState,
                        orientation = Orientation.Vertical,
                        anchors = mapOf(
                            -imageSizePx / 2 to "A",
                            0.0f to "B",
                            imageSizePx / 2 to "C"
                        ),
                        thresholds = { _, _ -> FixedThreshold(imageSize / 4) }
                    )
                    .offset { IntOffset(x = 0, y = swipeableState.offset.value.roundToInt()) }
                    .alpha(transition.value)
                    .scale(transition.value)
                    .clip(CircleShape)
                    .clickable {
                        Toast
                            .makeText(context, "Account switcher clicked", Toast.LENGTH_LONG)
                            .show()
                    }
            )
        },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        shape = MaterialTheme.shapes.extraLarge
    )

    LaunchedEffect(Unit) {
        snapshotFlow { swipeableState.currentValue }
            .collect {
                if (it == "C") {
                    swipeableState.snapTo("B")
                    transition.animateTo(0.0f)
                    onNextAccount()
                    transition.animateTo(1.0f)
                } else if (it == "A") {
                    swipeableState.snapTo("B")
                    transition.animateTo(0.0f)
                    onPreviousAccount()
                    transition.animateTo(1.0f)
                }
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoogleAccountSwitcherWithPointerInput(
    accountInfo: AccountInfo,
    modifier: Modifier = Modifier,
    onAccountChanged: () -> Unit
) {
    TextField(
        value = "",
        onValueChange = {},
        modifier = modifier,
        label = {
            Text(text = "Search in mail")
        },
        leadingIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
            }

        },
        trailingIcon = {
            val offsetY = remember { Animatable(0f) }
            val alpha = remember { Animatable(1f) }
            val scale = remember { Animatable(1f) }
            val context = LocalContext.current
            Image(
                painter = painterResource(id = accountInfo.accountImage),
                contentDescription = "Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(32.dp)
                    .pointerInput(Unit) {
                        // Used to calculate a settling position of a fling animation.
                        val decay = splineBasedDecay<Float>(this)
                        val velocityTracker = VelocityTracker()
                        offsetY.updateBounds(
                            lowerBound = -size.height.toFloat() / 2,
                            upperBound = size.height.toFloat() / 2
                        )
                        coroutineScope {
                            detectVerticalDragGestures(
                                onDragEnd = {
                                    launch {
                                        if (abs(offsetY.value) >= size.height / 4) {
                                            offsetY.animateDecay(
                                                velocityTracker.calculateVelocity().y,
                                                decay
                                            )
                                            onAccountChanged()
                                            scale.snapTo(0.0f)
                                        }
                                        offsetY.snapTo(0f)
                                        alpha.snapTo(1f)
                                        if (scale.value == 0f) {
                                            scale.animateTo(1f)
                                        }
                                    }
                                }) { change, dragAmount ->
                                change.consume()
                                val newOffset = offsetY.value + dragAmount
                                launch {
                                    offsetY.snapTo(newOffset)
                                }
                                launch {
                                    if (abs(newOffset) > size.height / 4) {
                                        alpha.snapTo(0.0f)
                                        scale.snapTo(0.0f)
                                    } else
                                        alpha.snapTo(0.5f)
                                }
                                // Record the velocity of the drag.
                                velocityTracker.addPosition(change.uptimeMillis, change.position)
                            }
                        }
                    }
                    .offset(y = offsetY.value.roundToInt().dp)
                    .alpha(alpha.value)
                    .scale(scale.value)
                    .clip(CircleShape)
                    .clickable {
                        Toast
                            .makeText(context, "Account switcher clicked", Toast.LENGTH_LONG)
                            .show()
                    }
            )
        },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        shape = MaterialTheme.shapes.extraLarge
    )
}
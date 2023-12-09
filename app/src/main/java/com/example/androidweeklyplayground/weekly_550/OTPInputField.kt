package com.example.androidweeklyplayground.weekly_550

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.androidweeklyplayground.ui.theme.AndroidWeeklyPlaygroundTheme
import com.ramcosta.composedestinations.annotation.Destination

private const val OTP_LENGTH = 6

@Destination
@Composable
fun OTPInputField() {
    val focusManager: FocusManager = LocalFocusManager.current
    val otp = remember {
        mutableStateListOf<String>().apply {
            repeat(OTP_LENGTH) { add("") }
        }
    }
    val isProcessing by remember {
        derivedStateOf {
            Log.i("Saurabh", "List details: ${otp.toList()}")
            !otp.contains("")
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        OTPRow(
            focusManager = focusManager,
            saveDigit = { input, index ->
                otp[index] = input
            },
            modifier = Modifier.align(Alignment.Center)
        )
        if (isProcessing) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.TopCenter))
        }
    }
}

@Composable
private fun OTPRow(
    focusManager: FocusManager,
    saveDigit: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        for (i in 0..<OTP_LENGTH) {
            var digit by remember { mutableStateOf("") }
            DigitTextField(
                input = digit,
                onValueChange = {
                    if (it.isDigitsOnly() && it.length == 1) {
                        digit = it
                        focusManager.moveFocus(FocusDirection.Next)
                        saveDigit(digit, i)
                    } else if (it.isEmpty()) {
                        digit = ""
                        saveDigit(digit, i)
                    }
                },
                modifier = Modifier.onKeyEvent {
                    if ((it.key == Key.Backspace || it.key == Key.Delete) && digit.isEmpty()) {
                        focusManager.moveFocus(FocusDirection.Previous)
                        return@onKeyEvent true
                    } else {
                        return@onKeyEvent false
                    }
                },
                keyboardActions = KeyboardActions(onNext = {
                    if (digit.isNotEmpty() && i != OTP_LENGTH - 1) {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                })
            )
        }
    }
}

@Composable
private fun DigitTextField(
    input: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions,
) {
    OutlinedTextField(
        value = input,
        onValueChange = onValueChange,
        modifier = modifier
            .padding(horizontal = 8.dp)
            .width(40.dp),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrect = false,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        keyboardActions = keyboardActions,
        singleLine = true,
        shape = RoundedCornerShape(4.dp)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun OTPPreview() {
    AndroidWeeklyPlaygroundTheme {
        Surface {
            OTPInputField()
        }
    }
}
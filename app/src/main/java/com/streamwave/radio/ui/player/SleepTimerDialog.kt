package com.streamwave.radio.ui.player

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.streamwave.radio.R
import com.streamwave.radio.core.theme.*
import com.streamwave.radio.player.SleepTimerManager

@Composable
fun SleepTimerDialogFull(sleepTimerManager: SleepTimerManager, onDismiss: () -> Unit, onStop: () -> Unit) {
    val remaining by sleepTimerManager.remainingSeconds.collectAsState()
    val isActive by sleepTimerManager.isActive.collectAsState()
    var customMinutes by remember { mutableStateOf("") }
    var showCustom by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss, containerColor = Panel, shape = RoundedCornerShape(20.dp),
        title = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("⏰ ${stringResource(R.string.sleep_timer)}", color = PrimaryText, fontWeight = FontWeight.Bold)
                if (isActive) Text(sleepTimerManager.formatRemaining(), color = LiveRed, fontSize = 14.sp)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                if (isActive) {
                    Text(stringResource(R.string.sleep_timer_active), color = SecondaryText, fontSize = 14.sp)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { sleepTimerManager.cancel(); onDismiss() },
                        colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                        shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(R.string.sleep_timer_cancel), color = PrimaryText)
                    }
                } else if (showCustom) {
                    OutlinedTextField(value = customMinutes, onValueChange = { if (it.all { c -> c.isDigit() } && it.length <= 4) customMinutes = it },
                        label = { Text(stringResource(R.string.sleep_timer_custom_minutes), color = SecondaryText) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = fieldColors(), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { showCustom = false }, shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f)) {
                            Text(stringResource(R.string.cancel), color = SecondaryText) }
                        Button(onClick = {
                            val mins = customMinutes.toIntOrNull() ?: 0
                            if (mins > 0) { sleepTimerManager.start(mins) { onStop() }; onDismiss() }
                        }, colors = ButtonDefaults.buttonColors(containerColor = Purple),
                            shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f), enabled = (customMinutes.toIntOrNull() ?: 0) > 0) {
                            Text(stringResource(R.string.start))
                        }
                    }
                } else {
                    val presets = listOf(
                        10 to R.string.sleep_timer_10, 20 to R.string.sleep_timer_20, 30 to R.string.sleep_timer_30,
                        45 to R.string.sleep_timer_45, 60 to R.string.sleep_timer_60, 90 to R.string.sleep_timer_90,
                        120 to R.string.sleep_timer_120, 180 to R.string.sleep_timer_180,
                        240 to R.string.sleep_timer_240, 360 to R.string.sleep_timer_360,
                        480 to R.string.sleep_timer_480, 720 to R.string.sleep_timer_720
                    )
                    presets.forEach { (mins, labelRes) ->
                        TextButton(onClick = { sleepTimerManager.start(mins) { onStop() }; onDismiss() }, modifier = Modifier.fillMaxWidth()) {
                            Text(stringResource(labelRes), color = PrimaryText, fontSize = 16.sp)
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    TextButton(onClick = { showCustom = true }, modifier = Modifier.fillMaxWidth()) {
                        Text("⚙️ ${stringResource(R.string.sleep_timer_custom)}", color = Purple, fontSize = 15.sp)
                    }
                }
            }
        },
        confirmButton = { if (!isActive && !showCustom) TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel), color = SecondaryText) } }
    )
}

@Composable
fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Card, unfocusedContainerColor = Card,
    focusedTextColor = PrimaryText, unfocusedTextColor = PrimaryText,
    cursorColor = Purple, focusedBorderColor = Purple, unfocusedBorderColor = GlassBorder,
    focusedLabelColor = Purple, unfocusedLabelColor = SecondaryText
)

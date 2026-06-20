package com.streamwave.radio.ui.player

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.streamwave.radio.core.theme.*
import com.streamwave.radio.player.SleepTimerManager
import kotlinx.coroutines.delay

@Composable
fun SleepTimerDialogFull(
    sleepTimerManager: SleepTimerManager,
    onDismiss: () -> Unit,
    onStop: () -> Unit
) {
    val remaining by sleepTimerManager.remainingSeconds.collectAsState()
    val isActive by sleepTimerManager.isActive.collectAsState()
    var customMinutes by remember { mutableStateOf("") }
    var showCustom by remember { mutableStateOf(false) }

    // Auto-sluiten als timer afloopt
    LaunchedEffect(remaining, isActive) {
        if (!isActive && remaining == 0L && isActive) {
            // Timer is net afgelopen
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Panel,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("⏰ Slaaptimer", color = PrimaryText, fontWeight = FontWeight.Bold)
                if (isActive) {
                    Text(sleepTimerManager.formatRemaining(), color = LiveRed, fontSize = 14.sp)
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                if (isActive) {
                    // Actieve timer — toon cancel
                    Text("Timer loopt…", color = SecondaryText, fontSize = 14.sp)
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            sleepTimerManager.cancel()
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Timer annuleren", color = PrimaryText)
                    }
                } else if (showCustom) {
                    // Custom tijd invoer
                    OutlinedTextField(
                        value = customMinutes,
                        onValueChange = { if (it.all { c -> c.isDigit() } && it.length <= 4) customMinutes = it },
                        label = { Text("Aantal minuten", color = SecondaryText) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = fieldColors(),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { showCustom = false }, shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f)) {
                            Text("Terug", color = SecondaryText)
                        }
                        Button(
                            onClick = {
                                val mins = customMinutes.toIntOrNull() ?: 0
                                if (mins > 0) {
                                    sleepTimerManager.start(mins) { onStop() }
                                    onDismiss()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Purple),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            enabled = (customMinutes.toIntOrNull() ?: 0) > 0
                        ) {
                            Text("Start")
                        }
                    }
                } else {
                    // Presets
                    val presets = listOf(
                        "10 min" to 10, "20 min" to 20, "30 min" to 30, "45 min" to 45,
                        "1 uur" to 60, "1,5 uur" to 90, "2 uur" to 120, "3 uur" to 180,
                        "4 uur" to 240, "6 uur" to 360, "8 uur" to 480, "12 uur" to 720
                    )
                    presets.forEach { (label, mins) ->
                        TextButton(
                            onClick = {
                                sleepTimerManager.start(mins) { onStop() }
                                onDismiss()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(label, color = PrimaryText, fontSize = 16.sp)
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    TextButton(onClick = { showCustom = true }, modifier = Modifier.fillMaxWidth()) {
                        Text("⚙️ Aangepaste tijd…", color = Purple, fontSize = 15.sp)
                    }
                }
            }
        },
        confirmButton = {
            if (!isActive && !showCustom) {
                TextButton(onClick = onDismiss) { Text("Annuleren", color = SecondaryText) }
            }
        }
    )
}

@Composable
fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Card, unfocusedContainerColor = Card,
    focusedTextColor = PrimaryText, unfocusedTextColor = PrimaryText,
    cursorColor = Purple, focusedBorderColor = Purple, unfocusedBorderColor = GlassBorder,
    focusedLabelColor = Purple, unfocusedLabelColor = SecondaryText
)

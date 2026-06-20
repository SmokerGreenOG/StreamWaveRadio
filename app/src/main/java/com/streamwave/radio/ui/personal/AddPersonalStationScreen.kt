package com.streamwave.radio.ui.personal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.streamwave.radio.core.theme.*
import com.streamwave.radio.ui.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPersonalStationScreen(
    onBack: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var streamUrl by remember { mutableStateOf("") }
    var websiteUrl by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var testResult by remember { mutableStateOf("") }
    var testing by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text("Persoonlijk station toevoegen", color = PrimaryText) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Terug", tint = PrimaryText) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name, onValueChange = { name = it },
                label = { Text("Stationsnaam") },
                colors = fieldColors(), shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = streamUrl, onValueChange = { streamUrl = it },
                label = { Text("Directe stream-URL") },
                placeholder = { Text("https://...mp3 of .m3u8") },
                colors = fieldColors(), shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = websiteUrl, onValueChange = { websiteUrl = it },
                label = { Text("Website (optioneel)") },
                colors = fieldColors(), shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description, onValueChange = { description = it },
                label = { Text("Beschrijving (optioneel)") },
                colors = fieldColors(), shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // Test result
            if (testResult.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (testResult.contains("werkt")) ElevatedCard else Card
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        testResult,
                        color = if (testResult.contains("werkt")) SuccessGreen else Pink,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = {
                        testing = true
                        testResult = "Testen…"
                    },
                    shape = RoundedCornerShape(12.dp),
                    enabled = streamUrl.isNotBlank() && !testing
                ) {
                    Text(if (testing) "Testen…" else "Stream testen", color = Purple)
                }
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = {
                        if (name.isNotBlank() && streamUrl.isNotBlank()) {
                            // Maak een nieuw persoonlijk station en speel het af
                            homeViewModel.radioPlayer.play(streamUrl, name)
                            onBack()
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Purple),
                    enabled = name.isNotBlank() && streamUrl.isNotBlank()
                ) {
                    Text("Opslaan & afspelen")
                }
            }
        }
    }
}

@Composable
fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Card, unfocusedContainerColor = Card,
    focusedTextColor = PrimaryText, unfocusedTextColor = PrimaryText,
    cursorColor = Purple, focusedBorderColor = Purple, unfocusedBorderColor = GlassBorder,
    focusedLabelColor = Purple, unfocusedLabelColor = SecondaryText
)

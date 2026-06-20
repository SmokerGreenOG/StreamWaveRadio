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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.streamwave.radio.R
import com.streamwave.radio.core.theme.*
import com.streamwave.radio.ui.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPersonalStationScreen(onBack: () -> Unit, homeViewModel: HomeViewModel = hiltViewModel()) {
    var name by remember { mutableStateOf("") }
    var streamUrl by remember { mutableStateOf("") }
    var websiteUrl by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var testing by remember { mutableStateOf(false) }

    Scaffold(containerColor = Background,
        topBar = { TopAppBar(title = { Text(stringResource(R.string.add_station), color = PrimaryText) },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back), tint = PrimaryText) } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text(stringResource(R.string.station_name)) }, colors = fdColors(), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = streamUrl, onValueChange = { streamUrl = it }, label = { Text(stringResource(R.string.stream_url)) }, placeholder = { Text("https://...mp3") }, colors = fdColors(), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = websiteUrl, onValueChange = { websiteUrl = it }, label = { Text(stringResource(R.string.website_url)) }, colors = fdColors(), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text(stringResource(R.string.description)) }, colors = fdColors(), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth(), minLines = 3)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { testing = true }, shape = RoundedCornerShape(12.dp), enabled = streamUrl.isNotBlank() && !testing) {
                    Text(if (testing) stringResource(R.string.testing) else stringResource(R.string.test_stream), color = Purple) }
                Spacer(Modifier.weight(1f))
                Button(onClick = { if (name.isNotBlank() && streamUrl.isNotBlank()) { homeViewModel.radioPlayer.play(streamUrl, name); onBack() } },
                    shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Purple), enabled = name.isNotBlank() && streamUrl.isNotBlank()) {
                    Text(stringResource(R.string.save_play)) }
            }
        }
    }
}

@Composable fun fdColors() = OutlinedTextFieldDefaults.colors(focusedContainerColor = Card, unfocusedContainerColor = Card, focusedTextColor = PrimaryText, unfocusedTextColor = PrimaryText, cursorColor = Purple, focusedBorderColor = Purple, unfocusedBorderColor = GlassBorder, focusedLabelColor = Purple, unfocusedLabelColor = SecondaryText)

package com.streamwave.radio.ui.personal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.streamwave.radio.R
import com.streamwave.radio.core.theme.*
import com.streamwave.radio.data.database.entity.PersonalStationEntity
import com.streamwave.radio.data.repository.PersonalStationRepository
import com.streamwave.radio.ui.home.HomeViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPersonalStationScreen(onBack: () -> Unit, homeViewModel: HomeViewModel = hiltViewModel()) {
    var name by remember { mutableStateOf("") }
    var streamUrl by remember { mutableStateOf("") }
    var websiteUrl by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var testing by remember { mutableStateOf(false) }
    var testResult by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

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

            if (testResult != null) {
                Text(testResult!!, color = if (testResult!!.startsWith("✅")) SuccessGreen else ErrorRed, fontSize = 13.sp)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = {
                    testing = true; testResult = null
                    scope.launch {
                        testResult = try {
                            val url = java.net.URL(streamUrl)
                            val conn = url.openConnection() as java.net.HttpURLConnection
                            conn.connectTimeout = 5000; conn.readTimeout = 5000
                            conn.setRequestProperty("User-Agent", "StreamWave Radio")
                            conn.connect()
                            val type = conn.contentType ?: ""
                            conn.disconnect()
                            if (type.contains("audio") || type.contains("mpeg") || type.contains("ogg") || type.contains("aac") || type.contains("x-mpegurl"))
                                "✅ Stream werkt!"
                            else "⚠️ Geen audio-stream ($type)"
                        } catch (e: Exception) {
                            "❌ ${e.message?.take(50) ?: "Niet bereikbaar"}"
                        }
                        testing = false
                    }
                }, shape = RoundedCornerShape(12.dp), enabled = streamUrl.isNotBlank() && !testing) {
                    Text(if (testing) stringResource(R.string.testing) else stringResource(R.string.test_stream), color = Purple)
                }
                Spacer(Modifier.weight(1f))
                Button(onClick = {
                    if (name.isNotBlank() && streamUrl.isNotBlank()) {
                        scope.launch {
                            val dao = homeViewModel.personalStationDao()
                            dao.insert(PersonalStationEntity(
                                name = name, streamUrl = streamUrl, websiteUrl = websiteUrl,
                                description = description, categoryId = 15, lastPlayedAt = System.currentTimeMillis()
                            ))
                        }
                        homeViewModel.radioPlayer.play(streamUrl, name)
                        onBack()
                    }
                }, shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Purple), enabled = name.isNotBlank() && streamUrl.isNotBlank()) {
                    Text(stringResource(R.string.save_play))
                }
            }
        }
    }
}

@Composable fun fdColors() = OutlinedTextFieldDefaults.colors(focusedContainerColor = Card, unfocusedContainerColor = Card, focusedTextColor = PrimaryText, unfocusedTextColor = PrimaryText, cursorColor = Purple, focusedBorderColor = Purple, unfocusedBorderColor = GlassBorder, focusedLabelColor = Purple, unfocusedLabelColor = SecondaryText)

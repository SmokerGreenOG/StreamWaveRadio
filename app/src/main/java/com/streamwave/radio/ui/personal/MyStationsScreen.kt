package com.streamwave.radio.ui.personal

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.streamwave.radio.R
import com.streamwave.radio.core.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyStationsScreen(onBack: () -> Unit) {
    Scaffold(containerColor = Background,
        topBar = { TopAppBar(title = { Text(stringResource(R.string.my_stations), color = PrimaryText) },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back), tint = PrimaryText) } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)) }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Radio, null, tint = Purple, modifier = Modifier.size(48.dp))
                Spacer(Modifier.height(16.dp))
                Text(stringResource(R.string.no_stations), color = SecondaryText)
            }
        }
    }
}

package com.streamwave.radio.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
fun AdminScreen(onBack: () -> Unit) {
    Scaffold(containerColor = Background,
        topBar = { TopAppBar(title = { Text(stringResource(R.string.admin_login), color = PrimaryText) },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back), tint = PrimaryText) } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AdminCard(Icons.Default.Dashboard, stringResource(R.string.admin_dashboard), "")
            AdminCard(Icons.Default.Radio, stringResource(R.string.admin_stations), "")
            AdminCard(Icons.Default.Add, stringResource(R.string.admin_add), "")
            AdminCard(Icons.Default.Assessment, stringResource(R.string.admin_submissions), "")
            Spacer(Modifier.height(24.dp))
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { Text(stringResource(R.string.admin_secured), color = SecondaryText) }
        }
    }
}

@Composable
fun AdminCard(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String) {
    Surface(onClick = {}, color = Card, shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = Purple)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) { Text(title, color = PrimaryText) }
            Icon(Icons.Default.ChevronRight, null, tint = SecondaryText)
        }
    }
}

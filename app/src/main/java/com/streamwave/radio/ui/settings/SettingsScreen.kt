package com.streamwave.radio.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.streamwave.radio.core.common.Constants
import com.streamwave.radio.core.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onOpenAdmin: () -> Unit,
    onOpenMyStations: () -> Unit
) {
    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text("Instellingen", color = PrimaryText) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Terug", tint = PrimaryText) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            // Language
            SettingsRow(icon = Icons.Default.Language, title = "Taal", subtitle = "Nederlands", onClick = {})

            // My Stations
            SettingsRow(icon = Icons.Default.Radio, title = "Mijn stations", subtitle = "Persoonlijke radiostations", onClick = onOpenMyStations)

            // Account
            SettingsRow(icon = Icons.Default.AccountCircle, title = "Account", subtitle = "Optioneel — synchronisatie", onClick = {})

            // Admin
            SettingsRow(icon = Icons.Default.AdminPanelSettings, title = "Beheerder", subtitle = "Admin paneel", onClick = onOpenAdmin)

            Divider(color = GlassBorder, modifier = Modifier.padding(vertical = 12.dp))

            // About
            Text("Over", color = LightPurple)
            Spacer(Modifier.height(8.dp))
            Text(Constants.APP_NAME, color = PrimaryText)
            Text("Versie ${Constants.VERSION}", color = SecondaryText)
            Text("Gemaakt door ${Constants.MAKER}", color = SecondaryText)
        }
    }
}

@Composable
fun SettingsRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = Card,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Icon(icon, null, tint = Purple)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = PrimaryText)
                Text(subtitle, color = SecondaryText, fontSize = 12.sp)
            }
            Icon(Icons.Default.ChevronRight, null, tint = SecondaryText)
        }
    }
}

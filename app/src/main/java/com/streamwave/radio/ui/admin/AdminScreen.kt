package com.streamwave.radio.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.streamwave.radio.core.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(onBack: () -> Unit) {
    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text("Beheerder", color = PrimaryText) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Terug", tint = PrimaryText) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AdminCard(Icons.Default.Dashboard, "Dashboard", "Stream statussen en overzicht")
            AdminCard(Icons.Default.Radio, "Stations beheren", "Toevoegen, bewerken, verwijderen")
            AdminCard(Icons.Default.Add, "Station toevoegen", "Nieuw officieel station")
            AdminCard(Icons.Default.Assessment, "Inzendingen", "Beoordeel ingestuurde stations")
            AdminCard(Icons.Default.Category, "Categorieën", "Beheer categorieën")

            Spacer(Modifier.height(24.dp))
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("🔒 Beveiligd adminpaneel — alleen voor beheerder", color = SecondaryText)
            }
        }
    }
}

@Composable
fun AdminCard(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String) {
    Surface(
        onClick = {},
        color = Card,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = Purple)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = PrimaryText)
                Text(subtitle, color = SecondaryText)
            }
            Icon(Icons.Default.ChevronRight, null, tint = SecondaryText)
        }
    }
}

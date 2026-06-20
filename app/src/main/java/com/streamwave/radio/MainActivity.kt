package com.streamwave.radio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.streamwave.radio.core.theme.Background
import com.streamwave.radio.core.theme.PrimaryText
import com.streamwave.radio.core.theme.StreamWaveTheme
import com.streamwave.radio.core.common.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StreamWaveTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Background),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = Constants.APP_NAME,
                        color = PrimaryText,
                        style = androidx.compose.material3.MaterialTheme.typography.displayLarge
                    )
                }
            }
        }
    }
}

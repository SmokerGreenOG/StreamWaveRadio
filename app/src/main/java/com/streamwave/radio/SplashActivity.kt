package com.streamwave.radio

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.streamwave.radio.core.localization.LanguageManager
import com.streamwave.radio.core.theme.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {

    @Inject lateinit var languageManager: LanguageManager

    override fun attachBaseContext(newBase: Context) {
        val lang = newBase.let { ctx ->
            try {
                val app = ctx.applicationContext as StreamWaveApp
                app.languageManager.getInitialLanguage()
            } catch (e: Exception) {
                "en"
            }
        }
        val locale = LanguageManager.getLocaleForCode(lang)
        super.attachBaseContext(LanguageManager.wrapContextWithLocale(newBase, locale))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StreamWaveTheme {
                SplashScreen {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.5f) }
    LaunchedEffect(Unit) {
        alpha.animateTo(1f, animationSpec = tween(800, easing = FastOutSlowInEasing))
        scale.animateTo(1f, animationSpec = tween(800, easing = FastOutSlowInEasing))
        kotlinx.coroutines.delay(1200)
        onFinished()
    }
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Background, Card))), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            androidx.compose.foundation.Image(painter = painterResource(R.drawable.app_logo), contentDescription = null,
                modifier = Modifier.size(200.dp).scale(scale.value).alpha(alpha.value), contentScale = ContentScale.Fit)
            Spacer(Modifier.height(16.dp))
            Text("StreamWave Radio", color = PrimaryText, fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier.alpha(alpha.value))
            Spacer(Modifier.height(4.dp))
            Text("Your radio, your stream", color = Purple, fontSize = 14.sp, modifier = Modifier.alpha(alpha.value))
            Spacer(Modifier.height(24.dp))
            Text("Powered by Futuristic Creations", color = SecondaryText, fontSize = 11.sp, modifier = Modifier.alpha(alpha.value))
        }
    }
}

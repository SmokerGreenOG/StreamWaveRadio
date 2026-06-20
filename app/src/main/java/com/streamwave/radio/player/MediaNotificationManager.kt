package com.streamwave.radio.player

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.media3.common.Player
import com.streamwave.radio.MainActivity
import com.streamwave.radio.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val CHANNEL_ID = "streamwave_playback"
        const val NOTIFICATION_ID = 1001
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Radioweergave",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Toont het huidige radiostation en bediening"
                setShowBadge(false)
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    fun buildNotification(
        stationName: String,
        stationLogo: String?,
        artist: String,
        title: String,
        isPlaying: Boolean,
        onPlayPause: () -> Unit,
        onStop: () -> Unit,
        onPrevious: () -> Unit,
        onNext: () -> Unit
    ): android.app.Notification {
        val openIntent = PendingIntent.getActivity(
            context, 0,
            Intent(context, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_SINGLE_TOP },
            if (Build.VERSION.SDK_INT >= 31) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val playPauseIcon = if (isPlaying) android.R.drawable.ic_media_pause
                           else android.R.drawable.ic_media_play

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(stationName)
            .setContentText(if (title.isNotEmpty()) "$artist — $title" else "LIVE")
            .setOngoing(isPlaying)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(openIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(android.R.drawable.ic_media_previous, "Vorige", null) // Placeholder
            .addAction(playPauseIcon, if (isPlaying) "Pauze" else "Play", null)
            .addAction(android.R.drawable.ic_media_next, "Volgende", null)
            .addAction(android.R.drawable.ic_media_play, "Stop", null)

        return builder.build()
    }
}

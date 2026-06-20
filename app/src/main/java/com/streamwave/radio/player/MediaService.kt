package com.streamwave.radio.player

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.streamwave.radio.MainActivity
import com.streamwave.radio.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MediaService : MediaSessionService() {

    @Inject lateinit var radioPlayer: RadioPlayer

    private var mediaSession: MediaSession? = null

    companion object {
        const val CHANNEL_ID = "streamwave_radio_playback"
        const val NOTIFICATION_ID = 1001
    }

    override fun onCreate() {
        super.onCreate()

        // Notification channel voor lockscreen controls
        val channel = android.app.NotificationChannel(
            CHANNEL_ID,
            "Radio Playback",
            android.app.NotificationManager.IMPORTANCE_LOW
        ).apply { description = "StreamWave Radio playback controls" }
        val nm = getSystemService(android.app.NotificationManager::class.java)
        nm.createNotificationChannel(channel)

        val player = radioPlayer.getExoPlayer()

        val intent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_SINGLE_TOP },
            if (Build.VERSION.SDK_INT >= 31) PendingIntent.FLAG_IMMUTABLE else 0
        )

        mediaSession = MediaSession.Builder(this, player)
            .setId("streamwave_radio_session")
            .setSessionActivity(intent)
            .build()

        // Bouw notificatie voor lockscreen
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.app_logo_small)
            .setContentTitle("StreamWave Radio")
            .setContentText("Actief — swipe voor controls")
            .setContentIntent(intent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = mediaSession

    override fun onTaskRemoved(rootIntent: Intent?) {
        if (mediaSession?.player?.playWhenReady != true) stopSelf()
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        super.onDestroy()
    }
}

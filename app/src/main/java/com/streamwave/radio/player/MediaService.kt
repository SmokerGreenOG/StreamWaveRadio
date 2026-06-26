package com.streamwave.radio.player

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
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
        var instance: MediaService? = null
            private set
    }

    fun getPlayer(): Player? = mediaSession?.player
    fun stopPlayback() {
        mediaSession?.player?.stop()
        radioPlayer.stop()
        stopSelf()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Notification channel
        val channel = android.app.NotificationChannel(
            CHANNEL_ID,
            "Radio Playback",
            android.app.NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "StreamWave Radio playback controls"
            setShowBadge(false)
        }
        val nm = getSystemService(android.app.NotificationManager::class.java)
        nm.createNotificationChannel(channel)

        val player = radioPlayer.getExoPlayer()

        val openIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_SINGLE_TOP },
            if (Build.VERSION.SDK_INT >= 31) PendingIntent.FLAG_IMMUTABLE else 0
        )

        mediaSession = MediaSession.Builder(this, player)
            .setId("streamwave_radio_session")
            .setSessionActivity(openIntent)
            .build()

        // Direct startForeground (binnen 5 sec verplicht!)
        // onUpdateNotification updatet 'm later met de volledige info
        val initNotification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.app_logo_small)
            .setContentTitle("StreamWave Radio")
            .setContentText("Gereed om te streamen")
            .setContentIntent(openIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
            .setOngoing(false)
            .build()
        startForeground(NOTIFICATION_ID, initNotification)
    }

    // Media3 roept dit aan wanneer de notificatie getoond/geüpdatet moet worden.
    // Wij bouwen onze eigen notificatie met logo, controls en station info.
    override fun onUpdateNotification(session: MediaSession, startInForegroundRequired: Boolean) {
        val player = session.player
        val state = radioPlayer.playerState.value
        val isPlaying = player.playWhenReady && player.playbackState == Player.STATE_READY

        val largeIcon = android.graphics.BitmapFactory.decodeResource(resources, R.drawable.app_logo)

        val intentFlags = if (Build.VERSION.SDK_INT >= 31)
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        else PendingIntent.FLAG_UPDATE_CURRENT

        val openIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_SINGLE_TOP },
            if (Build.VERSION.SDK_INT >= 31) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val playPauseIntent = PendingIntent.getBroadcast(
            this, 101,
            Intent(NotificationActionReceiver.ACTION_PLAY_PAUSE).apply { setPackage(packageName) },
            intentFlags
        )

        val stopIntent = PendingIntent.getBroadcast(
            this, 102,
            Intent(NotificationActionReceiver.ACTION_STOP).apply { setPackage(packageName) },
            intentFlags
        )

        val playPauseIcon = if (isPlaying) android.R.drawable.ic_media_pause
                           else android.R.drawable.ic_media_play

        // Titel = stationsnaam (uit StateFlow) of MediaItem metadata
        val title = if (state.stationName.isNotEmpty()) state.stationName
                    else player.currentMediaItem?.mediaMetadata?.title?.toString()
                    ?: "StreamWave Radio"

        // Subtitle: track info of LIVE
        val subtitle: String = when {
            state.title.isNotEmpty() && state.title != "Unknown" && state.title != "Onbekend" -> {
                if (state.artist.isNotEmpty() && state.artist != "Unknown" && state.artist != "Onbekend")
                    "${state.artist} — ${state.title}"
                else state.title
            }
            isPlaying -> "🔴 LIVE"
            state.state == PlayingState.PAUSED -> "⏸ Gepauzeerd"
            state.state == PlayingState.BUFFERING -> "⏳ Bufferen..."
            else -> "Gereed"
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.app_logo_small)
            .setLargeIcon(largeIcon)
            .setContentTitle(title)
            .setContentText(subtitle)
            .setContentIntent(openIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(isPlaying)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
            .addAction(playPauseIcon, if (isPlaying) "Pauze" else "Play", playPauseIntent)
            .addAction(android.R.drawable.ic_delete, "Stop", stopIntent)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = mediaSession

    override fun onTaskRemoved(rootIntent: Intent?) {
        if (mediaSession?.player?.playWhenReady != true) stopSelf()
    }

    override fun onDestroy() {
        instance = null
        mediaSession?.run {
            player.stop()
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}

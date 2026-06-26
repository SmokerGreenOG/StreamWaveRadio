package com.streamwave.radio.player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.media3.common.Player

class NotificationActionReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_PLAY_PAUSE = "com.streamwave.radio.action.PLAY_PAUSE"
        const val ACTION_STOP = "com.streamwave.radio.action.STOP"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // Haal de MediaService op via een statische reference,
        // want receiver kan niet injecten via Hilt
        val service = MediaService.instance ?: return

        when (intent.action) {
            ACTION_PLAY_PAUSE -> {
                val player = service.getPlayer()
                if (player != null) {
                    if (player.playWhenReady) player.pause()
                    else player.play()
                }
            }
            ACTION_STOP -> {
                service.stopPlayback()
            }
        }
    }
}

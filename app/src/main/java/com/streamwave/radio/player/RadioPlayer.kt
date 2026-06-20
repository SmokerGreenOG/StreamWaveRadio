package com.streamwave.radio.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

enum class PlayingState { IDLE, BUFFERING, PLAYING, PAUSED, ERROR }

data class PlayerState(
    val state: PlayingState = PlayingState.IDLE,
    val stationName: String = "",
    val stationLogo: String = "",
    val artist: String = "",
    val title: String = "",
    val albumArt: String = "",
    val volume: Float = 1f,
    val isMuted: Boolean = false,
    val isLive: Boolean = true,
    val sleepTimerRemaining: Long = 0L,
    val error: String? = null
)

@Singleton
class RadioPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(context)
        .setMediaSourceFactory(DefaultMediaSourceFactory(context))
        .build()

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private var retryCount = 0
    private val maxRetries = 5

    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                val mapped = when (state) {
                    Player.STATE_BUFFERING -> PlayingState.BUFFERING
                    Player.STATE_READY -> if (exoPlayer.playWhenReady) PlayingState.PLAYING else PlayingState.PAUSED
                    Player.STATE_ENDED -> PlayingState.IDLE
                    Player.STATE_IDLE -> PlayingState.IDLE
                    else -> PlayingState.IDLE
                }
                _playerState.value = _playerState.value.copy(state = mapped)
            }

            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                if (retryCount < maxRetries) {
                    retryCount++
                    val delay = 2000L * retryCount
                    exoPlayer.playWhenReady = true
                } else {
                    _playerState.value = _playerState.value.copy(
                        state = PlayingState.ERROR,
                        error = error.localizedMessage ?: "Stream cannot be played"
                    )
                }
            }

            override fun onMediaMetadataChanged(metadata: MediaMetadata) {
                val artist = metadata.artist?.toString() ?: ""
                val title = metadata.title?.toString() ?: ""
                val albumArt = metadata.artworkUri?.toString() ?: ""
                if (artist.isNotEmpty() || title.isNotEmpty()) {
                    _playerState.value = _playerState.value.copy(
                        artist = artist,
                        title = title,
                        albumArt = albumArt
                    )
                }
            }
        })
    }

    fun play(streamUrl: String, stationName: String, stationLogo: String = "") {
        retryCount = 0
        _playerState.value = _playerState.value.copy(
            stationName = stationName,
            stationLogo = stationLogo,
            artist = "",
            title = "",
            albumArt = "",
            error = null,
            state = PlayingState.BUFFERING
        )
        val mediaItem = MediaItem.Builder()
            .setUri(streamUrl)
            .setMediaId(stationName)
            .build()
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    fun pause() {
        exoPlayer.playWhenReady = false
        _playerState.value = _playerState.value.copy(state = PlayingState.PAUSED)
    }

    fun resume() {
        exoPlayer.playWhenReady = true
    }

    fun stop() {
        exoPlayer.stop()
        exoPlayer.clearMediaItems()
        _playerState.value = PlayerState()
    }

    fun setVolume(volume: Float) {
        exoPlayer.volume = volume.coerceIn(0f, 1f)
        _playerState.value = _playerState.value.copy(
            volume = volume.coerceIn(0f, 1f),
            isMuted = volume == 0f
        )
    }

    fun mute() {
        exoPlayer.volume = 0f
        _playerState.value = _playerState.value.copy(isMuted = true, volume = 0f)
    }

    fun unmute() {
        exoPlayer.volume = _playerState.value.volume.coerceAtLeast(0.1f)
        _playerState.value = _playerState.value.copy(isMuted = false)
    }

    fun getExoPlayer(): ExoPlayer = exoPlayer

    fun release() {
        exoPlayer.release()
    }
}

package com.streamwave.radio.core.common

object Constants {
    const val APP_NAME = "StreamWave Radio"
    const val MAKER = "SmokerGreenOG"
    const val VERSION = "1.1.0"

    // DataStore
    const val PREF_NAME = "streamwave_settings"

    // Stream timeout
    const val STREAM_CONNECT_TIMEOUT_MS = 10_000L
    const val STREAM_READ_TIMEOUT_MS = 15_000L

    // Retry
    const val MAX_RECONNECT_ATTEMPTS = 5
    const val RECONNECT_BASE_DELAY_MS = 2_000L

    // Cache
    const val CACHE_TTL_HOURS = 24
}

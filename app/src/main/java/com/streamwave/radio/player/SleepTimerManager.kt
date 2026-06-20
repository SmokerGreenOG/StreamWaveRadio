package com.streamwave.radio.player

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SleepTimerManager @Inject constructor() {
    private val _remainingSeconds = MutableStateFlow(0L)
    val remainingSeconds: StateFlow<Long> = _remainingSeconds.asStateFlow()

    private val _isActive = MutableStateFlow(false)
    val isActive: StateFlow<Boolean> = _isActive.asStateFlow()

    private var job: Job? = null

    fun start(minutes: Int, onFinish: () -> Unit) {
        cancel()
        var seconds = minutes * 60L
        _remainingSeconds.value = seconds
        _isActive.value = true

        job = CoroutineScope(Dispatchers.Default).launch {
            while (seconds > 0 && isActive) {
                delay(1000L)
                seconds--
                _remainingSeconds.value = seconds
            }
            if (seconds <= 0) {
                _isActive.value = false
                _remainingSeconds.value = 0
                withContext(Dispatchers.Main) { onFinish() }
            }
        }
    }

    fun cancel() {
        job?.cancel()
        job = null
        _remainingSeconds.value = 0
        _isActive.value = false
    }

    fun formatRemaining(): String {
        val total = _remainingSeconds.value
        val hours = total / 3600
        val minutes = (total % 3600) / 60
        return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
    }
}

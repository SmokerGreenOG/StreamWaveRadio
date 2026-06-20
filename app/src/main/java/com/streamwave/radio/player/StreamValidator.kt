package com.streamwave.radio.player

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton

data class StreamTestResult(
    val isValid: Boolean,
    val isAudio: Boolean,
    val message: String,
    val contentType: String = "",
    val bitrate: String = "",
    val metadata: String = ""
)

@Singleton
class StreamValidator @Inject constructor() {

    suspend fun testStream(url: String): StreamTestResult = withContext(Dispatchers.IO) {
        // Validate URL
        if (url.isBlank()) {
            return@withContext StreamTestResult(false, false, "De URL is ongeldig.")
        }

        val lowerUrl = url.lowercase()
        if (!lowerUrl.startsWith("http://") && !lowerUrl.startsWith("https://")) {
            return@withContext StreamTestResult(false, false, "De URL is ongeldig. Alleen http en https zijn toegestaan.")
        }

        // Check for dangerous schemes
        val dangerousSchemes = listOf("file:", "ftp:", "javascript:", "data:")
        if (dangerousSchemes.any { lowerUrl.startsWith(it) }) {
            return@withContext StreamTestResult(false, false, "De URL bevat een niet-toegestaan schema.")
        }

        try {
            val uri = URI(url)
            val connection = uri.toURL().openConnection() as HttpURLConnection
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.setRequestProperty("User-Agent", "StreamWaveRadio/1.0")
            connection.setRequestProperty("Icy-MetaData", "1")
            connection.instanceFollowRedirects = true

            val code = connection.responseCode
            val contentType = connection.contentType ?: ""
            val lowerContentType = contentType.lowercase()

            // Check if it's an audio content type
            val audioTypes = listOf("audio/", "application/ogg", "application/vnd.apple.mpegurl", "video/mp2t")
            val isAudio = audioTypes.any { lowerContentType.contains(it) } || code == 200

            val icyName = connection.getHeaderField("icy-name") ?: ""
            val icyGenre = connection.getHeaderField("icy-genre") ?: ""
            val icyBr = connection.getHeaderField("icy-br") ?: ""

            val metadata = buildString {
                if (icyName.isNotEmpty()) appendLine("Station: $icyName")
                if (icyGenre.isNotEmpty()) appendLine("Genre: $icyGenre")
            }

            connection.disconnect()

            when {
                code == 200 && isAudio -> StreamTestResult(
                    isValid = true, isAudio = true,
                    message = "De stream werkt.",
                    contentType = contentType,
                    bitrate = icyBr,
                    metadata = metadata
                )
                code == 200 && !isAudio -> StreamTestResult(
                    isValid = false, isAudio = false,
                    message = "Deze link is geen directe audiostream. Controleer of je een directe stream-URL hebt (MP3, AAC, M3U8, etc.)."
                )
                code in 400..499 -> StreamTestResult(
                    isValid = false, isAudio = false,
                    message = "De server heeft de verbinding geweigerd (HTTP $code)."
                )
                code in 500..599 -> StreamTestResult(
                    isValid = false, isAudio = false,
                    message = "De streamserver reageert niet correct (HTTP $code)."
                )
                else -> StreamTestResult(
                    isValid = false, isAudio = isAudio,
                    message = "De stream is momenteel offline of niet bereikbaar (HTTP $code)."
                )
            }
        } catch (e: java.net.UnknownHostException) {
            StreamTestResult(false, false, "De server is niet bereikbaar. Controleer de URL.")
        } catch (e: java.net.SocketTimeoutException) {
            StreamTestResult(false, false, "De streamserver reageert niet (timeout).")
        } catch (e: Exception) {
            StreamTestResult(false, false, "De stream werkt niet: ${e.localizedMessage ?: "onbekende fout"}")
        }
    }
}

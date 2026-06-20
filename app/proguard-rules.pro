# Proguard rules for StreamWave Radio

# Keep Retrofit + Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.streamwave.radio.network.api.** { *; }
-keep class com.streamwave.radio.data.model.** { *; }

# Keep Room entities
-keep class com.streamwave.radio.data.database.entity.** { *; }

# Hilt
-dontwarn dagger.hilt.**
-dontwarn javax.annotation.**
-dontwarn com.google.common.**

# OkHttp / OkIO
-dontwarn okhttp3.**
-dontwarn okio.**

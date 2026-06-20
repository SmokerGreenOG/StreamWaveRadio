# Proguard rules for StreamWave Radio
# Keep Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.streamwave.radio.network.api.** { *; }
-keep class com.streamwave.radio.data.model.** { *; }

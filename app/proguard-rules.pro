# Add rules here for release builds
-keepattributes *Annotation*
-keep class com.mindshield.app.data.local.entities.** { *; }
-keep class com.mindshield.app.domain.model.** { *; }

# RevenueCat
-keep class com.revenuecat.purchases.** { *; }

# Gemini AI
-keep class com.google.ai.client.generativeai.** { *; }

# Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.json.** { kotlinx.serialization.KSerializer serializer(...); }

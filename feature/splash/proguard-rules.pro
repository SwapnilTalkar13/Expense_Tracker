# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep Lottie classes
-keep class com.airbnb.lottie.** { *; }

# Keep Hilt generated classes
-keep class * extends dagger.hilt.internal.GeneratedComponent
-keep class **_HiltModules_*
-keep class **_Hilt_** { *; }
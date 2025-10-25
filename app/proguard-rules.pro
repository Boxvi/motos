#########################################################
# 📦 PROGUARD RULES PARA FIREBASE + GOOGLE PLAY SERVICES
#########################################################

# --- Mantener clases base de Firebase ---
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# --- Firestore (si usas Cloud Firestore) ---
-keep class com.google.firestore.** { *; }
-dontwarn com.google.firestore.**

# --- Google Play Services (Auth, Analytics, etc.) ---
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# --- Mantener anotaciones necesarias para reflexión ---
-keepattributes Signature
-keepattributes *Annotation*

# --- Evitar eliminar clases del modelo (POJOs usados en Firestore) ---
-keepclassmembers class * {
    @com.google.firebase.firestore.PropertyName <fields>;
}

# --- Mantener los miembros públicos de tus data classes de Firestore ---
-keepclassmembers class com.boxvisoft.motos.model.** {
    public <fields>;
    public <methods>;
}

# --- Si usas Firebase Auth (mantener clases necesarias) ---
-keep class com.google.firebase.auth.** { *; }
-dontwarn com.google.firebase.auth.**

# --- Si usas Firebase Analytics o Messaging ---
-keep class com.google.firebase.analytics.** { *; }
-keep class com.google.firebase.messaging.** { *; }

# --- Mantener las clases de inicialización ---
-keep class com.google.firebase.provider.FirebaseInitProvider { *; }

# --- Evitar remover el contenido de tu Application (si inicializas Firebase ahí) ---
-keep class **.MyApplication { *; }

# --- Opcional: mantener logs y nombres para depuración básica ---
-keepattributes SourceFile,LineNumberTable

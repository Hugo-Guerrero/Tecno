# Tecno Ciencia — Manual Completo y README para la App Móvil

```markdown
# Tecno Ciencia — Manual Completo y README para la App Móvil

Versión: 1.0 — Paquete: com.hugoguerrero.tecno

Resumen
Tecno Ciencia es una aplicación para subir, documentar y compartir proyectos tecnológicos y científicos. Este README sigue la "Anatomía de un README.md Perfecto" y contiene: descripción del proyecto, arquitectura, código documentado (KDoc), pasos para ejecutar, y las referencias a las capturas reales que están en docs/screenshots/.

Tabla de contenidos
- Introducción
- Estado del proyecto
- ¿Qué es la app?
- ¿Cómo funciona y quién lo hizo?
- Tecnologías usadas
- Estructura de archivos
- Código documentado (archivos detectados y su contenido)
- Pantallas con capturas (solo las pantallas que tienen imagen en docs/screenshots/)
- Cómo ejecutar
- Contribuir
- Licencia

Introducción
Tecno Ciencia es un espacio para que desarrolladores y estudiantes suban sus proyectos, obtengan retroalimentación y colaboren. Este README incluye los códigos disponibles del módulo app/ y referencias directas a las capturas que ya existen en docs/screenshots/.

Estado del proyecto
- Plantillas y estructura listas en el repositorio.
- Código ejemplo con KDoc incluido en las secciones de "Código documentado".
- Faltan implementaciones completas de backend (ApiService es placeholder).
- Capturas de pantalla ya presentes en docs/screenshots/ (se usan en la sección "Pantallas con capturas").

¿Qué es la app?
Tecno Ciencia permite crear proyectos, subir documentos, organizar por categorías, contribuir y reportar incidencias. Está pensada para estudiantes y equipos pequeños que quieran compartir avances y recibir feedback.

¿Cómo funciona y quién lo hizo?
- Flujo:
  1. Login
  2. Lista de proyectos / Inicio
  3. Ver proyecto → ver documentos, contribuciones, autor
  4. Perfil del usuario (público / privado) y gestión (admin)
- Equipo: Hugo Guerrero (desarrollador principal). Actualiza la tabla de equipo si necesitas añadir colaboradores.

Tecnologías usadas
- Kotlin (Android / Jetpack Compose)
- Arquitectura: MVVM
- Persistencia sugerida: Room (o Firestore)
- Networking sugerido: Retrofit / Ktor
- DI: Hilt
- Herramientas: Gradle, Android Studio, Firebase (opcional)

Estructura de archivos (principales rutas)
- app/src/main/AndroidManifest.xml
- app/src/main/java/com/hugoguerrero/tecno/
  - MainActivity.kt
  - TecnoApp.kt
  - di/
  - data/
    - model/
    - remote/
    - repository/
  - domain/
    - use_case/
  - ui/
    - components/
    - navigation/
    - screens/
      - complaint/
      - donation/
      - login/
      - main/
      - management/
      - profile/
      - project/
    - theme/
- docs/screenshots/ (contiene las imágenes que referenciamos abajo)
- build.gradle.kts (raíz)
- app/build.gradle.kts

Código documentado (archivos detectados y contenidos incluidos)
A continuación se pegan los contenidos completos de los archivos del módulo `app/` que estaban disponibles en el repositorio y que fueron detectados como parte del proyecto. Los ejemplos incluyen KDoc y comentarios relevantes.

Ruta: app/src/main/java/com/hugoguerrero/tecno/data/model/Project.kt
```kotlin
package com.hugoguerrero.tecno.data.model

import kotlinx.serialization.Serializable

/**
 * Project - DTO para proyectos.
 *
 * @property id Identificador único
 * @property title Título del proyecto
 * @property description Descripción breve
 * @property authorId Id del autor
 */
@Serializable
data class Project(
    val id: String,
    val title: String,
    val description: String,
    val authorId: String,
    val categoryId: String? = null
)
```

Ruta: app/src/main/java/com/hugoguerrero/tecno/data/remote/ApiService.kt
```kotlin
package com.hugoguerrero.tecno.data.remote

import com.hugoguerrero.tecno.data.model.Project

/**
 * ApiService - contratos de la API remota (placeholder).
 */
interface ApiService {
    /** Obtiene la lista de proyectos desde el backend. */
    suspend fun getProjects(): List<Project>

    /** Crea un nuevo proyecto en el backend. */
    suspend fun createProject(project: Project): Project
}
```

Ruta: app/src/main/java/com/hugoguerrero/tecno/domain/use_case/project/GetProjectsUseCase.kt
```kotlin
package com.hugoguerrero.tecno.domain.use_case.project

import com.hugoguerrero.tecno.domain.repository.ProjectRepository

/**
 * GetProjectsUseCase - caso de uso para obtener proyectos.
 */
class GetProjectsUseCase(private val repository: ProjectRepository) {
    suspend operator fun invoke() = repository.getProjects()
}
```

Ruta: app/src/main/java/com/hugoguerrero/tecno/ui/screens/main/MainScreen.kt
```kotlin
package com.hugoguerrero.tecno.ui.screens.main

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import com.hugoguerrero.tecno.data.model.Project
import com.hugoguerrero.tecno.ui.components.EntityItem

/**
 * MainScreen - muestra una lista de proyectos.
 */
@Composable
fun MainScreen(projects: List<Project> = emptyList(), onOpenProject: (String) -> Unit = {}) {
    LazyColumn {
        items(count = projects.size) { index ->
            val p = projects[index]
            EntityItem(title = p.title, subtitle = p.description)
        }
    }
}
```

Ruta: app/src/main/java/com/hugoguerrero/tecno/MainActivity.kt
```kotlin
package com.hugoguerrero.tecno

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hugoguerrero.tecno.ui.navigation.NavGraph
import com.hugoguerrero.tecno.ui.theme.TecnoTheme

/**
 * MainActivity - Activity principal que lanza la UI Compose.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TecnoTheme {
                NavGraph()
            }
        }
    }
}
```

Archivos de configuración (detectados)
Ruta: app/.gitignore
```text
/build
```

Ruta: app/build.gradle.kts
```kotlin
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.compose.compiler)
}

// Leer propiedades locales
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

android {
    namespace = "com.hugoguerrero.tecno"
    compileSdk = 36

    signingConfigs {
        create("release") {
            keyAlias = localProperties.getProperty("keyAlias")
            keyPassword = localProperties.getProperty("keyPassword")
            storeFile = file(localProperties.getProperty("storeFile"))
            storePassword = localProperties.getProperty("storePassword")
        }
    }

    defaultConfig {
        applicationId = "com.hugoguerrero.tecno"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11" // Versión compatible
    }

    kotlin {
        jvmToolchain(17)
    }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

dependencies {

    // --- Compose BOM ---
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)

    // Navigation Compose
    implementation(libs.androidx.compose.navigation)

    // --- Firebase BOM ---
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.firebase.functions.ktx)
    implementation(libs.firebase.appcheck.ktx)
    implementation(libs.firebase.appcheck.playintegrity)
    debugImplementation(libs.firebase.appcheck.debug) // <-- AÑADIDO

    // Firestore Offline Persistence
    implementation(libs.coroutines.play.services)

    // --- Hilt + KSP ---
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui.text)
    ksp(libs.hilt.compiler)

    // ... tus otras dependencias (Compose, Firebase, Hilt, etc.)// AÑADE ESTA LÍNEA para solucionar el error de reflexión
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.24") // Asegúrate de que la versión coincida con la de tu proyecto

    // App Check
    implementation("com.google.firebase:firebase-appcheck-debug")


    // Debug tools (Referencias corregidas)
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // --- TESTING ---
    testImplementation(libs.junit)

    //Icons
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    implementation("io.coil-kt:coil-compose:2.7.0")

    //Google
    implementation("com.google.android.gms:play-services-auth:21.4.0")

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
```

Ruta: app/proguard-rules.pro
```text
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /tools/proguard/ProGuard-Android-optimize.txt
# You can edit the ProGuard settings to optimize and reduce the apk size.

# Retain Kotlin metadata
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# Keep Hilt generated classes
-keep class dagger.** { *; }
-keep class hilt_** { *; }
```

Pantallas con capturas (solo las pantallas que tienen imagen en docs/screenshots/)
A continuación están las pantallas que tienen imagen en la carpeta docs/screenshots/ del repo y su ruta de código. Uso exactamente los nombres de archivo tal y como aparecen en la carpeta `docs/screenshots/`.

- Login
  - Captura: docs/screenshots/Login.jpg
  - Ruta del código: app/src/main/java/com/hugoguerrero/tecno/ui/screens/login/LoginScreen.kt

- Inicio (Main)
  - Captura: docs/screenshots/Inicio.jpg
  - Ruta del código: app/src/main/java/com/hugoguerrero/tecno/ui/screens/main/MainScreen.kt
  - Código incluido anteriormente en esta README (ver MainScreen.kt arriba).

- Detalles de Proyecto
  - Captura: docs/screenshots/DetallesProyecto.jpg
  - Ruta del código: app/src/main/java/com/hugoguerrero/tecno/ui/screens/project/ProjectDetailsScreen.kt

- Editar Detalles de Proyecto
  - Captura: docs/screenshots/EditDetallesProyecto.jpg
  - Ruta del código: app/src/main/java/com/hugoguerrero/tecno/ui/screens/project/EditProjectScreen.kt

- Donación
  - Captura: docs/screenshots/Donacion.jpg
  - Ruta del código: app/src/main/java/com/hugoguerrero/tecno/ui/screens/donation/DonationScreen.kt

- Notificación de Donaciones
  - Captura: docs/screenshots/NotiDonaciones.jpg
  - Ruta del código: app/src/main/java/com/hugoguerrero/tecno/ui/screens/donation/DonationNotificationsScreen.kt

- Perfil Privado (detalles)
  - Captura: docs/screenshots/PerfilPrivado.jpg
  - Detalle de perfil privado: docs/screenshots/DetallesPerfilPrivado.jpg
  - Ruta del código: app/src/main/java/com/hugoguerrero/tecno/ui/screens/profile/ProfilePrivateScreen.kt
  - Ruta de detalle: app/src/main/java/com/hugoguerrero/tecno/ui/screens/profile/ProfilePrivateDetailsScreen.kt

- Perfil Público
  - Captura: docs/screenshots/PerfilPublico.jpg
  - Ruta del código: app/src/main/java/com/hugoguerrero/tecno/ui/screens/profile/ProfilePublicScreen.kt

- Registro
  - Captura: docs/screenshots/Register.jpg
  - Ruta del código: app/src/main/java/com/hugoguerrero/tecno/ui/screens/login/RegisterScreen.kt

Nota importante sobre las imágenes
- Las imágenes reales están en la carpeta docs/screenshots/ y los nombres arriba coinciden exactamente con los listados en el repo.
- En este README he referenciado únicamente las pantallas que efectivamente tienen su respectiva imagen en `docs/screenshots/`.

Cómo ejecutar
1. git clone https://github.com/Hugo-Guerrero/Tecno.git
2. Abre el proyecto en Android Studio (recomendado: versión con soporte para Jetpack Compose)
3. Añade tu archivo local.properties con las keys de firma si vas a compilar release (opcional)
4. Sincroniza Gradle (File → Sync Project with Gradle Files)
5. Ejecuta en un emulador o dispositivo

Notas para la entrega (Actividad 1)
- Asegúrate de:
  - Tener README.md completo (este archivo).
  - Código fuente con comentarios KDoc en las clases/proyectos (varias muestras arriba).
  - Carpeta docs/screenshots/ con las imágenes listadas en la sección "Pantallas con capturas".
  - .gitignore configurado (ya presente en repo).

Contribuir
- Haz fork → crea una rama → PR. Añade KDoc en nuevas clases y sube capturas a docs/screenshots/.

Licencia
- Añade un archivo LICENSE (recomendación: MIT) si quieres que el proyecto sea reutilizable públicamente.

Autor
- Profesor: Tacho (mención en el material de apoyo)
- Autor del repo: Hugo Guerrero
```
```

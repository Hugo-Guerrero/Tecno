# Tecno Ciencia — Manual Completo y README para la App Móvil

Versión: 1.0 — Paquete: com.hugoguerrero.tecno

Resumen
Tecno Ciencia es una aplicación para subir, documentar y compartir proyectos tecnológicos y científicos. Este README sigue la "Anatomía de un README.md Perfecto" y contiene: descripción del proyecto, cómo funciona, tecnologías, estructura de archivos, ejemplos de código documentado (KDoc), espacio para capturas de pantalla y pasos para ejecutar.

Tabla de contenidos
- Introducción
- Estado del proyecto
- ¿Qué es la app?
- ¿Cómo funciona y quién lo hizo?
- Tecnologías usadas
- Estructura de archivos
- Código documentado (ejemplos y rutas)
- Capturas de pantalla (sitio para pegarlas)
- Cómo ejecutar
- Contribuir
- Licencia

Introducción
Tecno Ciencia es un espacio para que desarrolladores y estudiantes suban sus proyectos, obtengan retroalimentación y colaboren. Este README incluye ejemplos de código con KDoc para validar que el proyecto tiene código documentado.

Estado del proyecto
- Plantillas y estructura listas en el repositorio.
- Código ejemplo con KDoc incluido en secciones abajo.
- Faltan implementaciones de backend reales (ApiService placeholder).
- Añade tus capturas en docs/screenshots/ para completar la entrega.

¿Qué es la app?
Tecno Ciencia permite crear proyectos, subir documentos, organizar por categorías, contribuir y reportar incidencias. Está pensada para estudiantes y equipos pequeños que quieran compartir avances y recibir feedback.

¿Cómo funciona y quién lo hizo?
- Flujo:
  1. Login
  2. Lista de proyectos
  3. Ver proyecto → ver documentos, contribuciones, autor
  4. Perfil del usuario y gestión (admin)
- Código documentado con KDoc. Equipo: Hugo Guerrero (desarrollador principal). Actualiza la tabla de equipo con los nombres reales.

Tecnologías usadas
- Kotlin (Android / Jetpack Compose)
- Arquitectura: MVVM
- Persistencia sugerida: Room
- Networking sugerido: Retrofit/Ktor
- Herramientas: Gradle, Android Studio

Estructura de archivos (principales rutas)
- app/src/main/AndroidManifest.xml
- app/src/main/java/com/hugoguerrero/tecno/
  - MainActivity.kt
  - TecnoApp.kt
  - di/AppModule.kt
  - data/
    - model/ (Project.kt, User.kt, Category.kt, Contribution.kt, Document.kt)
    - remote/ (ApiService.kt)
    - repository/ (ProjectRepositoryImpl.kt, ...)
  - domain/
    - model/ (ProjectDomain.kt, ...)
    - repository/ (ProjectRepository.kt, ...)
    - use_case/ (GetProjectsUseCase.kt, CreateProjectUseCase.kt, ...)
  - ui/
    - components/ (EntityItem.kt, ...)
    - navigation/ (NavGraph.kt)
    - screens/ (login/, main/, project/, profile/, management/, complaint/, donation/)
    - theme/ (Theme.kt)
- docs/screenshots/ (guarda imágenes aquí)

Código documentado (ejemplos)

A continuación se muestran extractos con KDoc de los archivos más relevantes. En el repo están las rutas indicadas donde debe estar el código completo; si necesitas que pegue todo el contenido de cada archivo en el README lo puedo hacer, pero normalmente es suficiente presentar extractos y mantener los archivos en sus rutas.

1) app/src/main/java/com/hugoguerrero/tecno/data/model/Project.kt
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

2) app/src/main/java/com/hugoguerrero/tecno/data/remote/ApiService.kt (extracto)
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

3) app/src/main/java/com/hugoguerrero/tecno/domain/use_case/project/GetProjectsUseCase.kt
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

4) app/src/main/java/com/hugoguerrero/tecno/ui/screens/main/MainScreen.kt
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

5) app/src/main/java/com/hugoguerrero/tecno/MainActivity.kt (extracto)
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

Modo de presentación de código completo
- Si tu requisito es que el README contenga "todo el código de cada archivo", puedo pegar cada archivo completo aquí. Ten en cuenta que hará el README muy largo. Si confirmas que quieres todo pegado en el README, dime "Pegar todo en README" y lo hago.
- Mi recomendación: mantener los archivos reales en sus rutas y dejar extractos en el README + enlaces a los archivos para navegación más limpia.

Capturas de pantalla (sitio para pegarlas)
- Guarda al menos 3 capturas en: docs/screenshots/
- Nombres sugeridos (usa exactamente estos nombres para que el README muestre las imágenes):
  - docs/screenshots/screenshot-login.png
  - docs/screenshots/screenshot-main.png
  - docs/screenshots/screenshot-project.png
  - docs/screenshots/screenshot-profile.png
  - docs/screenshots/screenshot-management.png
  - docs/screenshots/screenshot-complaint.png
  - docs/screenshots/screenshot-donation.png

Ejemplo de inserción en README cuando subas las imágenes:

### LoginScreen
![LoginScreen](docs/screenshots/screenshot-login.png)
Ruta: `app/src/main/java/com/hugoguerrero/tecno/ui/screens/login/LoginScreen.kt`
Breve: Pantalla de ingreso con botón "Entrar".

Cómo ejecutar
1. git clone https://github.com/Hugo-Guerrero/Tecno.git
2. Abre el proyecto en Android Studio
3. Sincroniza Gradle y ejecuta en un emulador o dispositivo

Contribuir
- Haz fork → branch → PR. Añade KDoc en nuevas clases.

Licencia
- Añade un archivo LICENSE con la licencia que prefieras (MIT recomendado).

Notas finales
- Este README está diseñado según la "Anatomía de un README perfecto" que compartiste: describe la app, muestra pantallas, incluye código documentado y lista las tecnologías.
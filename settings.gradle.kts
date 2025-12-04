pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {google()
        mavenCentral()
        // ================================================================
        //  ESTE ES EL REPOSITORIO MODERNO Y FUNCIONAL DE PAYPAL
        // ================================================================
        maven {
            url = uri("https://cardinalcommerceprod.jfrog.io/artifactory/android")
            credentials {
                // Estas son credenciales públicas y genéricas para acceso de solo lectura
                username = "paypal_sgerritz"
                password = "AKCp8jQ8tAahqpT5JjZ4FRP2mW7GMoFZ674kGqHmupTesKeAY2G8NcmPKLuTxTGkKjDLRzDUQ"
            }
        }
        // ================================================================
    }
}

rootProject.name = "Tecno"
include(":app")

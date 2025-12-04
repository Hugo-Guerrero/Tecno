package com.hugoguerrero.tecno

import android.app.Application
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.ktx.Firebase
import com.hugoguerrero.tecno.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TecnoApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Inicializar Firebase App Check
        if (BuildConfig.DEBUG) {
            // Usar el proveedor de depuración para las pruebas
            Firebase.appCheck.installAppCheckProviderFactory(
                DebugAppCheckProviderFactory.getInstance(),
            )
        } else {
            // Usar el proveedor de producción para la versión de lanzamiento
            Firebase.appCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance(),
            )
        }
    }
}

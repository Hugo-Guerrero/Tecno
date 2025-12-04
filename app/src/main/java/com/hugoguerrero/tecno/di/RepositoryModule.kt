package com.hugoguerrero.tecno.di // Asegúrate de que el paquete sea el correcto

import com.hugoguerrero.tecno.data.repository.AuthRepositoryImpl // ¡Importa tu implementación!
import com.hugoguerrero.tecno.domain.repository.AuthRepository // ¡Importa tu interfaz!
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl // Le dices que la implementación es AuthRepositoryImpl
    ): AuthRepository          // Y que debe usarla cuando alguien pida un AuthRepository

}
    
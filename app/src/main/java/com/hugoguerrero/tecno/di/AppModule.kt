package com.hugoguerrero.tecno.di

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hugoguerrero.tecno.data.repository.AuthRepository
import com.hugoguerrero.tecno.data.repository.DonationRepository
import com.hugoguerrero.tecno.data.repository.ProjectRepository
import com.hugoguerrero.tecno.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth, application: Application): AuthRepository = AuthRepository(firebaseAuth, application)

    @Provides
    @Singleton
    fun provideUserRepository(firestore: FirebaseFirestore, storage: FirebaseStorage): UserRepository = UserRepository(firestore, storage) // Corregido aquí

    @Provides
    @Singleton
    fun provideProjectRepository(firestore: FirebaseFirestore, storage: FirebaseStorage): ProjectRepository = ProjectRepository(firestore, storage)

    @Provides
    @Singleton
    fun provideDonationRepository(firestore: FirebaseFirestore): DonationRepository = DonationRepository(firestore)

}

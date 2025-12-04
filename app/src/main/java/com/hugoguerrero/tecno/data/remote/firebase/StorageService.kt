package com.hugoguerrero.tecno.data.remote.firebase

import com.google.firebase.storage.FirebaseStorage
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class StorageService @Inject constructor(
    private val storage: FirebaseStorage
) {
    suspend fun uploadImage(path: String, data: ByteArray): String {
        val ref = storage.reference.child(path)
        ref.putBytes(data).await()
        return ref.downloadUrl.await().toString()
    }
}

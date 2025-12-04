package com.hugoguerrero.tecno.domain.use_case

import com.hugoguerrero.tecno.data.model.UserStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserStatsUseCase @Inject constructor() {
    // Esta es una simulación. En una app real, estos datos vendrían de tu backend.
    operator fun invoke(userId: String): Flow<Result<UserStats>> = flow {
        try {
            // Simulación de estadísticas
            val stats = UserStats(donationsCount = 15, successRate = 88)
            emit(Result.success(stats))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}

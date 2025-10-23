package com.mercel.mealmate.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mercel.mealmate.core.util.Constants
import com.mercel.mealmate.data.local.dao.RecipeDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val recipeDao: RecipeDao
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Clear old cached recipes (older than 7 days)
            val expiryTimestamp = System.currentTimeMillis() - 
                TimeUnit.DAYS.toMillis(Constants.CACHE_EXPIRY_DAYS)
            recipeDao.deleteOldCache(expiryTimestamp)
            
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

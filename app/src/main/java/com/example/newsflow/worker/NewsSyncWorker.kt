package com.example.newsflow.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.newsflow.domain.useCase.news.GetNewsUseCase
import com.example.newsflow.notification.NewsNotificationManager
import com.example.newsflow.utils.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NewsSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val getNewsUseCase: GetNewsUseCase,
    private val notificationManager: NewsNotificationManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val result = getNewsUseCase.invoke()
            when (result) {
                is Resource.Success -> {
                    notificationManager.showNotification("News Updated", "New articles available!")
                    Result.success()
                }

                is Resource.Error -> Result.retry()
                else -> Result.failure()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
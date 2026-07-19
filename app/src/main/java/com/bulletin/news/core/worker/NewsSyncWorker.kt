package com.bulletin.news.core.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bulletin.news.core.notification.NewsNotificationManager
import com.bulletin.news.core.utils.Resource
import com.bulletin.news.domain.useCase.news.GetHeadlinesUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NewsSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val getNewsUseCase: GetHeadlinesUseCase,
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
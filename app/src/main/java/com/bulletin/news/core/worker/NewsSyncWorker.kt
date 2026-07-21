package com.bulletin.news.core.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bulletin.news.core.notification.NewsNotificationManager
import com.bulletin.news.core.utils.Resource
import com.bulletin.news.domain.useCase.news.CheckForNewHeadlinesUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NewsSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val checkForNewHeadlinesUseCase: CheckForNewHeadlinesUseCase,
    private val notificationManager: NewsNotificationManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            when (val result = checkForNewHeadlinesUseCase()) {
                is Resource.Success -> {
                    notificationManager.showNotification("News Updated", "New articles available!")
                    Result.success()
                }

                is Resource.Error -> {
                    // دیگه هر خطایی رو retry نمی‌کنیم: مثلاً اگه API Key نامعتبر باشه
                    // (Unauthorized)، retry کردن هیچ‌وقت جواب نمی‌ده و فقط باتری/شبکه
                    // هدر می‌ره. WorkManager با Result.failure() دیگه دوباره امتحان نمی‌کنه.
                    if (result.error.isRetryable) Result.retry() else Result.failure()
                }
                else -> Result.failure()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
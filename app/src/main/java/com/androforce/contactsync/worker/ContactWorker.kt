package com.androforce.contactsync.worker

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.androforce.contactsync.utils.invokeContactSyncService

class ContactWorker(val context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        // Start sync
        invokeContactSyncService(context)
        return Result.success()

    }
}
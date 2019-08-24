package com.androforce.contactsync.utils

import android.content.Context
import android.content.Intent
import com.androforce.contactsync.worker.ContactSyncService

fun invokeContactSyncService(context: Context) {
    val contactSyncServiceIntent = Intent(context, ContactSyncService::class.java)
    context.startService(contactSyncServiceIntent)
}
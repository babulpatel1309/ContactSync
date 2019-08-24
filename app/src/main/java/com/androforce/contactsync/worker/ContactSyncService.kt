package com.androforce.contactsync.worker

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.provider.ContactsContract
import androidx.annotation.RequiresApi
import com.androforce.contactsync.data.bean.ContactsBean
import com.androforce.contactsync.data.viewModels.ContactsViewModel
import androidx.core.app.NotificationCompat
import com.androforce.contactsync.R
import com.androforce.contactsync.constant.CHANNEL_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.verbose
import kotlin.coroutines.CoroutineContext
import android.content.pm.PackageManager


class ContactSyncService(name: String = "ContactSyncService") : IntentService(name), AnkoLogger,
    CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.IO

    override fun onHandleIntent(p0: Intent?) {


        if (checkPermission()) {
            verbose { "Service started" }
            startForegroundService()
            getContacts()
        } else {
            verbose { "Failed to sync contacts due to permission issue. " }
        }

    }

    private fun getContacts() {
        val contactsViewModel = ContactsViewModel()

        val contactsCursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        verbose { "Contacts Size : ${contactsCursor?.count}" }
        contactsCursor?.let {
            val listOfContacts = ArrayList<ContactsBean>()

            while (it.moveToNext()) {
                val contactsBean = ContactsBean()
                contactsBean.displayName =
                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))

                contactsBean.phoneNumber =
                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                listOfContacts.add(contactsBean)
            }

            if (listOfContacts.isNotEmpty()) {
                launch {
                    contactsViewModel.addItems(listOfContacts)
                }
            }

        }

        contactsCursor?.close()
    }


    private fun startForegroundService() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(CHANNEL_ID, "Contact sync service")
            } else {
                ""
            }

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_stat_sync)
            .setTicker(resources.getString(R.string.app_name))
            .setContentTitle(resources.getString(R.string.notification_title))
            .setContentText(resources.getString(R.string.notification_content))
            .setProgress(0, 0, true)

        startForeground(1, builder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    private fun checkPermission(): Boolean {
        val hasPerm = packageManager.checkPermission(
            android.Manifest.permission.READ_CONTACTS,
            packageName
        )
        return hasPerm == PackageManager.PERMISSION_GRANTED
    }
}
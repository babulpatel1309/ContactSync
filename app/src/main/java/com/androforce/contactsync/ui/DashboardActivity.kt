package com.androforce.contactsync.ui

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.androforce.contactsync.R
import com.androforce.contactsync.adapter.ContactsAdapter
import com.androforce.contactsync.constant.CONTACTS
import com.androforce.contactsync.constant.WORK_REQUEST_TAG
import com.androforce.contactsync.utils.invokeContactSyncService
import com.androforce.contactsync.worker.ContactWorker
import com.joanzapata.iconify.fonts.MaterialIcons
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.custom_actionbar.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.jetbrains.anko.verbose
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext


class DashboardActivity : BaseActivity() {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    lateinit var contactsAdapter: ContactsAdapter
    override fun setContentView(): Int {
        return R.layout.activity_dashboard
    }

    override fun init() {

        //Init UI
        setToolbar(resources.getString(R.string.app_name), false, titleLay)

        titleLay.imgRightIcon.setImageDrawable(
            getMaterialDrawable(
                MaterialIcons.md_sync,
                R.color.white
            )
        )

        contactsAdapter = ContactsAdapter(context)
        recyclerContacts.layoutManager = LinearLayoutManager(context)
        recyclerContacts.adapter = contactsAdapter

        contactsViewModel.getAllDetail().observe(this, Observer {
            emptyView.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            contactsAdapter.submitList(it)
        })

        createWorkRequest()
    }

    override fun buttonClicks() {
        titleLay.btnSync.setOnClickListener {
            askPermission(context, object : PermissionListener {
                override fun onGranted() {
                    //TODO syncing logic
                    invokeContactSyncService(context)
                }

                override fun onDenied() {
                    showSnackBar(rootView, resources.getString(R.string.error_permission))
                }

            }, CONTACTS)

        }
    }


    private fun createWorkRequest() {

        val work = PeriodicWorkRequest
            .Builder(ContactWorker::class.java, 15, TimeUnit.MINUTES)
            .addTag(WORK_REQUEST_TAG)
            .build()

        val workManager = WorkManager.getInstance(context)


        val future = workManager.getWorkInfosByTag(WORK_REQUEST_TAG)
        val list = future.get()
        // start only if no such tasks present
        if (list == null || list.size == 0) {
            // shedule the task
            verbose { "Enqueued work request" }
            workManager.enqueue(work)
        } else {
            Log.e(TAG, "Already Enqueued ${list[0].id}")
        }

    }
}
package com.androforce.contactsync

import android.app.Application
import com.joanzapata.iconify.Iconify
import com.joanzapata.iconify.fonts.MaterialModule

class ApplicationClass : Application() {

    companion object {
        lateinit var mInstance: ApplicationClass

        @Synchronized
        fun getInstance(): ApplicationClass {
            return mInstance
        }
    }

    override fun onCreate() {
        super.onCreate()

        mInstance = this
        Iconify.with(MaterialModule())
    }

}
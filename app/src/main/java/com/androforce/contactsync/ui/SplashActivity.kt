package com.androforce.contactsync.ui

import com.androforce.contactsync.R
import com.androforce.contactsync.constant.SPLASH_TIMEOUT
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


// Created by Babul Patel on 5/5/18.

class SplashActivity : BaseActivity() {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override fun setContentView(): Int {
        return R.layout.activity_splash
    }

    override fun init() {
        launch {
            delay(SPLASH_TIMEOUT)
            goWithFinish(DashboardActivity::class.java)
        }
    }

    override fun buttonClicks() {
    }
}
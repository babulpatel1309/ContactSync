package com.androforce.contactsync.constant

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat


val SPLASH_TIMEOUT = 1500L

const val DATABASE_NAME = "contacts.db"

const val CONTACT_PICK = 10001
const val IMAGES_PICK = 10002
const val TAKE_PICTURE = 10003
const val DRIVE_PICK = 10004
const val EVENT_LISTENER = 10005
const val RC_REQUEST = 10006
const val EMAIL_SENT = 10007
const val CAMERA_PICK = 10008
const val RC_SIGN_IN = 10009

const val CONTACTS = 101
const val STORAGE = 102
const val CAMERA = 103
const val AUDIO = 104

const val WORK_REQUEST_TAG="work_request_tag"

/*Notification*/
const val CHANNEL_ID="contactsync"

fun getColorInt(context: Context, colorName: Int): Int {
    return ContextCompat.getColor(context, colorName)
}

fun getDrawableInt(context: Context, drawableName: Int): Drawable? {
    return ContextCompat.getDrawable(context, drawableName)
}
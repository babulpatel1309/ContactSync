package com.androforce.contactsync.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.androforce.contactsync.constant.DATABASE_NAME
import com.androforce.contactsync.data.bean.ContactsBean
import com.androforce.contactsync.data.dao.ContactsDao


/**
 * Created by Harsh Patel.
 */

@Database(entities = arrayOf(ContactsBean::class), version = 1, exportSchema = false)
abstract class RoomDBHelper : RoomDatabase() {

    abstract fun transactionsDao(): ContactsDao

    companion object {

        lateinit var sInstance: RoomDBHelper

        fun getInstance(context: Context): RoomDBHelper {
            return roomInstance(context)
        }

        private fun roomInstance(context: Context): RoomDBHelper {
            return try {
                if (sInstance != null) {
                    sInstance
                } else {
                    sInstance //Patch
                }
            } catch (e: Exception) {
                Log.d("DB", "DB initialized")
                sInstance = Room.databaseBuilder(context, RoomDBHelper::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()

                sInstance
            }
        }
    }


}
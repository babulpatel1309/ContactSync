package com.androforce.contactsync.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.androforce.contactsync.data.bean.ContactsBean

@Dao
interface ContactsDao {

    @Query("Select * from contacts order by id ")
    fun getAllDetails(): List<ContactsBean>

    @Query("Select * from contacts")
    fun getAllDetailsLive(): LiveData<List<ContactsBean>>

    @Query("Select * from contacts where phoneNumber=:phoneNumber LIMIT 1")
    fun checkRecord(phoneNumber: String): List<ContactsBean>

    @Query("Select * from contacts where id=:uniqueID LIMIT 1")
    fun getRecord(uniqueID: Int): ContactsBean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItems(transaction: List<ContactsBean>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItem(transaction: ContactsBean): Long

    @Update
    fun updateItem(ContactsBean: ContactsBean)

    @Update
    fun updateItem(ContactsBean: List<ContactsBean>)

}
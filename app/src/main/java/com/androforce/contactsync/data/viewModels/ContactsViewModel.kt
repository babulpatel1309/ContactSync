package com.androforce.contactsync.data.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.androforce.contactsync.ApplicationClass
import com.androforce.contactsync.data.bean.ContactsBean
import com.androforce.contactsync.data.database.RoomDBHelper
import com.androforce.contactsync.utils.OperationResponder

class ContactsViewModel : ViewModel() {

    private var transactionList: LiveData<List<ContactsBean>>
    private var roomDBHelper: RoomDBHelper = RoomDBHelper.getInstance(ApplicationClass.mInstance)

    init {
        transactionList = roomDBHelper.transactionsDao().getAllDetailsLive()
    }

    fun getAllDetail(): LiveData<List<ContactsBean>> {
        return transactionList
    }

    fun updateQuestions(
        ContactsBean: ContactsBean,
        operationResponder: OperationResponder? = null
    ) {
        operationResponder?.OnComplete(roomDBHelper.transactionsDao().updateItem(ContactsBean))
    }

    fun updateQuestions(
        ContactsBean: List<ContactsBean>,
        operationResponder: OperationResponder? = null
    ) {
        operationResponder?.OnComplete(roomDBHelper.transactionsDao().updateItem(ContactsBean))
    }

    fun addItems(transaction: List<ContactsBean>) {

        transaction.forEach {
            val record = roomDBHelper.transactionsDao().checkRecord(it.phoneNumber)
            if (record.isEmpty())
                roomDBHelper.transactionsDao().addItem(it)
        }
    }

    fun addItems(transaction: ContactsBean): Long {
        return roomDBHelper.transactionsDao().addItem(transaction)
    }

}
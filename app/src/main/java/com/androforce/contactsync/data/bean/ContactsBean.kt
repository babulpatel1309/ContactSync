package com.androforce.contactsync.data.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactsBean(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var displayName: String = "",
    var phoneNumber: String = ""
)
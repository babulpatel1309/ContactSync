package com.androforce.contactsync.utils

interface OperationResponder {
    fun OnComplete(returnValue: Any? = null)
}
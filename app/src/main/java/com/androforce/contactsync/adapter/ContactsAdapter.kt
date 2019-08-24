package com.androforce.contactsync.adapter

import android.content.Context
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androforce.contactsync.R
import com.androforce.contactsync.data.bean.ContactsBean
import kotlinx.android.synthetic.main.row_contacts.view.*

class ContactsAdapter(
    private val context: Context
) : ListAdapter<ContactsBean, ContactsAdapter.VH>(diffItems()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(context).inflate(R.layout.row_contacts, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.txtName.text = getItem(position).displayName
        holder.itemView.txtNumber.text = getItem(position).phoneNumber
        holder.itemView.txtShortName.text = getShortName(getItem(position).displayName.trim())
    }


    class diffItems() : DiffUtil.ItemCallback<ContactsBean>() {
        override fun areItemsTheSame(oldItem: ContactsBean, newItem: ContactsBean): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ContactsBean, newItem: ContactsBean): Boolean {
            return oldItem == newItem
        }

    }

    class VH(itemview: View) : RecyclerView.ViewHolder(itemview)

    private fun getShortName(displayName: String): String {
        return when {
            displayName.contains(" ") -> {
                val data = displayName.split(" ")
                "${data[0].subSequence(0, 1)}${data[1].subSequence(0, 1)}"
            }
            displayName.trim().length > 2 -> "${displayName.subSequence(0, 2)}"
            else -> displayName
        }
    }
}
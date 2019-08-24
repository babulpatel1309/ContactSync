package com.androforce.contactsync.ui

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.androforce.contactsync.R
import com.androforce.contactsync.constant.*
import com.androforce.contactsync.data.database.RoomDBHelper
import com.androforce.contactsync.data.viewModels.ContactsViewModel
import com.google.android.material.snackbar.Snackbar
import com.joanzapata.iconify.IconDrawable
import com.joanzapata.iconify.Iconify
import com.joanzapata.iconify.fonts.MaterialIcons
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import kotlinx.android.synthetic.main.custom_actionbar.view.*
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.anko.AnkoLogger
import java.text.SimpleDateFormat
import java.util.*

public abstract class BaseActivity : AppCompatActivity(), AnkoLogger, CoroutineScope {

    /*
    * Lateinit vars*/
    lateinit var context: Context
    lateinit var roomDBHelper: RoomDBHelper
    lateinit var contactsViewModel: ContactsViewModel
    lateinit var inflater: LayoutInflater

    /*
    * Initialized vars*/
    var TAG: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (setContentView() > 0)
            setContentView(setContentView())

        context = this
        TAG = context.javaClass.simpleName

        roomDBHelper = RoomDBHelper.getInstance(context)
        contactsViewModel = ViewModelProviders.of(this).get(ContactsViewModel::class.java)


        inflater = LayoutInflater.from(context)

        init()
        buttonClicks()
    }

    fun setToolbar(title: String, isBack: Boolean = false, titleLayView: View) {
        titleLayView.txtTitle.text = title
        titleLayView.txtTitle.setTextColor(getColorInt(context, R.color.white))

        if (isBack) {
            titleLayView.imgLeftIcon.setImageDrawable(getMaterialDrawable(MaterialIcons.md_arrow_back))
        } else {
//            titleLayView.imgLeftIcon.setImageDrawable(getMaterialDrawable(MaterialIcons.))
        }

    }

    fun go(cls: Class<*>) {
        startActivity(Intent(context, cls))
    }

    fun goWithFinish(cls: Class<*>) {
        startActivity(Intent(context, cls))
        finish()
    }

    fun goWithFinish(cls: Class<*>, bundle: Bundle) {
        startActivity(Intent(context, cls), bundle)
        finish()
    }

    fun go(cls: Class<*>, bundle: Bundle?) {
        startActivity(Intent(context, cls), bundle)
    }

    interface PermissionListener {
        fun onGranted()

        fun onDenied()
    }

    /**
     * Handle all kind of permissions.
     */

    fun askPermission(
        context: Context = this.context,
        permissionListener: PermissionListener,
        type: Int
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionListener.onGranted()
        }

        when (type) {

            CONTACTS -> {

                Dexter.withActivity(this)
                    .withPermission(Manifest.permission.READ_CONTACTS)
                    .withListener(object : com.karumi.dexter.listener.single.PermissionListener {
                        override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                            permissionListener.onGranted()
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permission: PermissionRequest?,
                            token: PermissionToken?
                        ) {
                            val builder = AlertDialog.Builder(context)
                            builder.setMessage("We need permissions for this app.")
                            builder.setPositiveButton(
                                "Ok",
                                object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        token?.continuePermissionRequest()
                                    }
                                })
                            builder.setNegativeButton(
                                "Cancel",
                                object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        toast("Without CONTACTS permission this app won't work as Expected..!!")
                                    }

                                })
                            builder.show()

                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                            permissionListener.onDenied()
                            onSettingsShown()
                            toast("Without CONTACTS permission this app won't work as Expected..!!")
                        }
                    })
                    .check()

            }

            STORAGE -> {

                Dexter.withActivity(this)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(object : com.karumi.dexter.listener.single.PermissionListener {
                        override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                            permissionListener.onGranted()
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permission: PermissionRequest?,
                            token: PermissionToken?
                        ) {
                            val builder = AlertDialog.Builder(context)
                            builder.setMessage("We need permissions for this app.")
                            builder.setPositiveButton(
                                "Ok",
                                object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        token?.continuePermissionRequest()
                                    }
                                })
                            builder.setNegativeButton(
                                "Cancel",
                                object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        toast("Without STORAGE permission this app won't work as Expected..!!")
                                    }

                                })
                            builder.show()

                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                            permissionListener.onDenied()
                            onSettingsShown()
                            toast("Without STORAGE permission this app won't work as Expected..!!")
                        }
                    })
                    .check()

            }

            CAMERA -> {

                Dexter.withActivity(this)
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(object : com.karumi.dexter.listener.single.PermissionListener {
                        override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                            permissionListener.onGranted()
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permission: PermissionRequest?,
                            token: PermissionToken?
                        ) {
                            val builder = AlertDialog.Builder(context)
                            builder.setMessage("We need permissions for this app.")
                            builder.setPositiveButton(
                                "Ok",
                                object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        token?.continuePermissionRequest()
                                    }
                                })
                            builder.setNegativeButton(
                                "Cancel",
                                object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        toast("Without CAMERA permission this app won't work as Expected..!!")
                                    }

                                })
                            builder.show()

                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                            permissionListener.onDenied()
                            onSettingsShown()
                            toast("Without CAMERA permission this app won't work as Expected..!!")
                        }
                    })
                    .check()

            }

            AUDIO -> {

                Dexter.withActivity(this)
                    .withPermission(Manifest.permission.RECORD_AUDIO)
                    .withListener(object : com.karumi.dexter.listener.single.PermissionListener {
                        override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                            permissionListener.onGranted()
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permission: PermissionRequest?,
                            token: PermissionToken?
                        ) {
                            val builder = AlertDialog.Builder(context)
                            builder.setMessage("We need permissions for this app.")
                            builder.setPositiveButton(
                                "Ok",
                                object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        token?.continuePermissionRequest()
                                    }
                                })
                            builder.setNegativeButton(
                                "Cancel",
                                object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        toast("Without AUDIO permission this app won't work as Expected..!!")
                                    }

                                })
                            builder.show()

                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                            permissionListener.onDenied()
                            onSettingsShown()
                            toast("Without AUDIO permission this app won't work as Expected..!!")
                        }
                    })
                    .check()

            }

        }
    }

    fun toast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun onSettingsShown() {
        // open setting screen
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:" + context.getPackageName())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        context.startActivity(intent)
    }

    fun getMaterialDrawable(key: MaterialIcons, color: Int = R.color.white): IconDrawable {
        return IconDrawable(context, Iconify.findIconForKey(key.key()))
            .colorRes(color)
    }

    fun showSnackBar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    //TODO abstract methods
    abstract fun setContentView(): Int

    abstract fun init()

    abstract fun buttonClicks()
}
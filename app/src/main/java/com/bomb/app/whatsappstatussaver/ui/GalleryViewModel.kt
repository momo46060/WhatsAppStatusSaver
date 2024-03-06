package com.bomb.app.whatsappstatussaver.ui

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.util.Log
import androidx.annotation.Nullable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.io.File
import kotlin.collections.ArrayList
import androidx.core.app.ActivityCompat.getExternalFilesDirs
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import java.lang.Exception
import java.lang.StringBuilder


class GalleryViewModel(application: Application) : AndroidViewModel(application) {
    var context = application
    private var savedd = File( ( "/storage/emulated/0/Pictures/Status Saver 2022/"))
    private var whatss = File("/storage/emulated/0/Android/media/com.WhatsApp/WhatsApp/Media/.Statuses/")
    private var whatss2 = File("/storage/emulated/0/WhatsApp/Media/.Statuses/")
    var whatsapp = MutableLiveData<List<String>>()
    fun whatsapp() {
        whatsapp.value = getList(whatss,whatss2)
    }

    fun saved_whatsapp() {
        val Imagelist = ArrayList<String>()
        val files = savedd.listFiles()
        files.size?.let {
            for (filer in files) {
                Log.d(TAG, "onCreateView: ${filer.absoluteFile}")
                Imagelist.add(filer.absolutePath)
            }

        }
        whatsapp.value = Imagelist


    }

    fun getList(file: File, file2: File): ArrayList<String> {
        val Imagelist = ArrayList<String>()
//        val f = file.listFiles()
//        val f2 = file2.listFiles()
//        if (file.absoluteFile.exists()) {
//            for (filer in f) {
//                Log.d(TAG, "onCreateViewWW: ${filer.absolutePath}")
//                Imagelist.add(filer.absolutePath)
//            }
//        } else if (file2.absoluteFile.exists()) {
//            for (filer in f2) {
//                Log.d(TAG, "onCreateView: ${filer.absoluteFile}")
//                Imagelist.add(filer.absolutePath)
//            }
//        }

        return Imagelist
    }


}

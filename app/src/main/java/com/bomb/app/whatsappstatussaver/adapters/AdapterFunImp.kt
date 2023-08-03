package com.bomb.app.whatsappstatussaver.adapters

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@ActivityScoped
class AdapterFunImp @Inject constructor(@ApplicationContext private val context: Context) : AdapterFun {

    override fun saveImageInQ(bitmap: Bitmap): Uri {
        val filename = "IMG_${System.currentTimeMillis()}.jpg"
        var fos: OutputStream?
        var imageUri: Uri?
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                (Environment.DIRECTORY_PICTURES + "/Status Saver 2022/")
            )
            put(MediaStore.Video.Media.IS_PENDING, 1)
        }

        //use application context to get contentResolver
        val contentResolver = context.contentResolver

        contentResolver.also { resolver ->
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }

        fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 70, it) }

        contentValues.clear()
        contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
        contentResolver.update(imageUri!!, contentValues, null, null)

        return imageUri!!
    }

    override fun saveVideo(path: String) {
        val valuesvideos = ContentValues()
        valuesvideos.put(
            MediaStore.Video.Media.RELATIVE_PATH,
            (Environment.DIRECTORY_PICTURES + "/Status Saver 2022/")
        )
        valuesvideos.put(MediaStore.Video.Media.TITLE, "videoFileName.mp4")
        valuesvideos.put(MediaStore.Video.Media.DISPLAY_NAME, "videoFileName")
        valuesvideos.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
        valuesvideos.put(
            MediaStore.Video.Media.DATE_ADDED,
            System.currentTimeMillis() / 1000
        )
        valuesvideos.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis())
        valuesvideos.put(MediaStore.Video.Media.IS_PENDING, 1)
        val collection =
            MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY) //all video files on primary external storage

        val uriSavedVideo: Uri = context.contentResolver.insert(collection, valuesvideos)!!

        val pfd: ParcelFileDescriptor?

        try {
            pfd = context.contentResolver.openFileDescriptor(uriSavedVideo, "w")
            assert(pfd != null)
            val out = FileOutputStream(pfd!!.fileDescriptor)

            // Get the already saved video as fileinputstream from here.
            val `in` = context.contentResolver.openInputStream(path.toUri())!!
            val buf = ByteArray(8192)
            var len: Int
            while (`in`.read(buf).also { len = it } > 0) {
                out.write(buf, 0, len)
            }
            out.close()
            `in`.close()
            pfd!!.close()
            valuesvideos.clear()
            valuesvideos.put(
                MediaStore.Video.Media.IS_PENDING,
                0
            ) // Only your app can see the files until pending is turned into 0.
            context.contentResolver.update(uriSavedVideo, valuesvideos, null, null)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }

    fun share(path: String) {

        val share = Intent("android.intent.action.SEND")
        share.type = "image/*"
        val uri = FileProvider.getUriForFile(
            context,
            "com.bomb.app.whatsappstatussaver.provider", File(path)
        )

        share.putExtra("android.intent.extra.STREAM", uri)
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val chooserIntent = Intent.createChooser(share, "Open With")
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)

    }


}
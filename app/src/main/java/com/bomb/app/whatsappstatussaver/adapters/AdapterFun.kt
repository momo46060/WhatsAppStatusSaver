package com.bomb.app.whatsappstatussaver.adapters

import android.graphics.Bitmap
import android.net.Uri

interface AdapterFun {

    fun saveImageInQ(bitmap: Bitmap): Uri

    fun saveVideo(path: String)

}
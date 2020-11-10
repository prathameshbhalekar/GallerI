package com.example.galleri.other

import android.content.Context
import android.media.MediaScannerConnection
import android.util.Log
import com.example.galleri.mediamanager.ImageEntity
import java.io.File
import java.net.URI


object Delete {
    fun deleteImages(context: Context,images: MutableList<ImageEntity>)  {
        for (image in images){
            try{
                val uri: URI = URI.create(image.uri.toString())
                val file = File(uri)
                if(file.exists())
                    file.delete()
            } catch (e:Exception){ continue }

        }
        Log.e("-->", " >= 14")
        MediaScannerConnection.scanFile(context,
            arrayOf(context.getExternalFilesDir(null)?.absolutePath),
            null
        ) { path, uri ->

            Log.e("ExternalStorage", "Scanned $path:")
            Log.e("ExternalStorage", "-> uri=$uri")
        }
    }
}
package com.example.galleri.mediamanager

import android.net.Uri
import java.io.Serializable

data class ImageEntity (
    val uri:Uri,
    val id:Long,
    val title:String,
    val size:Int,
    val date:Long,
) : Serializable
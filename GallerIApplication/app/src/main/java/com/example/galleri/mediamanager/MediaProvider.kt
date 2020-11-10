package com.example.galleri.mediamanager

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import java.io.File
import java.net.URI

class MediaProvider (private val context: Context){
    var imageList= mutableListOf<ImageEntity>()
    private var state:State = State.STATE_UNINITIALIZED

    init {
        state=State.STATE_UNINITIALIZED
    }

    enum class State{
        STATE_INITIALIZED,
        STATE_UNINITIALIZED,
        START_INITIALIZING
    }

    fun getMediaProviderState() = state

    fun initialize(){
        state=State.START_INITIALIZING
        val projection= arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATE_ADDED,
        )

        val query = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        query?.use {cursor ->
            val idColumn=cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val titleColumn=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE)
            val sizeColumn=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
            val dateColumn=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
            while(cursor.moveToNext()){
                val id=cursor.getLong(idColumn)
                val title=cursor.getString(titleColumn)
                val size=cursor.getInt(sizeColumn)
                val date=cursor.getLong(dateColumn)

                val uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                imageList.add(ImageEntity(uri, id, title, size, date))

            }
        }
        query?.close()
        imageList=imageList.asReversed()
        state=State.STATE_INITIALIZED
    }


}

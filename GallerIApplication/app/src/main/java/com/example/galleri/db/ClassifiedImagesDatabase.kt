package com.example.galleri.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ClassifiedImage::class],
    version = 2
)
abstract class ClassifiedImagesDatabase : RoomDatabase() {

    abstract fun getClassifiedImagesDao():ClassifiedImagesDao

}
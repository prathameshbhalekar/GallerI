package com.example.galleri.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "classified_image_table")
data class ClassifiedImage (
    var uri:String?=null,
    @PrimaryKey
    var id:Long?=null,
    var title:String?=null,
    var classification:String?=null
)
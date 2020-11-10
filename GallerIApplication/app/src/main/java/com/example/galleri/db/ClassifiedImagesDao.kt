package com.example.galleri.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ClassifiedImagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClassifiedImage(classifiedImage: ClassifiedImage)

    @Query("DELETE FROM classified_image_table WHERE id = :id")
    suspend fun delete(id:Long)

    @Query("SELECT * FROM classified_image_table WHERE id = :id LIMIT 1")
    fun getClassifiedImageFromId(id: Long):LiveData<ClassifiedImage>

    @Query("SELECT id FROM classified_image_table")
    suspend fun getClassifiedImageId():MutableList<ClassifiedImage>

    @Query("SELECT classification FROM classified_image_table GROUP BY classification")
    fun getImagesCategories():LiveData<MutableList<String>>

    @Query("SELECT * FROM classified_image_table WHERE classification = :classification")
    fun getClassifiedImageFromClassification(classification:String):MutableList<ClassifiedImage>

}
package com.example.galleri.repositories

import com.example.galleri.db.ClassifiedImage
import com.example.galleri.db.ClassifiedImagesDao
import com.example.galleri.mediamanager.ImageEntity
import com.example.galleri.mediamanager.MediaProvider
import javax.inject.Inject

class MainRepository (
        var mediaProvider: MediaProvider,
        var dao: ClassifiedImagesDao,
){
    fun initializeMediaProvider() = mediaProvider.initialize()

    fun getMediaProviderState() = mediaProvider.getMediaProviderState()

    fun getImagesList() = mediaProvider.imageList

    suspend fun insert(classifiedImage: ClassifiedImage) = dao.insertClassifiedImage(classifiedImage)

    suspend fun delete(id: Long) = dao.delete(id)

    fun getClassifiedImageFromId(id:Long) = dao.getClassifiedImageFromId(id)

    suspend fun getClassifiedImageIds() = dao.getClassifiedImageId()

    fun getImagesCategories() = dao.getImagesCategories()

    fun getClassifiedImagesFromClassification(classification:String) = dao.getClassifiedImageFromClassification(classification)

}
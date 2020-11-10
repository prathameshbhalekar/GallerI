package com.example.galleri.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.galleri.mediamanager.ImageEntity
import com.example.galleri.mediamanager.MediaProvider
import com.example.galleri.other.Constants
import com.example.galleri.repositories.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel @ViewModelInject constructor(
        private val repo:MainRepository
): ViewModel() {

    var selectedImages= mutableListOf<ImageEntity>()
    var isSelecting=false
    var currentPosition:Int? = null
    var currentClassification = Constants.ALL
    var currentClassificationIndividualImages = Constants.ALL

    fun getClassifiedImagesFromCategory(classification:String): MutableLiveData<MutableList<ImageEntity>> {
        val data=MutableLiveData<MutableList<ImageEntity>>()
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                if (repo.getMediaProviderState() == MediaProvider.State.STATE_UNINITIALIZED)
                    repo.initializeMediaProvider()
                while (repo.getMediaProviderState() != MediaProvider.State.STATE_INITIALIZED)
                    delay(1)
                if(classification==Constants.ALL){
                    withContext(Dispatchers.Main){
                        data.value=repo.getImagesList()
                    }
                } else {
                    val imagesList= repo.getImagesList().toMutableList()
                    imagesList.sortBy { it.id }
                    val idList = imagesList.map { it.id } as MutableList
                    val classifiedImage = repo.getClassifiedImagesFromClassification(classification)
                    val result = mutableListOf<ImageEntity>()
                    for(image in classifiedImage){
                        val position=idList.binarySearch(image.id)
                        if(position>=0)
                            result+=imagesList[position]
                    }
                    result.reverse()
                    withContext(Dispatchers.Main){
                        data.value=result
                    }
                }
            }

        }
        return data
    }

    fun getAllCategories() = repo.getImagesCategories()


}
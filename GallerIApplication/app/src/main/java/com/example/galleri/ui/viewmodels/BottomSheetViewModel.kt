package com.example.galleri.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.galleri.repositories.MainRepository

class BottomSheetViewModel @ViewModelInject constructor(
    private val repo: MainRepository
): ViewModel() {
    var title:String?=null
    var date:Long?=null
    var size:Float?=null

    fun getClassifiedImage(id:Long) = repo.getClassifiedImageFromId(id)
}
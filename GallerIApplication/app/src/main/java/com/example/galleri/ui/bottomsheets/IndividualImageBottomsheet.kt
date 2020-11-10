package com.example.galleri.ui.bottomsheets

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.galleri.R
import com.example.galleri.databinding.IndividualImageBottomsheetBinding
import com.example.galleri.mediamanager.ImageEntity
import com.example.galleri.other.DateConverter
import com.example.galleri.ui.viewmodels.BottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IndividualImageBottomsheet: BottomSheetDialogFragment() {
    private lateinit var viewModel:BottomSheetViewModel
    private lateinit var binding:IndividualImageBottomsheetBinding
    private lateinit var image:ImageEntity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.individual_image_bottomsheet,container,false)
        viewModel = ViewModelProvider(this).get(BottomSheetViewModel::class.java)

        setTextViews()
        viewModel.getClassifiedImage(image.id).observe(viewLifecycleOwner, {classifiedImage->
            if(classifiedImage!=null)
                binding.classificationTextview.text = classifiedImage.classification
        })

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setTextViews(){
        if(arguments!=null){
            image= arguments?.getSerializable("image") as ImageEntity
            viewModel.title = image.title
            viewModel.date = image.date
            viewModel.size = image.size.toFloat()/1000.0F
        }
        binding.titleTextview.text = viewModel.title
        binding.dateTextview.text = viewModel.date?.let { DateConverter.convertDate(it) }
        binding.sizeTextview.text="${viewModel.size.toString()} KB"
    }
}
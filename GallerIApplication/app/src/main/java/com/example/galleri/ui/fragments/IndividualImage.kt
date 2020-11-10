package com.example.galleri.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.galleri.R
import com.example.galleri.adapters.ViewPageAdapter
import com.example.galleri.databinding.FragmentIndividualImageBinding
import com.example.galleri.mediamanager.ImageEntity
import com.example.galleri.other.Animations.crossFade
import com.example.galleri.other.Constants
import com.example.galleri.other.DateConverter
import com.example.galleri.other.Delete
import com.example.galleri.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class IndividualImage : Fragment() {

    private lateinit var binding: FragmentIndividualImageBinding
    private lateinit var mainViewModel: MainViewModel
    @Inject
    lateinit var adapter:ViewPageAdapter
    private lateinit var imagesList: MutableList<ImageEntity>
    var shortAnimationDuration:Long? = null
    private lateinit var navController:NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_individual_image,
            container,
            false
        )
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        shortAnimationDuration=  resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        if(mainViewModel.currentPosition ==null) {
            mainViewModel.currentPosition = arguments?.getInt(Constants.POSITION_KEY)
            mainViewModel.currentClassificationIndividualImages= arguments?.getString(Constants.CLASSIFICATION_KEY)!!
        }
        setOnClickListeners()
        setViewPager()

        mainViewModel.getClassifiedImagesFromCategory(mainViewModel.currentClassificationIndividualImages).observe(viewLifecycleOwner, { imageList ->
            if (!imageList.isNullOrEmpty()) {
                adapter.setImages(imageList)
                this.imagesList = imageList
                adapter.notifyDataSetChanged()
                mainViewModel.currentPosition?.let { binding.viewpager.setCurrentItem(it, false) }
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController=view.findNavController()
    }

    private fun setOnClickListeners(){

        binding.info.setOnClickListener {
            val currentImage=imagesList[mainViewModel.currentPosition!!]
            val bundle= bundleOf("image" to currentImage)
            navController.navigate(R.id.action_individualImage_to_individual_image_dialog, bundle)
        }

        binding.delete.setOnClickListener {
            val currentImage=imagesList[mainViewModel.currentPosition!!]
            binding.viewpager.setCurrentItem(
                if (mainViewModel.currentPosition!! - 1 < 0) mainViewModel.currentPosition!! - 1 else mainViewModel.currentPosition!! + 1,
                true
            )
            Delete.deleteImages(requireContext(),mutableListOf(currentImage))
        }

        binding.share.setOnClickListener { sendImage() }

    }

    private fun sendImage(){
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        val currentImage = imagesList[mainViewModel.currentPosition!!]
        shareIntent.putExtra(Intent.EXTRA_STREAM, currentImage.uri)
        shareIntent.type = "image/*"
        startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }

    private fun setViewPager(){

        adapter.setOnClickListener(object : ViewPageAdapter.OnClickListener {
            override fun onClick() {
                crossFade(binding.topDrawer, shortAnimationDuration!!)
                crossFade(binding.bottomDrawer, shortAnimationDuration!!)
            }
        })

        binding.viewpager.adapter = adapter

        binding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                mainViewModel.currentPosition = position

                if (!imagesList.isNullOrEmpty()) {

                    val dateLong = imagesList[position].date

                    binding.date.text = DateConverter.convertDate(dateLong)
                    binding.title.text = imagesList[position].title

                }
            }

            override fun onPageScrollStateChanged(state: Int) {}

        })



    }

}
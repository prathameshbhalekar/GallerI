package com.example.galleri.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.galleri.R
import com.example.galleri.adapters.AllImagesAdapter
import com.example.galleri.adapters.CategoriesAdapter
import com.example.galleri.databinding.FragmentMainRecyclerBinding
import com.example.galleri.mediamanager.ImageEntity
import com.example.galleri.other.Constants
import com.example.galleri.other.Delete
import com.example.galleri.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainRecycler : Fragment() {
    private lateinit var binding: FragmentMainRecyclerBinding
    private lateinit var mainViewModel: MainViewModel
    private val TAG = "MainRecycler"
    @Inject
    lateinit var imagesAdapter: AllImagesAdapter
    @Inject
    lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var navController:NavController
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.i(TAG,"Fragment launched")

        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_main_recycler,container,false)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        imagesAdapter.setSelectingState(mainViewModel.isSelecting,mainViewModel.selectedImages)
        categoriesAdapter.setSelected(mainViewModel.currentClassification)

        binding.options.isVisible = mainViewModel.isSelecting
        binding.selectedCount.text = "${imagesAdapter.getSelected().size} Items Selected"

        setUpMainRecyclerView()
        setUpCategoriesRecyclerView()
        setOnClickListener()
        setUpSearch()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController=view.findNavController()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainViewModel.selectedImages = imagesAdapter.getSelected()
        mainViewModel.isSelecting = imagesAdapter.getIsSelecting()
    }

    private fun setUpSearch(){
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                categoriesAdapter.filter.filter(text)
                return false
            }

        })
    }

    private fun setOnClickListener(){

        binding.cancel.setOnClickListener {
            imagesAdapter.cancelSelecting()
            binding.options.isVisible=false
            imagesAdapter.notifyDataSetChanged()
        }

        binding.share.setOnClickListener {
            sendImages()
            imagesAdapter.cancelSelecting()
            binding.options.isVisible=false
            imagesAdapter.notifyDataSetChanged()
        }

        binding.delete.setOnClickListener {
            Delete.deleteImages(requireContext(),imagesAdapter.getSelected())
            imagesAdapter.cancelSelecting()
            binding.options.isVisible=false
            imagesAdapter.notifyDataSetChanged()
        }
    }

    private fun sendImages(){
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND_MULTIPLE
        val uris= ArrayList<Uri>()
        for(image in imagesAdapter.getSelected())
            uris.add(image.uri)
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
        shareIntent.type = "image/*"
        startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }

    private fun setUpCategoriesRecyclerView(){
        categoriesAdapter.setSelected(mainViewModel.currentClassification)
        categoriesAdapter.setOnClickListener(object :CategoriesAdapter.OnClickListener{
            override fun onClick(classification: String) {
                if(classification!=mainViewModel.currentClassification){
                    mainViewModel.currentClassification=classification
                    setImagesByClassification(classification)
                }

            }

        })
        binding.rvCategories.adapter = categoriesAdapter
        binding.rvCategories.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        mainViewModel.getAllCategories().observe(viewLifecycleOwner,{ categories->
            if(categories.isNullOrEmpty())
                binding.pbAllImages.isVisible=true
            else{
                categories.add(0, Constants.ALL)
                binding.pbAllImages.isVisible=false
                categoriesAdapter.setCategoryTitles(categories)
                categoriesAdapter.notifyDataSetChanged()
                Log.d(TAG,"Categories adapter list set")
            }
        })
    }

    private fun setUpMainRecyclerView(){

        imagesAdapter.setOnClickListener(object :AllImagesAdapter.OnItemClickListener{
            @SuppressLint("SetTextI18n")
            override fun onItemClick(image: ImageEntity, position:Int) {
                if(imagesAdapter.getIsSelecting()){
                    binding.selectedCount.text = "${imagesAdapter.getSelected().size} Items Selected"
                } else {
                    val positionData=Bundle()
                    positionData.putInt(Constants.POSITION_KEY,position)
                    positionData.putString(Constants.CLASSIFICATION_KEY,mainViewModel.currentClassification)
                    navController.navigate(R.id.action_mainRecycler_to_individualImage,positionData)
                }

            }

            @SuppressLint("SetTextI18n")
            override fun onItemLongClick(image: ImageEntity) {
                binding.options.isVisible=true
                binding.selectedCount.text = "${imagesAdapter.getSelected().size} Items Selected"
            }

        })
        imagesAdapter.setSelectingState(mainViewModel.isSelecting , mainViewModel.selectedImages)


        val orientation=activity?.resources?.configuration?.orientation

        binding.rvAllImages.layoutManager =
                if(orientation== Configuration.ORIENTATION_PORTRAIT)
                    GridLayoutManager(context, Constants.SPAN_COUNT_PORTRAIT)
                else
                    GridLayoutManager(context,Constants.SPAN_COUNT_LANDSCAPE)

        binding.rvAllImages.adapter=this.imagesAdapter
        setImagesByClassification(mainViewModel.currentClassification)

      }

    private fun setImagesByClassification(classification:String){
        imagesAdapter.cancelSelecting()
        binding.options.isVisible=false
        mainViewModel
                .getClassifiedImagesFromCategory(classification)
                .observe(viewLifecycleOwner,{images->
                    if(images.isNullOrEmpty())
                        binding.pbAllImages.isVisible=true
                    else{
                        binding.pbAllImages.isVisible=false
                        imagesAdapter.images = images
                        binding.rvAllImages.adapter?.notifyDataSetChanged()
                        Log.d(TAG, "Images adapter list set")
                    }
                })
    }


}
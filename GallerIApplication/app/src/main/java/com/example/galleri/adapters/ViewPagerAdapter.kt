package com.example.galleri.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.RequestManager
import com.example.galleri.mediamanager.ImageEntity
import com.github.chrisbanes.photoview.PhotoView
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ViewPageAdapter @Inject constructor (
        @ApplicationContext
        private val context:Context,
        private val glide: RequestManager,
): PagerAdapter() {

    interface OnClickListener{
        fun onClick()
    }

    private var onClickListener: OnClickListener? = null
    private var images:MutableList<ImageEntity>? = null

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener=onClickListener
    }

    fun setImages(images:MutableList<ImageEntity>){
        this.images=images
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int = if (images==null) 0 else images!!.size


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView= PhotoView(context)
        glide
                .load(images!![position].uri)
                .fitCenter()
                .into(imageView)
        imageView.setOnClickListener { onClickListener?.onClick() }
        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView((obj as View))
    }


}
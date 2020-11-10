package com.example.galleri.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.galleri.R
import com.example.galleri.mediamanager.ImageEntity
import javax.inject.Inject

class AllImagesAdapter @Inject constructor(
    private val glide:RequestManager
) :RecyclerView.Adapter<AllImagesAdapter.AllImagesViewHolder>() {

    private var isSelecting = false

    private lateinit var selectedImages : MutableList<ImageEntity>

    private  var onItemClickListener: OnItemClickListener? = null

    fun setSelectingState(isSelecting:Boolean, selectedImages:MutableList<ImageEntity> ){
        this.selectedImages = selectedImages
        this.isSelecting = isSelecting
    }


    class AllImagesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView = itemView.findViewById(R.id.recycler_individual_image)

        fun bind(image: ImageEntity,glide:RequestManager,contains:Boolean){
            itemView.isPressed=contains
            glide.load(image.uri)
                .centerCrop()
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllImagesViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(
                R.layout.all_images_recycler_individual_image,
                parent,
                false
        )
        return AllImagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllImagesViewHolder, position: Int) {
        holder.bind(images[position] , glide , selectedImages.contains(images[position]))
        holder.imageView.setOnLongClickListener {
            addSelectedImages(position)
            isSelecting=true
            onItemClickListener?.onItemLongClick(images[position])
            holder.itemView.isPressed=selectedImages.contains(images[position])
            true
        }

        holder.imageView.setOnClickListener {
            if(isSelecting){
                addSelectedImages(position)
                holder.itemView.isPressed=selectedImages.contains(images[position])
            }
            onItemClickListener?.onItemClick(images[position] , position)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    private fun addSelectedImages(position: Int){
        if(selectedImages.contains(images[position]))
            selectedImages.remove(images[position])
        else
            selectedImages.add(images[position])
    }

    fun getSelected() = selectedImages
    fun getIsSelecting() = isSelecting

    fun cancelSelecting(){
        selectedImages.clear()
        isSelecting=false
    }

    fun setOnClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener=onItemClickListener
    }

    interface OnItemClickListener{
        fun onItemClick(image: ImageEntity,position: Int)
        fun onItemLongClick(image: ImageEntity)
    }

    private val diffCallback = object : DiffUtil.ItemCallback<ImageEntity>() {
        override fun areItemsTheSame(oldItem: ImageEntity, newItem: ImageEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ImageEntity, newItem: ImageEntity): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var images: List<ImageEntity>
        get() = differ.currentList
        set(value) = differ.submitList(value)

}
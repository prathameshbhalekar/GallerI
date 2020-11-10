package com.example.galleri.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.galleri.R
import com.example.galleri.other.Constants
import java.util.*


class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>(),Filterable {

    interface OnClickListener{
        fun onClick(classification:String)
    }

    private var selectedItem = Constants.ALL
    private var onClickListener:OnClickListener? = null

    fun setSelected(selectedItem: String) {this.selectedItem = selectedItem}

    fun setOnClickListener(onClickListener: OnClickListener){ this.onClickListener=onClickListener }

    class CategoriesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val categoryTv: TextView = itemView.findViewById(R.id.category_tv)
        val selectedIv:ImageView = itemView.findViewById(R.id.selectedIndividualCategory)

        fun bind(categoryText:String, isSelected:Boolean){
            categoryTv.text = categoryText
            selectedIv.isVisible = isSelected

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(
                R.layout.individual_category,
                parent,
                false
        )
        return CategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        filterList?.get(position)?.let { holder.bind(it, filterList!![position] == selectedItem) }
        holder.categoryTv.setOnClickListener {
            selectedItem = filterList!![position]
            notifyDataSetChanged()
            holder.selectedIv.isVisible=true
            onClickListener?.onClick(filterList!![position])
        }
    }

    override fun getItemCount(): Int {
        return if(filterList==null) 0 else filterList!!.size
    }

    private var categoryTitles: MutableList<String>?=null
    private var filterList: MutableList<String>?=null

    fun setCategoryTitles(categoryTitles:MutableList<String>){
        this.categoryTitles = categoryTitles
        filterList=categoryTitles.toMutableList()
    }

    override fun getFilter(): Filter {
        return object:Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                val filterResults = FilterResults()
                filterList = if(charSearch.isEmpty())
                    categoryTitles!!.toMutableList()
                else{
                    val resultList = mutableListOf<String>()
                    for(titles in categoryTitles!!)
                        if(titles.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT)))
                            resultList.add(titles)
                    resultList
                }
                filterResults.values = filterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterList = results?.values as MutableList<String>
                notifyDataSetChanged()
            }

        }
    }

}
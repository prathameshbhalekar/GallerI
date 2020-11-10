package com.example.galleri.other

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewDecoration (private val space: Int) : RecyclerView.ItemDecoration(){
    override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
    ) {
        outRect.set(space,space,space,space)
    }
}

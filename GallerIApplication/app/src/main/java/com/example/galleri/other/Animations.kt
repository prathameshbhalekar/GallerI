package com.example.galleri.other

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.card.MaterialCardView

object Animations {
    fun crossFade(view:View,shortAnimationDuration:Long){
        if(view.visibility==View.VISIBLE){
            view.alpha=1f
            view.animate()
                    .alpha(0f)
                    .setDuration(shortAnimationDuration)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            view.visibility=View.GONE
                        }
                    })
        }
        else{
            view.alpha=0f
            view.visibility=View.VISIBLE
            view.animate()
                    .alpha(1f)
                    .setDuration(shortAnimationDuration)
                    .setListener(null)

        }


    }
}
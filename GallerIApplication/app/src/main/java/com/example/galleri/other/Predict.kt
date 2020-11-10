package com.example.galleri.other

import android.content.Context
import com.example.galleri.mediamanager.ImageEntity
import com.example.galleri.tf.Model
import org.tensorflow.lite.support.image.TensorImage

object Predict {
    fun classify(image: ImageEntity, model:Model, context: Context): String {
        val bitmap= BitmapConverter.getBitmap( image.uri,context)
        val tensorImage: TensorImage = TensorImage.fromBitmap(bitmap)
        val classification=model.predict(tensorImage).probabilityAsCategoryList
        var maxLabel=""
        var maxProbability=Float.MIN_VALUE
        for(category in classification)
            if(category.score>maxProbability){
                maxProbability=category.score
                maxLabel=category.label
            }
        return maxLabel
    }
}
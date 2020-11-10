package com.example.galleri.tf

import android.content.Context
import com.example.galleri.ml.EfficientnetLite4Int82
import com.example.galleri.ml.Model
import org.tensorflow.lite.support.image.TensorImage

class Model (val context:Context){

    private val model=Model.newInstance(context)

    fun predict(image:TensorImage) = model.process(image)

    fun close() = model.close()

}
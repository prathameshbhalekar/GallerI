package com.example.galleri.ui

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.galleri.R
import com.example.galleri.classificationService.ClassificationService
import com.example.galleri.ml.Model
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.support.image.TensorImage

@AndroidEntryPoint
class MainActivity : AppCompatActivity() , MultiplePermissionsListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val permissions = listOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        Dexter.withActivity(this)
            .withPermissions(permissions)
            .withListener(this)
            .check()
        supportActionBar?.hide()
        val model = Model.newInstance(this)
        val map = BitmapFactory.decodeResource(this.resources,R.drawable.img)
        val v = TensorImage.fromBitmap(map)
        val t=model.process(v)
        println("value is ${t.probabilityAsCategoryList}")

    }

    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
        startService(Intent(this,ClassificationService::class.java))
    }

    override fun onPermissionRationaleShouldBeShown(
        permissions: MutableList<PermissionRequest>?,
        token: PermissionToken?
    ) {
        TODO("Not yet implemented")
    }
}
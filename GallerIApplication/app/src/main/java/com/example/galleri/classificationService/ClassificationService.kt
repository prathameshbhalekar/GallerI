package com.example.galleri.classificationService

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.example.galleri.db.ClassifiedImage
import com.example.galleri.mediamanager.MediaProvider
import com.example.galleri.other.Constants
import com.example.galleri.other.Constants.APPLICATION_NAME
import com.example.galleri.other.Constants.NOTIFICATION_CHANNEL_ID
import com.example.galleri.other.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.galleri.other.Constants.NOTIFICATION_ICON
import com.example.galleri.other.Constants.NOTIFICATION_ID
import com.example.galleri.other.Constants.NOTIFICATION_TEXT
import com.example.galleri.other.Predict
import com.example.galleri.repositories.MainRepository
import com.example.galleri.tf.Model
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@AndroidEntryPoint
class ClassificationService : LifecycleService() {
    @Inject
    lateinit var model: Model

    @Inject
    lateinit var repo: MainRepository

    private val TAG = "classificationService"

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "Started Classification")
        startForegroundService()
        classifyImages(this)

    }

    private fun startForegroundService (){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(false)
                .setOngoing(true)
                .setSmallIcon(NOTIFICATION_ICON)
                .setContentTitle(APPLICATION_NAME)
                .setContentText(NOTIFICATION_TEXT)

                startForeground(NOTIFICATION_ID,notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)
    }

    private fun classifyImages(context: Context) {
        CoroutineScope(Dispatchers.Default).launch {
            val imagesListTask = async {

                if (repo.getMediaProviderState() == MediaProvider.State.STATE_UNINITIALIZED)
                    repo.initializeMediaProvider()

                while (repo.getMediaProviderState() != MediaProvider.State.STATE_INITIALIZED)
                    delay(1)

                repo.getImagesList()

            }

            val classifiedImageListTask = async {

                repo.getClassifiedImageIds()

            }

            val classifiedImageList = classifiedImageListTask.await()
            val imagesList = imagesListTask.await()
            val classifiedImagesId = classifiedImageList.map { it.id }
            if(!classifiedImageList.isNullOrEmpty()){

                classifiedImagesId.sortedWith { n1, n2 -> n1!!.compareTo(n2!!) }
                val deletedImages=classifiedImagesId.toList() as MutableList

                val time= measureTimeMillis {
                    for (image in imagesList)
                        deletedImages.remove(image.id)
                    Log.d(TAG, "${deletedImages.size} images deleted")
                    for(id in deletedImages)
                        repo.delete(id!!)
                }
                Log.d(TAG,"$time ms take for deletion")
            }


            var count = 0
            var totalCount=0
            val time = measureTimeMillis {

                for (image in imagesList){
                    totalCount++
                    if (classifiedImagesId.binarySearch(image.id, 0, classifiedImagesId.size) < 0) {
                        try {
                            withTimeout(3000L) {
                                count++
                                val category = Predict.classify(image, model, context = context)
                                val classifiedImage = ClassifiedImage(
                                        image.uri.toString(),
                                        image.id,
                                        image.title,
                                        Constants.classificationTitles[category]
                                )
                                repo.insert(classifiedImage)

                            }


                        } catch (e: Exception) {
                            count--
                            Log.e(TAG, "Error in classifying ${image.title}")
                        }
                    }
                }
            }

            Log.i(TAG, "Classified Images in $time ms")
            Log.i(TAG, "Successfully Classified $count Images")
            model.close()
            stopSelf()

        }
    }


}
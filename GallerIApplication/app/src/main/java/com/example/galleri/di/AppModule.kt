package com.example.galleri.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.galleri.R
import com.example.galleri.adapters.AllImagesAdapter
import com.example.galleri.adapters.CategoriesAdapter
import com.example.galleri.db.ClassifiedImagesDao
import com.example.galleri.db.ClassifiedImagesDatabase
import com.example.galleri.mediamanager.MediaProvider
import com.example.galleri.other.Constants.CLASSIFIED_IMAGES_DB_NAME
import com.example.galleri.repositories.MainRepository
import com.example.galleri.tf.Model
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext
        context:Context
    ) = Glide.with(context)
        .setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
        )

    @Provides
    @Singleton
    fun provideMediaProvider(
        @ApplicationContext
        context:Context
    ) = MediaProvider(context)

    @Provides
    @Singleton
    fun provideClassifiedImageDatabase(
        @ApplicationContext
        context:Context
    )= Room.databaseBuilder(
        context,
        ClassifiedImagesDatabase::class.java,
        CLASSIFIED_IMAGES_DB_NAME
    )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideClassifiedImageDatabaseDao(db:ClassifiedImagesDatabase)=db.getClassifiedImagesDao()

    @Provides
    @Singleton
    fun provideMainRepository(
            mediaProvider:MediaProvider,
            dao: ClassifiedImagesDao
    )= MainRepository(mediaProvider,dao)

    @Provides
    @Singleton
    fun provideModel(
        @ApplicationContext
        context: Context
    ) = Model(context)

    @Provides
    @Singleton
    fun provideCategoriesAdapter() = CategoriesAdapter()

}
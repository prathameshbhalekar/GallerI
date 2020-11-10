package com.example.galleri.other

import com.example.galleri.R

object Constants {
    const val CLASSIFIED_IMAGES_DB_NAME = "classified_images_db"

    const val NOTIFICATION_CHANNEL_ID = "classification_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Classification"
    const val NOTIFICATION_ID = 1
    const val NOTIFICATION_ICON = R.mipmap.icon_mimap
    const val NOTIFICATION_TEXT = "Updating your images."

    const val APPLICATION_NAME = "GallerI"

    const val SPAN_COUNT_PORTRAIT = 3
    const val SPAN_COUNT_LANDSCAPE = 6

    const val DATE_FORMAT = "dd MMMM yyyy"
    const val TIME_ZONE = "IST"

    const val ALL = "All"

    const val POSITION_KEY = "position"
    const val CLASSIFICATION_KEY = "classification"

    val classificationTitles = mapOf(
        "nature" to "Nature",
        "people" to "People",
        "architecture" to "Architecture",
        "fashion" to "Fashion",
        "health" to "Health & Wellness",
        "interiors" to "Interiors",
        "street-photography" to "Street Photography",
        "technology" to "Technology",
        "travel" to "Travel",
        "textures-patterns" to "Textures & Patterns",
        "business-work" to "Business & Work",
        "animals" to "Animals",
        "food-drink" to "Food & Drink",
        "athletics" to "Athletics",
        "arts-culture" to "Arts & Culture",
        "history" to "History",
        "screenshots" to "Screenshots"
    )

}
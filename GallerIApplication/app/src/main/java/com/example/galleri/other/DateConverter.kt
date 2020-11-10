package com.example.galleri.other

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateConverter {
    @SuppressLint("SimpleDateFormat")
    fun convertDate(dateLong:Long): String {
        val date = Date(dateLong * 1000)
        val df2 = SimpleDateFormat(Constants.DATE_FORMAT)
        df2.timeZone = TimeZone.getTimeZone(Constants.TIME_ZONE)
        return df2.format(date)
    }
}
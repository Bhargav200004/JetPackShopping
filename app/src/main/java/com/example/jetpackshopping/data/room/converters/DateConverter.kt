package com.example.jetpackshopping.data.room.converters

import androidx.room.TypeConverter
import java.util.Date


open class DateConverter {

    //Converts Long to Date
    @TypeConverter
    fun toDate(date : Long?) : Date? {
        return date?. let{ Date(it)}
    }

    //Converts date to time
    @TypeConverter
    fun fromDate(date: Date?) : Long? {
        return date?.time
    }

}



package com.tk.innovaweathercase.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tk.innovaweathercase.util.ConstantsDatabase

@Entity(
    tableName = ConstantsDatabase.WEATHER_DATABASE_CITY_TABLE
)
data class City(
    @PrimaryKey
    val geonameId: Int,
    val countryCode: String,
    val fcl: String,
    val fcode: String,
    val lat: String,
    val lng: String,
    val name: String,
    val toponymName: String,
    var isFav: Boolean = false,
    var isCurrent: Boolean = false
)
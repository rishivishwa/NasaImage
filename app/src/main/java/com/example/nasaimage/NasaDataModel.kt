package com.example.nasaimage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NasaDataModel")

class NasaDataModel (
    @PrimaryKey(autoGenerate = true) val id: Long,
    val date: String,
    val explanation: String,
    val hdurl: String,
    val media_type: String,
    val service_version: String,
    val title: String,
    val url: String,


)
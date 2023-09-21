package com.example.nasaimage

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET(TOKEN)
    fun getApi(): Call<NasaDataModel>
}
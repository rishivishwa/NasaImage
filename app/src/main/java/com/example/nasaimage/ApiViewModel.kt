package com.example.nasaimage

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.TimeUnit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiViewModel : ViewModel() {
    private val apiService: ApiService

    var apiData: MutableLiveData<NasaDataModel> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    private val handler = Handler(Looper.getMainLooper())

    /**
     * first class initialized then this block will call
     * using retrofit hit to url
     *
     */
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        fetchDataPeriodically()
    }


    private fun fetchDataPeriodically() {
        // it check time is 24 hours
        val delayMillis = TimeUnit.HOURS.toMillis(24) // 24 hours in milliseconds
        handler.post(object : Runnable {
            override fun run() {
                // time completed 24 hours the fetch api data again
                fetchApiData()
                handler.postDelayed(this, delayMillis)
            }
        })
    }
    //fetch  api response
    private fun fetchApiData() {
        isLoading.value = true
        val call = apiService.getApi()
        call.enqueue(object : Callback<NasaDataModel> {
            // onResponse function give api response is it successful
            override fun onResponse(call: Call<NasaDataModel>, response: Response<NasaDataModel>) {
                if (response.isSuccessful) { // response is  successful or not
                    apiData.value = response.body()
                }
                isLoading.value = false
            }
            // onFailure function give error
            override fun onFailure(call: Call<NasaDataModel>, t: Throwable) {
                isLoading.value = false
            }
        })
    }

    fun refreshData() {
        // Fetch the data again
        fetchApiData()
    }
}

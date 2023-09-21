package com.example.nasaimage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.nasaimage.databinding.ActivityMainBinding
import com.example.nasaimage.room.NasaDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private lateinit var binding: ActivityMainBinding
    private var viewModel: ApiViewModel? = null
    private var videoUrl: String? = null
    private var imageUrl: String? = null
    private var isVideoPlay: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeApiResponse()
        setupRefreshButton()
        playVideo()
    }

    /**
     * observeApiResponse function observe the viewModel Live data,
     * fetch data from table
     * saving the Api response in Room Database
     */
    private fun observeApiResponse() {
        viewModel = ViewModelProvider(this)[ApiViewModel::class.java]
        viewModel?.apiData?.observe(this) { apiData ->
            // data is observed then do false isRefreshing value
            binding.swipeUp.isRefreshing = false
            videoUrl = apiData.hdurl
            imageUrl = apiData.url
            apiData?.let {
                // Dispatchers.IO code block are running in background thread
                launch(Dispatchers.IO) {
                    val apiDao = NasaDatabase.getInstance(applicationContext).dbDao
                    val dataExists = apiDao.dataAvailable()
                    if (dataExists != null)
                    {
                        // Dispatchers.Main code block are running in UI thread
                        withContext(Dispatchers.Main) {
                            showApiData(dataExists)
                        }
                    } else {
                        try {
                            // Dispatchers.Main code block are running in UI thread
                            withContext(Dispatchers.Main) {
                                apiDao.insert(apiData)
                                showApiData(apiData)
                            }
                        } catch (e: Exception) {
                            // Handle error
                        }
                    }
                }
            }
        }
        isDataLoaded()
    }
    /**
     * here we are observing data is loaded or not
     * if loaded view is visible else gone the view
     */
    private fun isDataLoaded(){
        viewModel?.isLoading?.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }


    //this function we are using to set data on different views
    private fun showApiData(apiData: NasaDataModel) {
        Glide.with(this@MainActivity).load(apiData.url).into(binding.nasaImage)
        binding.imageTitle.text = apiData.title
        binding.imageDate.text = apiData.date
        binding.imageDescription.text = apiData.explanation
    }
    // to refresh the data setupRefreshButton function are using ...
    private fun setupRefreshButton() {
        binding.swipeUp.setOnRefreshListener {
            viewModel?.refreshData()
        }
    }

    // remove and show placeholder image and set data to play video
    private fun playVideo() {
        binding.nasaImage.setOnClickListener {
            if (isVideoPlay == true) {
                binding.nasaImage.visibility = ImageView.VISIBLE
                binding.playButton.visibility = Button.VISIBLE
                Glide.with(this@MainActivity).load(imageUrl).into(binding.nasaImage)
                isVideoPlay = false
            }
        }
        binding.playButton.setOnClickListener {
            removePlaceholderAndSetImage()
        }
    }
    // checking uri data is video or audio and handel accordingly
    private fun removePlaceholderAndSetImage(){
        binding.nasaImage.visibility = ImageView.GONE
        binding.playButton.visibility = Button.GONE
        val url = videoUrl
        val mimeType = Utils.isImageOrVideo(url)
        if (mimeType != null) {
            if (mimeType.startsWith("image")) {
                // It's an image
                binding.nasaImage.visibility = ImageView.VISIBLE
                Glide.with(this).load(url).into(binding.nasaImage)
                isVideoPlay = true

            } else if (mimeType.startsWith("video")) {
                // It's a video
                Log.d("video", "this is the video file")
                isVideoPlay = true

            } else {
                Log.d("error", "this file is neither a video or neither a image")
            }
        } else {
            Log.d(
                "error",
                "this file is neither a video or neither a image we did not determine mime type "
            )

        }
    }

}
package com.example.nasaimage

import android.webkit.MimeTypeMap


const val BASE_URL = "https://api.nasa.gov/"
const val TOKEN = "planetary/apod?api_key=ThSKRiav0rl2QVzdWWc7wXdNbTuCQQRFiWB6NUqi"

object Utils {
    // isImageOrVideo function return mimetype
    fun isImageOrVideo(url: String?): String? {
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(url)
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
        return mimeType
    }
}
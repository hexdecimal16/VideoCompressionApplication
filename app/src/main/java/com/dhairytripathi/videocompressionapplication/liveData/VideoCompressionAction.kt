package com.dhairytripathi.videocompressionapplication.liveData

enum class VideoCompressionAction(var message: String) {

    COMPRESS_REQUESTED(""),
    DISPLAY_PROGRESS("Converting video.."),
    COMPRESS_SUCCESS("Convertion successful"),
    FAILURE("UnExpected Error")
}
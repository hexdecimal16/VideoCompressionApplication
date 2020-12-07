package com.dhairytripathi.videocompressionapplication.ui.activity.videoSummary

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.dhairytripathi.videocompressionapplication.constants.VIDEO_URI

class VideoSummaryViewModel : ViewModel() {

    lateinit var videoUri: Uri

    fun extractIntentExtra(data: Intent) {
        videoUri = data.getParcelableExtra(VIDEO_URI)!!
    }
}
package com.dhairytripathi.videocompressionapplication.ui.activity.videoCompression

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dhairytripathi.videocompressionapplication.application.VideoCompressionApplication
import com.dhairytripathi.videocompressionapplication.constants.SELECTED_VIDEO_FILE_PATH
import com.dhairytripathi.videocompressionapplication.liveData.VideoCompressionAction
import com.dhairytripathi.videocompressionapplication.utils.CompressUtil
import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler
import nl.bravobit.ffmpeg.FFmpeg
import java.io.File

class VideoCompressionViewModel : ViewModel() {
    private val repository = VideoCompressionRepository()

    val videoCompressAction: MutableLiveData<VideoCompressionAction> get() = repository.videoCompressAction

    lateinit var videoUri: Uri
    lateinit var filePath: String
    val mBitrate = ObservableField("5000") // Standard bitrate for 720p video
    val isKbps = ObservableBoolean(true)    // there are currently only 2 -> KBPS and MBPS

    lateinit var ffmpeg: FFmpeg

    private val mOutputFileLocation =
            "${VideoCompressionApplication.appContext.getExternalFilesDir(null)?.absolutePath}/CompressedVideos"

    fun extractIntentExtras(data: Intent) {
        filePath = data.getStringExtra(SELECTED_VIDEO_FILE_PATH)!!
        videoUri = Uri.fromFile(File(filePath))
    }

    fun onCompressButtonClicked() {
        videoCompressAction.value = VideoCompressionAction.COMPRESS_REQUESTED
    }

    private fun createOutputFolderIfItDoesNotExists() {
        val file = File(mOutputFileLocation)
        if (!file.exists()) file.mkdir()
    }

    fun onCompressionInitiated() {
        createOutputFolderIfItDoesNotExists()

        repository.compressVideo(
                mBitrate.get()!!,
                videoUri.toFile().absolutePath,
                "$mOutputFileLocation/${File(filePath).name}",
                isKbps.get(),
                ffmpeg
        )
    }
}

class VideoCompressionRepository {

    val videoCompressAction = MutableLiveData<VideoCompressionAction>()

    fun compressVideo(
        bitrate: String,
        filePath: String,
        outputFilePath: String,
        isKbps: Boolean,
        ffmpeg: FFmpeg
    ) {
        val command = CompressUtil.getFfpmpegBitrateCompressionCommand(
            bitrate,
            if (isKbps) "K" else "M",
            filePath,
            outputFilePath
        )

        ffmpeg.execute(command, object : ExecuteBinaryResponseHandler() {

            override fun onStart() {
                videoCompressAction.value = VideoCompressionAction.DISPLAY_PROGRESS

                Log.e("onStart", "")
            }

            override fun onProgress(message: String) {
                Log.e("onProgress", message)
            }

            override fun onFailure(message: String) {
                val action = VideoCompressionAction.FAILURE
                action.message = "message"

                Log.e("onFailure", message)
            }

            override fun onSuccess(message: String) {
                videoCompressAction.value = VideoCompressionAction.COMPRESS_SUCCESS

                Log.e("onSuccess", message)
            }

            override fun onFinish() {
                Log.e("onFinish", "")
            }
        })
    }
}
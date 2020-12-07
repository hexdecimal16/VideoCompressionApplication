package com.dhairytripathi.videocompressionapplication.ui.activity.videoCompression

import android.content.Intent
import android.os.Bundle
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dhairytripathi.videocompressionapplication.R
import com.dhairytripathi.videocompressionapplication.constants.START_SUMMARY_ACTIVITY
import com.dhairytripathi.videocompressionapplication.constants.VIDEO_URI
import com.dhairytripathi.videocompressionapplication.databinding.ActivityVideoCompressionBinding
import com.dhairytripathi.videocompressionapplication.liveData.VideoCompressionAction
import com.dhairytripathi.videocompressionapplication.ui.activity.videoSummary.VideoSummaryActivity
import com.dhairytripathi.videocompressionapplication.utils.ToastUtils
import com.dhairytripathi.videocompressionapplication.utils.UiUtil
import kotlinx.android.synthetic.main.activity_video_compression.*
import nl.bravobit.ffmpeg.FFmpeg

class VideoCompressionActivity : AppCompatActivity() {
    private val viewModel: VideoCompressionViewModel =
        ViewModelProvider.NewInstanceFactory().create(VideoCompressionViewModel::class.java)

    private lateinit var binding: ActivityVideoCompressionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.extractIntentExtras(intent)
        setUpDataBinding()
        observeViewModelLiveData()
        initVideoView()
        initFfmpeg()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            START_SUMMARY_ACTIVITY -> finish()
        }
    }

    private fun setUpDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_compression)
        binding.vm = viewModel
    }

    private fun observeViewModelLiveData() {
        viewModel.videoCompressAction.observe(this, Observer {

            when (it) {
                VideoCompressionAction.COMPRESS_REQUESTED -> compressVideo()
                VideoCompressionAction.DISPLAY_PROGRESS -> displayCompressionInProgress()
                VideoCompressionAction.COMPRESS_SUCCESS -> onCompressionSuccess()
            }
        })
    }

    private fun displayCompressionInProgress() {
        UiUtil.displayProgress(this)
    }

    private fun onCompressionSuccess() {
        UiUtil.hideProgress()
        ToastUtils.showToast(applicationContext.getString(R.string.compression_success), this)
        startSummaryActivity()
    }

    private fun startSummaryActivity() {
        val intent = Intent(this, VideoSummaryActivity::class.java)
        intent.putExtra(VIDEO_URI, viewModel.videoUri)

        startActivityForResult(intent, START_SUMMARY_ACTIVITY)
    }

    private fun compressVideo() {
        if ((kbps_radio_button.isChecked || mbps_radio_button.isChecked)
            && viewModel.mBitrate.get()!!.isNotBlank()
        ) {
            viewModel.onCompressionInitiated()

        } else {
            ToastUtils.showToast(applicationContext.getString(R.string.bitrate), this)
        }
    }

    private fun initFfmpeg() {
        viewModel.ffmpeg = FFmpeg.getInstance(this)
        checkSupport() // Checking for library support.
    }

    private fun checkSupport() {
        if (!viewModel.ffmpeg.isSupported) {
            ToastUtils.showToast(applicationContext.getString(R.string.library_not_supported), this)

            finish()
        }
    }

    private fun initVideoView() {
        val mediaController = MediaController(this)
        mediaController.setAnchorView(video_view)

        video_view.setMediaController(mediaController)
        video_view.setVideoURI(viewModel.videoUri)
        video_view.requestFocus()
        video_view.start()
    }
}
package com.dhairytripathi.videocompressionapplication.ui.activity.videoSummary

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.dhairytripathi.videocompressionapplication.R
import com.dhairytripathi.videocompressionapplication.databinding.ActivityVideoSummaryBinding
import kotlinx.android.synthetic.main.activity_video_compression.*

class VideoSummaryActivity : AppCompatActivity() {
    private val viewModel: VideoSummaryViewModel =
        ViewModelProvider.NewInstanceFactory().create(VideoSummaryViewModel::class.java)

    lateinit var binding: ActivityVideoSummaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.extractIntentExtra(intent)
        setUpDataBinding()
        initVideoView()
    }

    private fun setUpDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_summary)
        binding.vm = viewModel
    }

    private fun initVideoView() {
        val mediaController = MediaController(this);
        mediaController.setAnchorView(video_view)
        video_view.setMediaController(mediaController);

        video_view.setVideoURI(viewModel.videoUri)
        video_view.requestFocus()
        video_view.start()

        initVideoViewListeners()
    }

    private fun initVideoViewListeners() {
        setVideoViewOnErrorListener()
    }

    private fun setVideoViewOnErrorListener() {
        video_view.setOnErrorListener { _, _, _ ->
            Toast.makeText(
                this,
                applicationContext.getString(R.string.unable_to_play_video),
                Toast.LENGTH_SHORT
            ).show()

            false
        }
    }
}
package com.dhairytripathi.videocompressionapplication.ui.activity.videoSelection

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dhairytripathi.videocompressionapplication.R
import com.dhairytripathi.videocompressionapplication.adapter.ClickListener
import com.dhairytripathi.videocompressionapplication.constants.COMPRESS_VIDEO
import com.dhairytripathi.videocompressionapplication.adapter.VideoSelectionAdapter
import com.dhairytripathi.videocompressionapplication.constants.SELECTED_VIDEO_FILE_PATH
import com.dhairytripathi.videocompressionapplication.databinding.ActivityVideoSelectionBinding
import com.dhairytripathi.videocompressionapplication.liveData.VideoSelectionAction
import com.dhairytripathi.videocompressionapplication.ui.activity.videoCompression.VideoCompressionActivity
import com.dhairytripathi.videocompressionapplication.utils.PermissionUtil
import com.dhairytripathi.videocompressionapplication.utils.UiUtil
import kotlinx.android.synthetic.main.activity_video_selection.*

class VideoSelectionActivity: AppCompatActivity(),ClickListener {

    private val viewModel: VideoSelectionViewModel = ViewModelProvider.NewInstanceFactory().create(VideoSelectionViewModel::class.java)

    lateinit var binding: ActivityVideoSelectionBinding

    private val adapter = VideoSelectionAdapter(this, emptyList(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpDataBinding()
        observeViewModelLiveData()
    }

    override fun onResume() {
        super.onResume()

        if (PermissionUtil.checkPermissions(this)) initRecyclerView()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for (permission in grantResults)
            if (permission != PermissionChecker.PERMISSION_GRANTED) {
                showPermissionNotGrantedToast()
                return
            }

        initRecyclerView()
    }

    private fun showPermissionNotGrantedToast() {
        Toast.makeText(
            this,
            "Please provide appropriate permissions to continue!",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            COMPRESS_VIDEO -> fetchAllVideosFromStorage()
        }
    }

    override fun onItemClick(position: Int) {
        startVideoCompressionActivity(adapter.videos!![position].path)
    }

    private fun startVideoCompressionActivity(selectedFilePath: String) {
        val compressVideoActivityIntent = Intent(this, VideoCompressionActivity::class.java)
        compressVideoActivityIntent.putExtra(SELECTED_VIDEO_FILE_PATH, selectedFilePath)

        startActivityForResult(
            compressVideoActivityIntent,
            COMPRESS_VIDEO
        )
    }

    private fun setUpDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_selection)
        binding.vm = viewModel
    }

    private fun observeViewModelLiveData() {
        viewModel.videoSelectionLiveData.observe(this, Observer {

            when (it) {
                VideoSelectionAction.SELECT_VIDEO_BUTTON_CLICKED -> fetchAllVideosFromStorage()
                null -> UiUtil.showUnexpectedErrorToast()
            }
        })
    }

    private fun fetchAllVideosFromStorage() {
        adapter.updateVideoList(viewModel.fetchAllVideosFromStorage(contentResolver))
    }

    private fun initRecyclerView() {
        fetchAllVideosFromStorage()
        rv_video.layoutManager = GridLayoutManager(this, 2)
        rv_video.adapter = adapter
    }
}
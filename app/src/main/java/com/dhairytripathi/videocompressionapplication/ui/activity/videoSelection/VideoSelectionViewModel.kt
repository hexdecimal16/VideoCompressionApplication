package com.dhairytripathi.videocompressionapplication.ui.activity.videoSelection

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dhairytripathi.videocompressionapplication.liveData.VideoSelectionAction
import com.dhairytripathi.videocompressionapplication.model.Video
import nl.bravobit.ffmpeg.FFmpeg

class VideoSelectionViewModel: ViewModel() {
    val repository = VideoSelectionRepository()

    val videoSelectionLiveData = MutableLiveData<VideoSelectionAction>()

    fun onSelectVideoButtonClicked() {
        videoSelectionLiveData.value = VideoSelectionAction.SELECT_VIDEO_BUTTON_CLICKED
    }

    fun fetchAllVideosFromStorage(contentResolver: ContentResolver) : List<Video> {
        return repository.fetchAllVideosFromStorage(contentResolver)
    }
}

class VideoSelectionRepository {

    fun fetchAllVideosFromStorage(contentResolver: ContentResolver): List<Video> {
        val videoList = ArrayList<Video>()

        val contentResolver = contentResolver
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val filePath: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                val title: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))

                val video = Video(filePath, title)
                videoList.add(video)

            } while (cursor.moveToNext())
        }

        return videoList
    }
}
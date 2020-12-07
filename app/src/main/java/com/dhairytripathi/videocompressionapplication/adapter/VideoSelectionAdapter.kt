package com.dhairytripathi.videocompressionapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dhairytripathi.videocompressionapplication.R
import com.dhairytripathi.videocompressionapplication.model.Video
import kotlinx.android.synthetic.main.item_video.view.*


class VideoSelectionAdapter(
    private val context: Context,
    var videos: List<Video>,
    private val listener: ClickListener
) : RecyclerView.Adapter<VideoSelectionAdapter.VideoSelectionViewHolder>() {

    class VideoSelectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.iv_video
        val titleTextView: TextView = view.tv_video_title
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoSelectionViewHolder {
        val view: View =  LayoutInflater.from(context).inflate(R.layout.item_video, parent, false)
        return  (VideoSelectionViewHolder(view))
    }

    override fun getItemCount(): Int {
        return if(videos.isEmpty()) { 0 } else { videos.size }
    }

    override fun onBindViewHolder(holder: VideoSelectionViewHolder, position: Int) {
        val video = videos?.get(position)

        holder.titleTextView.text = video?.title
        loadImage(video!!.path, holder.imageView)
        setOnImageClickListener(holder.imageView, position)
    }

    private fun loadImage(filePath: String, imageView: ImageView) {
        Glide.with(context)
                .load(filePath)
                .centerCrop()
                .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_baseline_ondemand_video))
                .into(imageView);
    }

    private fun setOnImageClickListener(imageView: ImageView, position: Int) {
        imageView.setOnClickListener {
            listener.onItemClick(position)
        }
    }

    fun updateVideoList(videoList: List<Video>) {
        this.videos = videoList
        notifyDataSetChanged()
    }

}
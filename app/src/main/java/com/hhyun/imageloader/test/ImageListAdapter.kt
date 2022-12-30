package com.hhyun.imageloader.test

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hhyun.imageloader.Util
import com.hhyun.imageloader.databinding.ItemImgaeBinding
import com.hhyun.imageloader.loader.ImageLoader

class ImageListAdapter(
    private val requestManager: RequestManager,
    private val height: Int
    ): ListAdapter<String, ImageListAdapter.ImageViewHolder> (
    object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        return ImageViewHolder(ItemImgaeBinding.inflate(LayoutInflater.from(parent.context), parent, false)).apply {
            this.binding.root.layoutParams = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
        }
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        if (position == RecyclerView.NO_POSITION) return

        val imageUrl = getItem(position)

        (holder.binding as? ItemImgaeBinding)?.apply {

            ImageLoader.Builder(requestManager)
                .loadImage(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .priority(Priority.IMMEDIATE)
                .into(iv)

        }

    }

    class ImageViewHolder(val binding: ItemImgaeBinding): RecyclerView.ViewHolder(binding.root)
}
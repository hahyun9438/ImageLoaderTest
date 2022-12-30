package com.hhyun.imageloader.loader

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.request.transition.Transition

interface InterparkImageDrawableLoadListener {
    fun onResourceReady(resource: Drawable?)
    fun onLoadFailed()
}

interface InterparkImageBitmapLoadListener {
    fun onResourceReady(resource: Bitmap?)
    fun onLoadFailed()
}

interface InterparkImageViewTargetListener {
    fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?)
    fun onLoadFailed(errorDrawable: Drawable?)
    fun onResourceCleared(placeholder: Drawable?)
}
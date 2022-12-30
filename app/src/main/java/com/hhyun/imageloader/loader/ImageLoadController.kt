package com.hhyun.imageloader.loader

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.*
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.hhyun.imageloader.R
import com.hhyun.imageloader.Util
import com.hhyun.imageloader.loader.transform.BorderTransformation
import com.hhyun.imageloader.loader.transform.OverlayTransformation
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class ImageLoadController {

    private var imageViewInitScaleType: ImageView.ScaleType? = null


    //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    // Glide로 이미지 로드
    //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    internal fun ready(imageView: ImageView?, imageResId: Int, scaleType: ImageView.ScaleType?) {
        imageView?.setImageResource(imageResId)
        scaleType?.let { imageView?.scaleType = it }
    }


    @SuppressLint("CheckResult")
    internal fun loadImageByGlide(
        context: Context,
        imageView: ImageView?,
        ivTarget: ImageViewTarget<*>?,
        requestManager: RequestManager? = null,
        param: LoadParam
    ) {

        try {

            this.imageViewInitScaleType = imageView?.scaleType ?: ivTarget?.view?.scaleType

            val transformations = mutableListOf<Transformation<Bitmap>>()

            when(param.imageScaleType) {
                ImageView.ScaleType.CENTER_CROP -> transformations.add(CenterCrop())
                ImageView.ScaleType.CENTER_INSIDE -> transformations.add(CenterInside())
                ImageView.ScaleType.FIT_CENTER -> transformations.add(FitCenter())
                else -> {
                    when(imageViewInitScaleType) {
                        ImageView.ScaleType.CENTER_CROP -> transformations.add(CenterCrop())
                        ImageView.ScaleType.CENTER_INSIDE -> transformations.add(CenterInside())
                        ImageView.ScaleType.FIT_CENTER -> transformations.add(FitCenter())
                        else -> {}
                    }
                }
            }

            if(param.blur != null) {
                transformations.add(BlurTransformation(param.blur!!))
            }

            if(param.isCircleCrop == true) {
                transformations.add(CircleCrop())

            } else {
                val radiusPx = Util.dpToPx(context, param.radiusDp ?: 0)
                if(radiusPx > 0) {
                    transformations.add(RoundedCornersTransformation(radiusPx, 0, param.radiusType))
                }
            }

            if(param.colorFilter != null) {
                transformations.add(ColorFilterTransformation(param.colorFilter!!))
            }

            if(param.inlineBorderResId != null) {
                transformations.add(OverlayTransformation(param.inlineBorderResId!!))
            }

            if((param.outlineBorderWidthDp ?: 0f) > 0f) {
                if(transformations.contains(FitCenter())) transformations.remove(FitCenter())

                transformations.add(
                    BorderTransformation(imageView?.id ?: context.hashCode())
                        .setBorderWidth(Util.dpToFloatPx(context, param.outlineBorderWidthDp ?: 0f))
                        .setBorderColor(param.outlineBorderColor)
                        .setMargin(Util.dpToFloatPx(context, param.outlineBorderMarginDp ?: 0f))
                        .setPadding(Util.dpToFloatPx(context, param.outlineBorderPaddingDp ?: 0f))
                        .setPaddingBoundaryBorderWidth(Util.dpToFloatPx(context, param.outlineBorderPaddingBoundaryWidthDp ?: 0f))
                        .setPaddingBoundaryBorderColor(param.outlineBorderPaddingBoundaryColor)
                        .isCircle(param.isCircleCrop)
                        .radius(Util.dpToPx(context, param.radiusDp ?: 0), param.radiusType)
                )
            }

            if(param.transformationBitmapList != null) {
                transformations.addAll(param.transformationBitmapList!!)
            }

            val options = RequestOptions().apply {
                if(transformations.size > 0) {
                    transforms(*transformations.toTypedArray())
                }
            }

            val placeholder = when {
                (param.placeholderResId ?: 0) > 0 -> ContextCompat.getDrawable(context, param.placeholderResId!!)
                param.placeholder != null -> param.placeholder
                else -> null
            }

            val error = when {
                (param.errorResId ?: 0) > 0 -> ContextCompat.getDrawable(context, param.errorResId!!)
                param.error != null -> param.error
                else -> null
            }

            val requestOption = RequestOptions().apply {

                placeholder?.let { placeholder(it) }
                error?.let { error(it) }

                if(param.isSkipMemoryCache) skipMemoryCache(true)
                if(param.diskCacheStrategy != null) diskCacheStrategy(param.diskCacheStrategy!!)

                if((param.overrideWidth ?: 0) > 0 && (param.overrideHeight ?: 0) > 0) override(param.overrideWidth!!, param.overrideHeight!!)
                if(param.isOverrideOriginalSize) override(Target.SIZE_ORIGINAL)

                if(param.priority != null) priority(param.priority!!)
                if(param.disableTransition) dontAnimate()

                apply(options)
            }



            // ----------------------------------
            val mRequestManager = requestManager ?: Glide.with(context)

            when {
                param.isBitmap -> {
                    val mRequestBuilder = if(param.imageBitmap != null) {
                        mRequestManager.asBitmap().load(param.imageBitmap).apply(requestOption)
                    } else {
                        mRequestManager.asBitmap().load(param.imageUrl).apply(requestOption)
                    }

                    if (param.returnBitmap != null){
                        param.returnBitmap = returnBitmapByGlide(mRequestBuilder, param)

                    } else {
                        loadBitmapByGlide(
                            imageView,
                            if (ivTarget is BitmapImageViewTarget) ivTarget else null,
                            mRequestBuilder, param)
                    }
                }

                param.imageUri != null -> {
                    val mRequestBuilder = mRequestManager.load(param.imageUri).apply(requestOption)
                    loadUriByGlide(
                        imageView,
                        if (ivTarget is DrawableImageViewTarget) ivTarget else null,
                        mRequestManager, mRequestBuilder, param, options, error)
                }

                param.gif != null -> loadGifByGlide(imageView, mRequestManager, param, requestOption)

                !param.imageUrl.isNullOrEmpty() -> {
                    val mRequestBuilder = mRequestManager.load(param.imageUrl).apply(requestOption)
                    loadDrawableByGlide(
                        imageView,
                        if (ivTarget is DrawableImageViewTarget) ivTarget else null,
                        mRequestManager, mRequestBuilder, param, options, error)
                }

                param.imageResId != null -> {
                    val mRequestBuilder = mRequestManager.load(param.imageResId).apply(requestOption)
                    loadDrawableByGlide(
                        imageView,
                        if (ivTarget is DrawableImageViewTarget) ivTarget else null,
                        mRequestManager, mRequestBuilder, param, options, error)
                }

                else -> {
                    if(param.usedImageRequestOptionsForPlaceholder) {
                        val mRequestBuilder = mRequestManager.load(param.placeholderResId ?: placeholder).apply(requestOption)
                        loadDrawableByGlide(
                            imageView,
                            if(ivTarget is DrawableImageViewTarget) ivTarget else null,
                            mRequestManager, mRequestBuilder, param, options, error)

                    } else {
                        imageView?.setImageDrawable(placeholder)
                        (param.placeholderScaleType ?: imageViewInitScaleType)?.let { imageView?.scaleType = it }
                    }
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @SuppressLint("CheckResult")
    private fun loadDrawableByGlide(
        imageView: ImageView?,
        ivTarget: ImageViewTarget<Drawable>?,
        requestManager: RequestManager,
        requestBuilder: RequestBuilder<Drawable>,
        param: LoadParam,
        options: RequestOptions,
        error: Drawable?
    ) {

        requestBuilder.setDrawableThumbnail(requestManager, param, options)
        requestBuilder.setDrawableError(requestManager, param, error, options)

        if(param.disableTransition) {
            requestBuilder.transition(DrawableTransitionOptions().dontTransition())

        } else if(param.isWithCrossFade) {
            if(param.withCrossFadeDuration > 0)
                requestBuilder.transition(DrawableTransitionOptions.withCrossFade(param.withCrossFadeDuration))
            else requestBuilder.transition(DrawableTransitionOptions.withCrossFade())
        }

        requestBuilder.listener(object: RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                e?.printStackTrace()
                if(param.isGoneViewIfError) imageView?.visibility = View.GONE
                param.drawableLoadListener?.onLoadFailed()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                param.drawableLoadListener?.onResourceReady(resource)
                return false
            }
        })

        if(imageView != null && param.viewTargetListener != null) {
            requestBuilder.into(object: CustomViewTarget<ImageView, Drawable>(imageView) {
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    param.viewTargetListener?.onLoadFailed(errorDrawable)
                }

                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    param.viewTargetListener?.onResourceReady(resource, transition)
                }

                override fun onResourceCleared(placeholder: Drawable?) {
                    param.viewTargetListener?.onResourceCleared(placeholder)
                }
            })

        }

        when {
            imageView != null -> requestBuilder.into(imageView)
            ivTarget != null -> requestBuilder.into(ivTarget)
            else -> requestBuilder.submit()
        }

    }

    @SuppressLint("CheckResult")
    private fun loadBitmapByGlide(
        imageView: ImageView?,
        ivTarget: ImageViewTarget<Bitmap>?,
        requestBuilder: RequestBuilder<Bitmap>,
        param: LoadParam
    ) {

        requestBuilder.listener(object: RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?, model: Any?,
                target: Target<Bitmap>?, isFirstResource: Boolean
            ): Boolean {
                e?.printStackTrace()
                if(param.isGoneViewIfError) imageView?.visibility = View.GONE
                param.bitmapLoadListener?.onLoadFailed()
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?, model: Any?, target: Target<Bitmap>?,
                dataSource: DataSource?, isFirstResource: Boolean
            ): Boolean {
                param.bitmapLoadListener?.onResourceReady(resource)
                return false
            }
        })

        when {
            imageView != null -> requestBuilder.into(imageView)
            ivTarget != null -> requestBuilder.into(ivTarget)
            else -> requestBuilder.submit()
        }
    }

    @SuppressLint("CheckResult")
    private fun returnBitmapByGlide(
        requestBuilder: RequestBuilder<Bitmap>,
        param: LoadParam
    ) :Bitmap?{

        requestBuilder.listener(object: RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?, model: Any?,
                target: Target<Bitmap>?, isFirstResource: Boolean
            ): Boolean {
                e?.printStackTrace()
                param.bitmapLoadListener?.onLoadFailed()
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?, model: Any?, target: Target<Bitmap>?,
                dataSource: DataSource?, isFirstResource: Boolean
            ): Boolean {
                param.bitmapLoadListener?.onResourceReady(resource)
                return false
            }
        })

        return if (param.bitmapWidth != null && param.bitmapHeight != null) {
            requestBuilder.submit(param.bitmapWidth!!, param.bitmapHeight!!).get()

        } else null
    }

    @SuppressLint("CheckResult")
    private fun loadUriByGlide(
        imageView: ImageView?,
        ivTarget: ImageViewTarget<Drawable>?,
        requestManager: RequestManager,
        requestBuilder: RequestBuilder<Drawable>,
        param: LoadParam,
        options: RequestOptions,
        error: Drawable?
    ) {

        requestBuilder.setDrawableThumbnail(requestManager, param, options)
        requestBuilder.setDrawableError(requestManager, param, error, options)

        requestBuilder
            .listener(object: RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?, model: Any?,
                    target: Target<Drawable>?, isFirstResource: Boolean
                ): Boolean {
                    e?.printStackTrace()
                    if(param.isGoneViewIfError) imageView?.visibility = View.GONE
                    param.drawableLoadListener?.onLoadFailed()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?, model: Any?, target: Target<Drawable>?,
                    dataSource: DataSource?, isFirstResource: Boolean
                ): Boolean {
                    param.drawableLoadListener?.onResourceReady(resource)
                    return false
                }
            })

        when {
            imageView != null -> requestBuilder.into(imageView)
            ivTarget != null -> requestBuilder.into(ivTarget)
            else -> requestBuilder.submit()
        }
    }

    @SuppressLint("CheckResult")
    private fun loadGifByGlide(
        imageView: ImageView?,
        requestManager: RequestManager,
        param: LoadParam,
        reqOptions: RequestOptions
    ) {

        val requestBuilder = requestManager.asGif().load(param.gif).apply(reqOptions)
        requestBuilder
            .listener(object: RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?, model: Any?,
                    target: Target<GifDrawable>?, isFirstResource: Boolean
                ): Boolean {
                    e?.printStackTrace()
                    if(param.isGoneViewIfError) imageView?.visibility = View.GONE
                    param.drawableLoadListener?.onLoadFailed()
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?,
                    dataSource: DataSource?, isFirstResource: Boolean
                ): Boolean {
                    param.drawableLoadListener?.onResourceReady(resource)
                    return false
                }
            })

        when {
            imageView != null -> requestBuilder.into(imageView)
            else -> requestBuilder.submit()
        }
    }


    @SuppressLint("CheckResult")
    private fun RequestBuilder<Drawable>.setDrawableThumbnail(requestManager: RequestManager, param: LoadParam, option: RequestOptions) {

        when {
            (param.thumbnailRatio ?: 0f) > 0f -> thumbnail(param.thumbnailRatio!!)

            param.thumbnailResId != null -> {
                val thumbnailRequestManager = requestManager.load(param.thumbnailResId)
                if(param.usedImageRequestOptionsForThumbnail) thumbnailRequestManager.apply(option)
                thumbnail(thumbnailRequestManager)
            }

            param.thumbnail != null -> {
                val thumbnailRequestManager = requestManager.load(param.thumbnail)
                if(param.usedImageRequestOptionsForThumbnail) thumbnailRequestManager.apply(option)
                thumbnail(thumbnailRequestManager)
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun RequestBuilder<Drawable>.setDrawableError(
        requestManager: RequestManager,
        param: LoadParam,
        error: Drawable?,
        option: RequestOptions
    ) {
        if(error == null) return
        if(param.usedImageRequestOptionsForError) error(requestManager.load(error).apply(option))
        else {
            val requestOption = RequestOptions()
            when(param.errorScaleType) {
                ImageView.ScaleType.CENTER_CROP -> requestOption.centerCrop()
                ImageView.ScaleType.CENTER_INSIDE -> requestOption.centerInside()
                ImageView.ScaleType.FIT_CENTER -> requestOption.fitCenter()
                else -> {
                    when(imageViewInitScaleType) {
                        ImageView.ScaleType.CENTER_CROP -> requestOption.centerCrop()
                        ImageView.ScaleType.CENTER_INSIDE -> requestOption.centerInside()
                        ImageView.ScaleType.FIT_CENTER -> requestOption.fitCenter()
                        else -> {}
                    }
                }
            }

            error(requestManager.load(error).apply(requestOption))
        }
    }


    class LoadParam {

        var imageUrl: String? = null
        var imageResId: Int? = null
        var imageUri: Uri? = null
        var imageBitmap: Bitmap? = null
        var imageScaleType: ImageView.ScaleType? = null

        var gif: Int? = null

        var isBitmap: Boolean = false
        var returnBitmap: Bitmap? = null
        var bitmapWidth: Int? = null
        var bitmapHeight: Int? = null

        var placeholderResId: Int? = null
        var placeholder: Drawable? = null
        var placeholderScaleType: ImageView.ScaleType? = null
        var usedImageRequestOptionsForPlaceholder: Boolean = false

        var errorResId: Int? = null
        var error: Drawable? = null
        var errorScaleType: ImageView.ScaleType? = null
        var usedImageRequestOptionsForError: Boolean = false

        var overrideWidth: Int? = null
        var overrideHeight: Int? = null
        var isOverrideOriginalSize: Boolean = false

        var thumbnailRatio: Float? = null
        var thumbnail: Drawable? = null
        var thumbnailResId: Int? = null
        var usedImageRequestOptionsForThumbnail: Boolean = false

        var radiusDp: Int? = null
        var radiusType: RoundedCornersTransformation.CornerType = RoundedCornersTransformation.CornerType.ALL

        var isCircleCrop: Boolean? = null

        var colorFilter: Int? = null
        var blur: Int? = null

        var inlineBorderResId: Int? = null

        var outlineBorderWidthDp: Float? = null
        var outlineBorderColor: Int? = null
        var outlineBorderPaddingDp: Float? = null
        var outlineBorderMarginDp: Float? = null
        var outlineBorderPaddingBoundaryWidthDp: Float? = null
        var outlineBorderPaddingBoundaryColor: Int? = null

        var transformationBitmapList: Array<out Transformation<Bitmap>>? = null
        var disableTransition: Boolean = false
        var isWithCrossFade: Boolean = false
        var withCrossFadeDuration: Int = 0

        var isGoneViewIfError: Boolean = false

        var isSkipMemoryCache: Boolean = false
        var diskCacheStrategy: DiskCacheStrategy? = null
        var priority: Priority? = null

        var drawableLoadListener: InterparkImageDrawableLoadListener? = null
        var bitmapLoadListener: InterparkImageBitmapLoadListener? = null

        var viewTargetListener: InterparkImageViewTargetListener? = null


        fun apply(imageView: ImageView, requestManager: RequestManager? = null, controller: ImageLoadController) {
            controller.loadImageByGlide(imageView.context, imageView, null, requestManager, this)
        }

        fun apply(ivTarget: ImageViewTarget<*>, requestManager: RequestManager? = null, controller: ImageLoadController) {
            controller.loadImageByGlide(ivTarget.view.context, null, ivTarget, requestManager, this)
        }

        fun apply(context: Context, requestManager: RequestManager? = null, controller: ImageLoadController) {
            controller.loadImageByGlide(context, null, null, requestManager, this)
        }

    }

}
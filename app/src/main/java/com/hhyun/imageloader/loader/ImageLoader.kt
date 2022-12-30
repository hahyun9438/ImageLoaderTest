package com.hhyun.imageloader.loader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.DrawableImageViewTarget
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class ImageLoader {


    private var loadController = ImageLoadController()


    class Builder(private val requestManager: RequestManager? = null) {

        private val param = ImageLoadController.LoadParam()


        fun loadImage(url: String?): Builder {
            param.imageUrl = url
            return this
        }

        fun loadImage(@RawRes @DrawableRes resId: Int?): Builder {
            param.imageResId = resId
            return this
        }

        fun loadImage(bitmap: Bitmap?): Builder {
            param.isBitmap = true
            param.imageBitmap = bitmap
            return this
        }

        fun loadImage(uri: Uri?): Builder {
            param.imageUri = uri
            return this
        }

        fun loadGif(@RawRes @DrawableRes gif: Int?): Builder {
            param.gif = gif
            return this
        }

        fun scaleType(scaleType: ImageView.ScaleType? = null): Builder {
            param.imageScaleType = scaleType
            return this
        }



        //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

        fun placeholder(@DrawableRes placeholder: Int? = null, scaleType: ImageView.ScaleType? = null): Builder {
            param.placeholderResId = placeholder
            param.placeholderScaleType = scaleType
            return this
        }
        fun placeholder(placeholder: Drawable? = null): Builder {
            param.placeholder = placeholder
            return this
        }
        fun usedImageRequestOptionsForPlaceholder(enable: Boolean): Builder {
            param.usedImageRequestOptionsForPlaceholder = enable
            return this
        }

        fun error(@DrawableRes error: Int? = null, scaleType: ImageView.ScaleType? = null): Builder {
            param.errorResId = error
            param.errorScaleType = scaleType
            return this
        }
        fun error(error: Drawable? = null): Builder {
            param.error = error
            return this
        }
        fun usedImageRequestOptionsForError(enable: Boolean): Builder {
            param.usedImageRequestOptionsForError = enable
            return this
        }

        fun thumbnail(ratio: Float): Builder {
            param.thumbnailRatio = ratio
            return this
        }
        fun thumbnail(@DrawableRes thumbnail: Int? = null): Builder {
            param.thumbnailResId = thumbnail
            return this
        }
        fun thumbnail(thumbnail: Drawable? = null): Builder {
            param.thumbnail = thumbnail
            return this
        }
        fun usedImageRequestOptionsForThumbnail(enable: Boolean): Builder {
            param.usedImageRequestOptionsForThumbnail = enable
            return this
        }




        //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
        fun overrideSize(width: Int, height: Int): Builder {
            param.overrideWidth = width
            param.overrideHeight = height
            return this
        }

        fun overrideOriginalSize(): Builder {
            param.isOverrideOriginalSize = true
            return this
        }

        fun radius(dp: Int): Builder {
            param.radiusDp = dp
            param.radiusType = RoundedCornersTransformation.CornerType.ALL
            return this
        }

        fun radius(dp: Int, radiusType: RoundedCornersTransformation.CornerType): Builder {
            param.radiusDp = dp
            param.radiusType = radiusType
            return this
        }

        fun isCircleCrop(isCircle: Boolean): Builder {
            param.isCircleCrop = isCircle
            return this
        }

        fun colorFilter(colorRRGGBB: String?, alpha: Float): Builder {  // ex. colorRRGGBB = "ef3e43", alpha = 0.2f
            if (colorRRGGBB.isNullOrEmpty()) return this
            if (colorRRGGBB.length != 6) return this
            try {
                val colorName = "#${Integer.toHexString((255 * alpha).toInt())}${colorRRGGBB}"
                param.colorFilter = Color.parseColor(colorName)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return this
        }

        fun colorFilter(colorAARRGGBB: String?): Builder {  // ex. colorAARRGGBB = "7fef3e43" || "#7fef3e43"
            if (colorAARRGGBB.isNullOrEmpty()) return this

            val validCode = (colorAARRGGBB.length == 9 && colorAARRGGBB.startsWith("#"))
                    || (colorAARRGGBB.length == 8 && !colorAARRGGBB.startsWith("#"))

            if (!validCode) return this

            try {
                val colorName = if (colorAARRGGBB.startsWith("#")) colorAARRGGBB else "#$colorAARRGGBB"
                param.colorFilter = Color.parseColor(colorName)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return this
        }

        fun colorFilter(context: Context?, @ColorRes colorResId: Int): Builder {
            if (context == null) return this
            param.colorFilter = ContextCompat.getColor(context, colorResId)
            return this
        }

        fun inlineBorder(@DrawableRes border: Int): Builder {
            param.inlineBorderResId = border
            return this
        }

        fun outlineBorder(
            widthDp: Float,
            @ColorInt colorInt: Int,
            paddingDp: Float,
            marginDp: Float,
            paddingBoundaryWidthDp: Float,
            @ColorInt paddingBoundaryColor: Int
        ): Builder {
            param.outlineBorderWidthDp = widthDp
            param.outlineBorderColor = colorInt
            param.outlineBorderPaddingDp = paddingDp
            param.outlineBorderMarginDp = marginDp
            param.outlineBorderPaddingBoundaryWidthDp = paddingBoundaryWidthDp
            param.outlineBorderPaddingBoundaryColor = paddingBoundaryColor
            return this
        }

        fun outlineBorder(
            context: Context?,
            widthDp: Float,
            @ColorRes colorResId: Int,
            paddingDp: Float,
            marginDp: Float,
            paddingBoundaryWidthDp: Float,
            @ColorRes paddingBoundaryColorResId: Int
        ): Builder {
            if (context == null) return this
            param.outlineBorderWidthDp = widthDp
            param.outlineBorderColor = ContextCompat.getColor(context, colorResId)
            param.outlineBorderPaddingDp = paddingDp
            param.outlineBorderMarginDp = marginDp
            param.outlineBorderPaddingBoundaryWidthDp = paddingBoundaryWidthDp
            param.outlineBorderPaddingBoundaryColor = ContextCompat.getColor(context, paddingBoundaryColorResId)
            return this
        }

        fun blur(blur: Int): Builder {
            param.blur = blur
            return this
        }

        fun transform(context: Context, transforms: String?): Builder {

            transforms?.split(ImageLoadTransform.SEPARATOR_SECTION)?.forEach { transform ->
                when {

                    // color_filter,000000,0.2
                    transform.startsWith(ImageLoadTransform.COLOR_FILTER) -> {
                        val colorHexCode = transform.split(ImageLoadTransform.SEPARATOR_TYPE).getOrNull(1)
                        val alpha = transform.split(ImageLoadTransform.SEPARATOR_TYPE).getOrNull(2)?.toFloatOrNull()?.coerceAtLeast(0f) ?: 1f
                        colorFilter(colorHexCode, alpha)
                    }

                    // border_overlay,rect_transparent_border_19000000_radius_13
                    transform.startsWith(ImageLoadTransform.BORDER_OVERLAY) -> {
                        val overlayDrawableName = transform.split(ImageLoadTransform.SEPARATOR_TYPE).getOrNull(1)
                        context.resources.getIdentifier(overlayDrawableName, "drawable", context.packageName).takeIf { it > 0 }?.let { id ->
                            inlineBorder(id)
                        }
                    }

                    // blur,50
                    transform.startsWith(ImageLoadTransform.BLUR) -> {
                        blur(transform.split(ImageLoadTransform.SEPARATOR_TYPE).getOrNull(1)?.toInt() ?: 0)
                    }

                    transform.startsWith(ImageLoadTransform.ROUND)                         // round,10
                            || transform.startsWith(ImageLoadTransform.ROUND_LEFT)         // round_left,10
                            || transform.startsWith(ImageLoadTransform.ROUND_TOP)          // round_top,10
                            || transform.startsWith(ImageLoadTransform.ROUND_RIGHT)        // round_right,10
                            || transform.startsWith(ImageLoadTransform.ROUND_BOTTOM)       // round_bottom,10
                            || transform.startsWith(ImageLoadTransform.ROUND_TOP_LEFT)     // round_top_left,10
                            || transform.startsWith(ImageLoadTransform.ROUND_TOP_RIGHT)    // round_top_right,10
                            || transform.startsWith(ImageLoadTransform.ROUND_BOTTOM_LEFT)  // round_bottom_left,10
                            || transform.startsWith(ImageLoadTransform.ROUND_BOTTOM_RIGHT) // round_bottom_right,10
                    -> {
                        try {
                            val roundType = transform.split(ImageLoadTransform.SEPARATOR_TYPE).getOrNull(0)
                            val dp = transform.split(ImageLoadTransform.SEPARATOR_TYPE).getOrNull(1)?.toInt()
                            dp?.let { radius(it, ImageLoadTransform.getRadiusType(roundType)) }
                        } catch (e: Exception) {}
                    }

                    // circle_crop
                    transform == ImageLoadTransform.CIRCLE_CROP -> {
                        isCircleCrop(true)
                    }

                    // center_crop
                    transform == ImageLoadTransform.CENTER_CROP -> {
                        param.imageScaleType = ImageView.ScaleType.CENTER_CROP
                    }

                    // fit_center
                    transform == ImageLoadTransform.FIT_CENTER -> {
                        param.imageScaleType = ImageView.ScaleType.FIT_CENTER
                    }

                }
            }

            return this
        }


        fun disableTransition(isDisable: Boolean): Builder {
            param.disableTransition = isDisable
            return this
        }

        fun isWithCrossFade(isWith: Boolean): Builder {
            param.isWithCrossFade = isWith
            return this
        }

        fun isWithCrossFade(isWith: Boolean, duration: Int): Builder {
            param.isWithCrossFade = isWith
            param.withCrossFadeDuration = duration
            return this
        }

        fun isGoneViewIfError(isGone: Boolean): Builder {
            param.isGoneViewIfError = isGone
            return this
        }

        fun isBimap(): Builder {
            param.isBitmap = true
            return this
        }

        fun transformationBitmap(vararg transformationBitmapList: Transformation<Bitmap>): Builder {
            param.transformationBitmapList = transformationBitmapList
            return this
        }

        //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

        /**
         * Glide의 메모리 캐싱을 skip 하고자 할때 호출.
         * @param isSkip true인 경우 메모리 캐싱이 스킵됨.
         */
        fun skipMemoryCache(isSkip: Boolean): Builder {
            param.isSkipMemoryCache = isSkip
            return this
        }

        /**
         * Glide의 디스크 캐싱 전략을 설정 할 때 호출.
         * @param diskCacheType 캐싱 전략 타입
         */
        fun diskCacheStrategy(diskCacheType: DiskCacheStrategy): Builder {
            param.diskCacheStrategy = diskCacheType
            return this
        }

        fun priority(priority: Priority): Builder {
            param.priority = priority
            return this
        }




        //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

        fun setLoadListener(listener: InterparkImageDrawableLoadListener): Builder {
            param.drawableLoadListener = listener
            return this
        }

        fun setLoadListener(listener: InterparkImageBitmapLoadListener): Builder {
            param.bitmapLoadListener = listener
            return this
        }

        fun setViewTargetListener(listener: InterparkImageViewTargetListener): Builder {
            param.viewTargetListener = listener
            return this
        }



        //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
        fun into(imageView: ImageView) {
            val loader = ImageLoader()
            param.apply(imageView, requestManager, loader.loadController)
        }

        fun into(ivTarget: DrawableImageViewTarget) {
            val loader = ImageLoader()
            param.apply(ivTarget, requestManager, loader.loadController)
        }

        fun into(ivTarget: BitmapImageViewTarget) {
            val loader = ImageLoader()
            param.apply(ivTarget, requestManager, loader.loadController)
        }

        fun submit(context: Context?) {
            if (context == null) return
            val loader = ImageLoader()
            param.apply(context, requestManager, loader.loadController)
        }

        fun submit(context: Context?, width: Int, height:Int, bitmap: (Bitmap?) -> Unit){

            if (context == null) {
                bitmap(null)
                return
            }
            param.bitmapWidth = width
            param.bitmapHeight = height
            param.bitmapLoadListener = object:InterparkImageBitmapLoadListener{
                override fun onResourceReady(resource: Bitmap?) {
                    bitmap(resource)
                }

                override fun onLoadFailed() {
                    bitmap(null)
                }
            }
            param.apply(context, requestManager, ImageLoader().loadController)

        }

    }

}
package com.hhyun.imageloader.test

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.hhyun.imageloader.R
import com.hhyun.imageloader.Util
import com.hhyun.imageloader.databinding.ActivityImageLoaderBinding
import com.hhyun.imageloader.loader.ImageLoader
import com.hhyun.imageloader.loader.transform.BorderTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class ImageLoaderActivity: AppCompatActivity() {

    companion object {
        const val TEST_IMAGE_URL = "https://cdn.pixabay.com/photo/2021/12/05/12/29/christmas-tree-6847584_640.jpg"
    }

    private lateinit var binding: ActivityImageLoaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityImageLoaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        load()
    }

    private fun load() {
        val requestManager = Glide.with(this)

//        val bitmap1 = ImageLoader.getBitmap(this, TEST_IMAGE_URL)
//        Log.d("hahTest", "load bitmap1 = ${bitmap1 != null}")
        val dp = Util.dpToFloatPx(this, 1).toInt()

        ImageLoader.Builder(requestManager)
            .loadImage(TEST_IMAGE_URL)
            .placeholder(R.drawable.noimage, ImageView.ScaleType.CENTER_INSIDE)
            .scaleType(ImageView.ScaleType.CENTER_INSIDE)
            .error(R.drawable.noimage)
            .disableTransition(false)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(DrawableImageViewTarget(binding.iv1))

        ImageLoader.Builder(requestManager)
            .loadImage(TEST_IMAGE_URL)
            .isBimap()
            .placeholder(R.drawable.noimage)
            .error(R.drawable.noimage)
            .disableTransition(true)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(BitmapImageViewTarget(binding.iv12))

        ImageLoader.Builder(requestManager)
            .loadImage(TEST_IMAGE_URL)
            .isCircleCrop(true)
            .placeholder(R.drawable.noimage)
            .error(R.drawable.noimage)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.iv2)

        ImageLoader.Builder(requestManager)
            .loadImage(TEST_IMAGE_URL)
            .isCircleCrop(true)
            .placeholder(R.drawable.noimage)
            .error(R.drawable.noimage)
            .colorFilter(this, R.color.color_4d000000)
//            .colorFilter("#7fef3e43")
//            .colorFilter("ef3e43", 0.5f)
            .inlineBorder(R.drawable.circle_transparent_border_19000000_radius_13)
            .skipMemoryCache(true)
            .into(binding.iv22)

        ImageLoader.Builder(requestManager)
            .loadImage(TEST_IMAGE_URL)
            .isCircleCrop(true)
            .placeholder(R.drawable.noimage)
            .error(R.drawable.noimage)
            .blur(10)
            .skipMemoryCache(true)
            .into(binding.iv23)

        ImageLoader.Builder(requestManager)
            .loadImage(TEST_IMAGE_URL)
            .placeholder(R.drawable.noimage)
            .error(R.drawable.noimage)
            .transform(this, "color_filter,000000,0.2|border_overlay,rect_transparent_border_19000000_radius_left_13|round_left,13|center_crop")
            .skipMemoryCache(true)
            .into(binding.iv24)

        ImageLoader.Builder(requestManager)
//            .loadImage("")
            .loadImage(R.drawable.paris)
            .placeholder(R.drawable.noimage_r13)
            .usedImageRequestOptionsForPlaceholder(true)
            .error(R.drawable.noimage_r13)
            .radius(13)
            .inlineBorder(R.drawable.rect_transparent_border_19000000_radius_13)
            .priority(Priority.IMMEDIATE)
            .isWithCrossFade(true)
            .skipMemoryCache(true)
            .into(binding.iv3)

        ImageLoader.Builder(requestManager)
            .loadImage(TEST_IMAGE_URL)
            .placeholder(R.drawable.noimage)
            .error(R.drawable.noimage)
            .thumbnail(R.drawable.noimage)
            .usedImageRequestOptionsForThumbnail(true)
            .radius(13)
            .inlineBorder(R.drawable.rect_transparent_border_19000000_radius_13)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.iv32)

        ImageLoader.Builder(requestManager)
            .loadImage(Uri.parse(TEST_IMAGE_URL))
            .placeholder(R.drawable.noimage)
            .error(R.drawable.noimage)
            .overrideSize(50, 50)
            .skipMemoryCache(true)
            .into(binding.iv33)


        ImageLoader.Builder(requestManager)
            .loadImage(TEST_IMAGE_URL)
            .scaleType(ImageView.ScaleType.CENTER_CROP)
            .placeholder(R.drawable.noimage)
            .error(R.drawable.noimage)
//            .radius(6)
            .transformationBitmap(BorderTransformation(binding.iv4.id)
                .setBorderWidth(dp.toFloat()*4)
                .setMargin(2 * dp.toFloat()*4)
                .setPadding(2 * dp.toFloat()*4)
                .setPaddingBoundaryBorderWidth(1f))
            .skipMemoryCache(true)
            .into(binding.iv4)

        ImageLoader.Builder(requestManager)
            .loadImage(TEST_IMAGE_URL)
            .placeholder(R.drawable.noimage)
            .usedImageRequestOptionsForPlaceholder(true)
            .error(R.drawable.noimage)
            .outlineBorder(this, 2f, R.color.color_dc941b, 2f, 2f, 1f, R.color.color_8b6bff)
            .skipMemoryCache(true)
            .into(binding.iv42)

        ImageLoader.Builder(requestManager)
            .loadImage(TEST_IMAGE_URL)
            .placeholder(R.drawable.noimage)
            .usedImageRequestOptionsForPlaceholder(true)
            .error(R.drawable.noimage)
            .isCircleCrop(true)
            .outlineBorder(this, 2f, R.color.color_dc941b, 2f, 2f, 1f, R.color.color_8b6bff)
            .skipMemoryCache(true)
            .into(binding.iv43)

        ImageLoader.Builder(requestManager)
            .loadImage(TEST_IMAGE_URL)
            .scaleType(ImageView.ScaleType.CENTER_CROP)
            .placeholder(R.drawable.noimage)
            .error(R.drawable.noimage)
            .radius(10, RoundedCornersTransformation.CornerType.TOP)
            .outlineBorder(this, 2f, R.color.color_dc941b, 2f, 2f, 1f, R.color.color_8b6bff)
            .skipMemoryCache(true)
            .into(binding.iv44)

        ImageLoader.Builder(requestManager)
            .loadImage(TEST_IMAGE_URL)
            .placeholder(R.drawable.noimage)
            .usedImageRequestOptionsForPlaceholder(true)
            .error(R.drawable.noimage)
            .radius(6)
            .outlineBorder(this, 2f, R.color.color_dc941b, 0f, 0f, 0f, R.color.transparent)
            .skipMemoryCache(true)
            .into(binding.iv45)


        ImageLoader.Builder(requestManager)
            .loadGif(R.raw.test)
            .scaleType(ImageView.ScaleType.CENTER_INSIDE)
            .placeholder(R.drawable.noimage)
            .error(R.drawable.noimage)
            .into(binding.iv5)

        ImageLoader.Builder(requestManager)
            .loadImage(TEST_IMAGE_URL)
            .placeholder(R.drawable.noimage)

    }

    private fun setImageResource(imageView: ImageView, resource: Drawable?) {
        imageView.setImageDrawable(resource)
//        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
    }

}
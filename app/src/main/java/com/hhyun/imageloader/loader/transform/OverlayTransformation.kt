package com.hhyun.imageloader.loader.transform

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import jp.wasabeef.glide.transformations.BitmapTransformation
import jp.wasabeef.glide.transformations.internal.Utils
import java.security.MessageDigest

class OverlayTransformation(val overlayId: Int) : BitmapTransformation() {

    companion object {
        private val VERSION = 1
        private val ID = "com.hhyun.imageloader.loader.transform.OverlayTransformation.$VERSION"
    }

    private val paint = Paint()

    override fun transform(context: Context, pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val width = toTransform.width
        val height = toTransform.height

        val bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        canvas.drawBitmap(toTransform, 0f, 0f, paint)

        val mask = Utils.getMaskDrawable(context.applicationContext, overlayId)
        mask.setBounds(0, 0, width, height)
        mask.draw(canvas)

        return bitmap
    }

    override fun toString(): String {
        return ("OverlayTransformation(overlayId=$overlayId)")
    }

    override fun equals(obj: Any?): Boolean {
        return obj is OverlayTransformation
                && obj.overlayId == overlayId
    }

    override fun hashCode(): Int {
        return ID.hashCode() + overlayId * 10
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((ID + overlayId).toByteArray(CHARSET))
    }
}
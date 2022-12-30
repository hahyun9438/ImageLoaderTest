package com.hhyun.imageloader.loader.transform

import android.graphics.*
import android.os.Build
import androidx.annotation.ColorInt
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.security.MessageDigest

class BorderTransformation(val viewId: Int): BitmapTransformation() {

    companion object {
        private val VERSION = 1
        private val ID = "com.hhyun.imageloader.loader.transform.BorderTransformation.$VERSION"
    }


    private var mBackgroundColor: Int = Color.TRANSPARENT

    private var mBorderColor: Int = Color.BLACK
    private var mBorderWidthPx: Float = 0f

    private var mMarginPx: Float = 0f
    private var mPaddingPx: Float = 0f
    private var mPaddingColor: Int = Color.TRANSPARENT

    private var mPaddingBoundaryBorderColor: Int = Color.LTGRAY
    private var mPaddingBoundaryBorderWidthPx: Float = 0f

    private var mIsCircle: Boolean = false

    private var mIsRounded: Boolean = false
    private var mRadiusLeftTopPx: Float = 0f
    private var mRadiusRightTopPx: Float = 0f
    private var mRadiusRightBottomPx: Float = 0f
    private var mRadiusLeftBottomPx: Float = 0f


    fun setBackgroundColor(@ColorInt backgroundColor: Int?): BorderTransformation {
        this.mBackgroundColor = backgroundColor ?: Color.TRANSPARENT
        return this
    }

    fun setBorderColor(@ColorInt borderColor: Int?): BorderTransformation {
        this.mBorderColor = borderColor ?: Color.TRANSPARENT
        return this
    }

    fun setBorderWidth(borderWidthDp: Float?): BorderTransformation {
        this.mBorderWidthPx = borderWidthDp ?: 0f
        return this
    }

    fun setMargin(marginDp: Float?): BorderTransformation {
        this.mMarginPx = marginDp ?: 0f
        return this
    }

    fun setPadding(paddingDp: Float?): BorderTransformation {
        this.mPaddingPx = paddingDp ?: 0f
        return this
    }

    fun setPaddingColor(@ColorInt paddingColor: Int?): BorderTransformation {
        this.mPaddingColor = paddingColor ?: Color.TRANSPARENT
        return this
    }

    fun setPaddingBoundaryBorderColor(@ColorInt paddingBoundaryBorderColor: Int?): BorderTransformation {
        this.mPaddingBoundaryBorderColor = paddingBoundaryBorderColor ?: Color.TRANSPARENT
        return this
    }

    fun setPaddingBoundaryBorderWidth(paddingBoundaryBorderWidthDp: Float?): BorderTransformation {
        this.mPaddingBoundaryBorderWidthPx = paddingBoundaryBorderWidthDp ?: 0f
        return this
    }

    fun isCircle(isCircle: Boolean?): BorderTransformation {
        this.mIsCircle = isCircle ?: false
        return this
    }

    fun radius(radiusPx: Int, cornerType: RoundedCornersTransformation.CornerType): BorderTransformation {

        this.mRadiusLeftTopPx = if(cornerType == RoundedCornersTransformation.CornerType.ALL
            || cornerType == RoundedCornersTransformation.CornerType.LEFT
            || cornerType == RoundedCornersTransformation.CornerType.TOP
            || cornerType == RoundedCornersTransformation.CornerType.TOP_LEFT
        ) {
            radiusPx.toFloat()
        } else 0f

        this.mRadiusRightTopPx = if(cornerType == RoundedCornersTransformation.CornerType.ALL
            || cornerType == RoundedCornersTransformation.CornerType.RIGHT
            || cornerType == RoundedCornersTransformation.CornerType.TOP
            || cornerType == RoundedCornersTransformation.CornerType.TOP_RIGHT
        ) {
            radiusPx.toFloat()
        } else 0f

        this.mRadiusRightBottomPx = if(cornerType == RoundedCornersTransformation.CornerType.ALL
            || cornerType == RoundedCornersTransformation.CornerType.RIGHT
            || cornerType == RoundedCornersTransformation.CornerType.BOTTOM
            || cornerType == RoundedCornersTransformation.CornerType.BOTTOM_RIGHT
        ) {
            radiusPx.toFloat()
        } else 0f

        this.mRadiusLeftBottomPx = if(cornerType == RoundedCornersTransformation.CornerType.ALL
            || cornerType == RoundedCornersTransformation.CornerType.LEFT
            || cornerType == RoundedCornersTransformation.CornerType.BOTTOM
            || cornerType == RoundedCornersTransformation.CornerType.BOTTOM_LEFT
        ) {
            radiusPx.toFloat()
        } else 0f

        if(mRadiusLeftTopPx > 0
            || mRadiusRightTopPx > 0
            || mRadiusRightBottomPx > 0
            || mRadiusLeftBottomPx > 0) {
            mIsRounded = true
        }

        return this
    }


    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {

        val resultSize = outWidth.coerceAtMost(outHeight)
        val bitmapCropSize = toTransform.width.coerceAtMost(toTransform.height)

        val r = resultSize / 2f
        val x = (toTransform.width - bitmapCropSize) / 2
        val y = (toTransform.height - bitmapCropSize) / 2

        val radii = FloatArray(8).apply {
            this[0] = mRadiusLeftTopPx
            this[1] = mRadiusLeftTopPx
            this[2] = mRadiusRightTopPx
            this[3] = mRadiusRightTopPx
            this[4] = mRadiusRightBottomPx
            this[5] = mRadiusRightBottomPx
            this[6] = mRadiusLeftBottomPx
            this[7] = mRadiusLeftBottomPx
        }

        // center crop 된 정사각형 비트맵
        val squared = Bitmap.createBitmap(toTransform, x, y, bitmapCropSize, bitmapCropSize)

        // 보여질 뷰에 맞춘 비트맵
        var result: Bitmap? = pool[resultSize, resultSize, Bitmap.Config.ARGB_8888]
        if (result == null) {
            result = Bitmap.createBitmap(resultSize, resultSize, Bitmap.Config.ARGB_8888)
        }

        // 캔버스에 그리기
        val canvas = Canvas(result!!)
        canvas.drawColor(mBackgroundColor)

        val paint = Paint()
        paint.isAntiAlias = true

        // bitmap을 작게 그린다
        val bitmapBounds = (mMarginPx + mBorderWidthPx + mPaddingPx + mPaddingBoundaryBorderWidthPx).toInt()
        canvas.drawBitmap(
            squared,
            Rect(0, 0, bitmapCropSize, bitmapCropSize),
            Rect(bitmapBounds, bitmapBounds, resultSize - bitmapBounds, resultSize - bitmapBounds),
            paint
        )


        // -------------------------------------------------
        // 이미지 프레임
        val path = Path()
        path.addRect(0f, 0f, resultSize.toFloat(), resultSize.toFloat(), Path.Direction.CW)

        when {
            mIsCircle -> {
                path.addCircle(r, r, mMarginPx - mBorderWidthPx - mPaddingPx - mPaddingBoundaryBorderWidthPx, Path.Direction.CCW)
            }

            mIsRounded -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    path.addRoundRect(0f, 0f, resultSize.toFloat(), resultSize.toFloat(), radii, Path.Direction.CW)
                }
            }

            else -> {
                path.addRect(0f, 0f, resultSize.toFloat(), resultSize.toFloat(), Path.Direction.CW)
            }
        }

        paint.color = mPaddingColor
        canvas.drawPath(path, paint)
        path.reset()


        // -------------------------------------------------
        // 바깥쪽 테두리
        paint.style = Paint.Style.STROKE
        paint.color = mBorderColor
        paint.strokeWidth = mBorderWidthPx

        when {
            mIsCircle -> canvas.drawCircle(r, r, r - mMarginPx, paint)

            mIsRounded -> {
                val offset = mBorderWidthPx / 2f
                val left = 0f + offset
                val top = 0f + offset
                val right = bitmapCropSize.toFloat() - offset
                val bottom = bitmapCropSize.toFloat() - offset
                val rectF = RectF(left, top, right, bottom)

                val borderPath = Path()
                borderPath.addRoundRect(rectF, radii, Path.Direction.CW)
                canvas.drawPath(borderPath, paint)
            }

            else -> {
                val offset = mBorderWidthPx / 2f
                val left = 0f + offset
                val top = 0f + offset
                val right = bitmapCropSize.toFloat() - offset
                val bottom = bitmapCropSize.toFloat() - offset
                val rectF = RectF(left, top, right, bottom)

                val borderPath = Path()
                borderPath.addRect(rectF, Path.Direction.CW)
                canvas.drawPath(borderPath, paint)
            }
        }


        // -------------------------------------------------
        // 이미지 테두리
        paint.color = mPaddingBoundaryBorderColor
        paint.strokeWidth = mPaddingBoundaryBorderWidthPx

        when {
            mIsCircle -> canvas.drawCircle(r, r, r - mMarginPx - mPaddingPx - mBorderWidthPx, paint)

            mIsRounded -> {
                val offset = mPaddingBoundaryBorderWidthPx / 2f
                val left = bitmapBounds.toFloat() - offset
                val top = bitmapBounds.toFloat() - offset
                val right = resultSize.toFloat() - bitmapBounds.toFloat() + offset
                val bottom = resultSize.toFloat() - bitmapBounds.toFloat() + offset
                val rectF = RectF(left, top, right, bottom)

                val boundaryBorderPath = Path()
                boundaryBorderPath.addRoundRect(rectF, radii, Path.Direction.CW)
                canvas.drawPath(boundaryBorderPath, paint)
            }

            else -> {
                val offset = mPaddingBoundaryBorderWidthPx / 2f
                val left = bitmapBounds.toFloat() - offset
                val top = bitmapBounds.toFloat() - offset
                val right = resultSize.toFloat() - bitmapBounds.toFloat() + offset
                val bottom = resultSize.toFloat() - bitmapBounds.toFloat() + offset
                val rectF = RectF(left, top, right, bottom)

                val boundaryBorderPath = Path()
                boundaryBorderPath.addRect(rectF, Path.Direction.CW)
                canvas.drawPath(boundaryBorderPath, paint)
            }
        }


        squared.recycle()
        return result
    }



    override fun toString(): String {
        return "BorderTransformation(viewId=$viewId)"
    }

    override fun hashCode(): Int {
        return ID.hashCode() + viewId * 10
    }

    override fun equals(obj: Any?): Boolean {
        return obj is BorderTransformation
                && obj.viewId == viewId
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((ID + viewId).toByteArray(CHARSET))
    }
}
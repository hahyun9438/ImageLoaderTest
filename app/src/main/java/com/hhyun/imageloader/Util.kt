package com.hhyun.imageloader

import android.content.Context
import android.util.TypedValue

object Util {

    fun dpToPx(context: Context, dp: Int): Int {
        return dpToFloatPx(context, dp.toFloat()).toInt()
    }

    fun dpToFloatPx(context: Context, dp: Int): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), metrics)
    }

    fun dpToFloatPx(context: Context, dp: Float): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics)
    }


    fun getDeviceWidth(context: Context?): Int {
        return context?.resources?.displayMetrics?.widthPixels ?: 0
    }

    fun getDeviceHeight(context: Context?): Int {
        return context?.resources?.displayMetrics?.heightPixels ?: 0
    }
}
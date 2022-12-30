package com.hhyun.imageloader.loader

import jp.wasabeef.glide.transformations.RoundedCornersTransformation

object ImageLoadTransform {
    const val SEPARATOR_SECTION = "|"
    const val SEPARATOR_TYPE = ","

    const val COLOR_FILTER = "color_filter"
    const val BORDER_OVERLAY = "border_overlay"
    const val BLUR = "blur"

    const val ROUND = "round"
    const val ROUND_LEFT = "round_left"
    const val ROUND_TOP = "round_top"
    const val ROUND_RIGHT = "round_right"
    const val ROUND_BOTTOM = "round_bottom"
    const val ROUND_TOP_LEFT = "round_top_left"
    const val ROUND_TOP_RIGHT = "round_top_right"
    const val ROUND_BOTTOM_LEFT = "round_bottom_left"
    const val ROUND_BOTTOM_RIGHT = "round_bottom_right"

    const val CIRCLE_CROP = "circle_crop"
    const val CENTER_CROP = "center_crop"
    const val FIT_CENTER = "fit_center"


    fun getRadiusType(round: String?): RoundedCornersTransformation.CornerType {
        return when(round) {
            ROUND_LEFT -> RoundedCornersTransformation.CornerType.LEFT
            ROUND_TOP -> RoundedCornersTransformation.CornerType.TOP
            ROUND_RIGHT -> RoundedCornersTransformation.CornerType.RIGHT
            ROUND_BOTTOM -> RoundedCornersTransformation.CornerType.BOTTOM
            ROUND_TOP_LEFT -> RoundedCornersTransformation.CornerType.TOP_LEFT
            ROUND_TOP_RIGHT -> RoundedCornersTransformation.CornerType.TOP_RIGHT
            ROUND_BOTTOM_LEFT -> RoundedCornersTransformation.CornerType.BOTTOM_LEFT
            ROUND_BOTTOM_RIGHT -> RoundedCornersTransformation.CornerType.BOTTOM_RIGHT
            else -> RoundedCornersTransformation.CornerType.ALL
        }
    }
}
package com.lingmiao.shop.util

import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.TranslateAnimation

/**
 * Author : Elson
 * Date   : 2020/7/15
 * Desc   : 位移动画
 */
fun showXTranslateAnim(duration: Long = 350, interpolator: Interpolator = DecelerateInterpolator()): Animation {
    val animation: Animation = TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, 1f,
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, 0f
    )
    animation.interpolator = interpolator
    animation.duration = duration
    return animation
}

fun hideXTranslateAnim(duration: Long = 350, interpolator: Interpolator = DecelerateInterpolator()): Animation {
    val animation: Animation = TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, 1f,
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, 0f
    )
    animation.interpolator = interpolator
    animation.duration = duration
    return animation
}


fun showYTranslateAnim(duration: Long = 350, interpolator: Interpolator = DecelerateInterpolator()): Animation {
    val animation: Animation = TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, 1f,
        Animation.RELATIVE_TO_PARENT, 0f
    )
    animation.interpolator = interpolator
    animation.duration = duration
    return animation
}

fun hideYTranslateAnim(duration: Long = 350, interpolator: Interpolator = DecelerateInterpolator()): Animation {
    val animation: Animation = TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, 1f
    )
    animation.interpolator = interpolator
    animation.duration = duration
    return animation
}



fun showYTranslateAnimOfTopToBottom(duration: Long = 350, interpolator: Interpolator = DecelerateInterpolator()): Animation {
    val animation: Animation = TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, -1f,
        Animation.RELATIVE_TO_PARENT, 0f
    )
    animation.interpolator = interpolator
    animation.duration = duration
    return animation
}

fun hideYTranslateAnimOfBottomToTop(duration: Long = 350, interpolator: Interpolator = DecelerateInterpolator()): Animation {
    val animation: Animation = TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, 0f,
        Animation.RELATIVE_TO_PARENT, -1f
    )
    animation.interpolator = interpolator
    animation.duration = duration
    return animation
}

package com.lingmiao.shop.util

import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.annotation.DrawableRes

/**
Create Date : 2021/6/215:02 PM
Auther      : Fox
Desc        :
 **/
fun TextView.drawable(left: Drawable?, top: Drawable?, right: Drawable?, bottom: Drawable?) {
    apply {
        setCompoundDrawables(left, top, right, bottom);
    }
}

fun TextView.drawableLeft(left: Drawable) {
    drawable(left, null, null, null);
}

fun TextView.drawableTop(top: Drawable) {
    drawable(null, top, null, null);
}

fun TextView.drawableRight(right: Drawable) {
    drawable(null, null, right, null);
}

fun TextView.drawableBottom(bottom: Drawable) {
    drawable(null, null, null, bottom);
}

fun TextView.res(@DrawableRes left: Int?,@DrawableRes top: Int?,@DrawableRes right: Int?,@DrawableRes bottom: Int?) {
    apply {
        setCompoundDrawablesWithIntrinsicBounds(left?:0, top?:0, right?:0, bottom?:0);
    }
}

fun TextView.drawableLeft(@DrawableRes left: Int) {
    res(left, null, null, null);
}

fun TextView.drawableTop(@DrawableRes top: Int) {
    res(null, top, null, null);
}

fun TextView.drawableRight(@DrawableRes right: Int) {
    res(null, null, right, null);
}

fun TextView.drawableBottom(@DrawableRes bottom: Int) {
    res(null, null, null, bottom);
}

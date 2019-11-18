package com.osome.stickydecorator

import android.content.res.Resources

val Int.px2dpF: Float
    get() = (this * Resources.getSystem().displayMetrics.density)

val Int.px2dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

interface ItemProvider<T> {
    fun get(position: Int): T

    fun size(): Int
}
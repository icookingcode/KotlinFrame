package com.guc.kframe.system

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.guc.kframe.R
import com.guc.kframe.base.BaseSystem
import com.guc.kframe.utils.LogG

/**
 * Created by guc on 2020/6/9.
 * 描述：图片加载工具
 */
class SystemImageLoader : BaseSystem() {
    override fun initSystem() {
    }

    override fun destroy() {
    }

    fun loadImage(context: Context, resId: Int, targetView: ImageView) {
        Glide.with(context).load(resId).into(targetView)
    }

    fun loadImage(tag: Any, url: String, targetView: ImageView) {
        loadImage(tag, url, targetView, -1, -1)
    }

    fun loadImage(
        tag: Any,
        url: String,
        targetView: ImageView,
        @DrawableRes placeHolderId: Int,
        @DrawableRes errorId: Int
    ) {
        val pid = if (placeHolderId == -1) R.drawable.ic_place_pic else placeHolderId
        val eid = if (errorId == -1) R.drawable.ic_place_pic else errorId
        val options = RequestOptions().placeholder(pid).error(eid)
        when (tag) {
            is FragmentActivity -> Glide.with(tag).load(url).apply(options)
                .into(targetView)
            is Activity -> Glide.with(tag).load(url).apply(options)
                .into(targetView)
            is Context -> Glide.with(tag).load(url).apply(options)
                .into(targetView)
            is Fragment -> Glide.with(tag).load(url).apply(options)
                .into(targetView)
            is View -> Glide.with(tag).load(url).apply(options)
                .into(targetView)
            else -> LogG.loge(TAG, "the tag is error")
        }
    }
}
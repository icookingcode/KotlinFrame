package com.guc.kframe.system

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.guc.kframe.R
import com.guc.kframe.base.BaseSystem
import com.guc.kframe.widget.WaterMarkView
import kotlin.math.PI

/**
 * Created by guc on 2020/6/12.
 * Description：水印实现工具
 */
class SystemWaterMark : BaseSystem() {
    companion object {
        const val VIEW_TAG = "view_tag"
    }

    var enable = false
    var text = "水印"
    var angle = 30
    var color: Int = -1
    var textSize: Int = -1
    var lineHeight: Int = -1
    var textAlpha: Float = -1f
    var markerSpace: Int = -1  //水印间距
    var lineSpace = -1 //多行行间距
    override fun initSystem() {
    }

    override fun destroy() {
    }

    @SuppressLint("InflateParams")
    fun onActivityStart(activity: Activity) {
        val rootView = activity.window.decorView.findViewById<ViewGroup>(android.R.id.content)
        if (enable && text.isNotEmpty() && rootView.findViewWithTag<View>(VIEW_TAG) == null) {
            val waterMarkView =
                LayoutInflater.from(activity)
                    .inflate(R.layout.layout_water_mark, null) as WaterMarkView
            waterMarkView.tag = VIEW_TAG
            waterMarkView.markText = text
            waterMarkView.radian = (angle / 180.0 * PI).toFloat()
            if (color != -1) {
                waterMarkView.markerTextColor = color
            }
            if (textSize != -1) {
                waterMarkView.markerTextSize = textSize.toFloat()
            }
            if (lineHeight != -1) {
                waterMarkView.lineHeight = lineHeight
            }
            if (textAlpha >= 0.2) {
                waterMarkView.textAlpha = textAlpha
            }
            if (markerSpace != -1) {
                waterMarkView.markerSpace = markerSpace
            }
            if (lineSpace > 0) {
                waterMarkView.lineSpace = lineSpace
            }
            rootView.addView(waterMarkView)
        }

    }

}
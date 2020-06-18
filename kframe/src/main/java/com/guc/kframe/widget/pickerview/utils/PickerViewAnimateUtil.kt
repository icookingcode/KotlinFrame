package com.guc.kframe.widget.pickerview.utils

import android.view.Gravity
import com.guc.kframe.R

/**
 * Created by guc on 2020/6/16.
 * Description：
 */
object PickerViewAnimateUtil {
    fun getAnimationResource(gravity: Int, isInAnim: Boolean) =
        when (gravity) {
            Gravity.BOTTOM -> if (isInAnim) R.anim.push_bottom_in else R.anim.push_bottom_out
            else -> if (isInAnim) R.anim.update_app_window_in else R.anim.update_app_window_out
        }
}
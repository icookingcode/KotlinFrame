package com.guc.kframe.widget.pickerview.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.guc.kframe.R
import com.guc.kframe.widget.pickerview.utils.PickerViewAnimateUtil

/**
 * Created by guc on 2020/6/17.
 * Description：
 */
typealias OnDismissListener = ((o: Any) -> Unit)?

open class BasePickerView(val context: Context) {
    private val params = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        Gravity.BOTTOM
    )
    protected var contentContainer: ViewGroup? = null
    protected var pickerview_timebtn_nor = -0xfa8201
    protected var pickerview_timebtn_pre = -0x3d250b
    protected var pickerview_bg_topbar = -0xa0a0b
    protected var pickerview_topbar_title = -0x1000000
    protected var bgColor_default = -0x1
    protected var clickView //是通过哪个View弹出的
            : View? = null
    private var decorView //activity的根View
            : ViewGroup? = null
    private var rootView //附加View 的 根View
            : ViewGroup? = null
    private var dialogView //附加Dialog 的 根View
            : ViewGroup? = null
    private val onDismissListener: OnDismissListener = null
    private var dismissing = false
    private lateinit var outAnim: Animation
    private lateinit var inAnim: Animation
    private var isShowing = false
    var gravity = Gravity.BOTTOM
    private var mDialog: Dialog? = null
    private val onCancelableTouchListener: View.OnTouchListener = View.OnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            dismiss()
        }
        false
    }
    private val onKeyBackListener: View.OnKeyListener = View.OnKeyListener { v, keyCode, event ->
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == MotionEvent.ACTION_DOWN && isShowing()
        ) {
            dismiss()
            true
        } else {
            false
        }
    }
    var cancelable //是否能取消
            = true

    protected fun initViews() {
        val layoutInflater = LayoutInflater.from(context)
        if (isDialog()) {
            //如果是对话框模式

            //如果是对话框模式
            dialogView =
                layoutInflater.inflate(R.layout.layout_basepickerview, null, false) as ViewGroup
            dialogView?.setBackgroundColor(Color.TRANSPARENT)
            contentContainer =
                dialogView!!.findViewById<View>(R.id.content_container) as ViewGroup
            //设置对话框 左右间距屏幕30
            params.leftMargin = 30
            params.rightMargin = 30
            params.gravity = gravity
            contentContainer?.layoutParams = params
            //创建对话框
            createDialog()
            //给背景设置点击事件,这样当点击内容以外的地方会关闭界面
            dialogView!!.setOnClickListener { if (cancelable) dismiss() }
        } else {
            //如果只是要显示在屏幕的下方
            //decorView是activity的根View
            decorView = (context as Activity).window.decorView
                .findViewById<View>(android.R.id.content) as ViewGroup
            //将控件添加到decorView中
            //将控件添加到decorView中
            rootView = layoutInflater.inflate(
                R.layout.layout_basepickerview,
                decorView,
                false
            ) as ViewGroup
            rootView!!.layoutParams =
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
            //这个是真正要加载时间选取器的父布局
            contentContainer =
                rootView!!.findViewById<View>(R.id.content_container) as ViewGroup
            contentContainer!!.layoutParams = params
        }
        setKeyBackCancelable(true)
    }

    protected fun init() {
        inAnim = getInAnimation()
        outAnim = getOutAnimation()
    }

    protected fun initEvents() {}
    fun findViewById(id: Int): View {
        return contentContainer!!.findViewById(id)
    }

    /**
     * 添加这个View到Activity的根视图
     */
    fun show() {
        if (isDialog()) {
            showDialog()
        } else {
            if (isShowing()) {
                return
            }
            isShowing = true
            onAttached(rootView!!)
            rootView!!.requestFocus()
        }
    }

    /**
     * 添加这个View到Activity的根视图
     *
     * @param v (是通过哪个View弹出的)
     */
    fun show(v: View?) {
        clickView = v
        show()
    }

    /**
     * 检测该View是不是已经添加到根视图
     *
     * @return 如果视图已经存在该View返回true
     */
    fun isViewShowing(): Boolean {
        return if (isDialog()) {
            false
        } else {
            rootView!!.parent != null || isShowing
        }
    }

    fun dismiss() {
        if (isDialog()) {
            dismissDialog()
        } else {
            if (dismissing) {
                return
            }
            dismissing = true

            //消失动画
            outAnim.setAnimationListener(object :
                Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    decorView!!.post { dismissImmediately() }
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            contentContainer!!.startAnimation(outAnim)
        }
    }

    fun isShowing(): Boolean {
        return if (isDialog()) {
            false
        } else {
            rootView!!.parent != null || isShowing
        }
    }

    open fun isDialog(): Boolean {
        return false
    }

    fun showDialog() {
        if (mDialog != null) {
            mDialog!!.show()
        }
    }

    fun dismissDialog() {
        if (mDialog != null) {
            mDialog!!.dismiss()
        }
    }

    fun dismissImmediately() {
        //从activity根视图移除
        decorView!!.removeView(rootView)
        isShowing = false
        dismissing = false
        onDismissListener?.let { it(this@BasePickerView) }
    }

    /**
     * show的时候调用
     *
     * @param view 这个View
     */
    private fun onAttached(view: View) {
        decorView!!.addView(view)
        contentContainer!!.startAnimation(inAnim)
    }

    private fun getInAnimation(): Animation {
        val res: Int = PickerViewAnimateUtil.getAnimationResource(gravity, true)
        return AnimationUtils.loadAnimation(context, res)
    }

    private fun getOutAnimation(): Animation {
        val res = PickerViewAnimateUtil.getAnimationResource(gravity, false)
        return AnimationUtils.loadAnimation(context, res)
    }

    protected fun setKeyBackCancelable(isCancelable: Boolean): BasePickerView {
        val viewGroup = if (isDialog()) {
            dialogView!!
        } else {
            rootView!!
        }
        viewGroup.isFocusable = isCancelable
        viewGroup.isFocusableInTouchMode = isCancelable
        if (isCancelable) {
            viewGroup.setOnKeyListener(onKeyBackListener)
        } else {
            viewGroup.setOnKeyListener(null)
        }
        return this
    }

    protected fun setOutSideCancelable(isCancelable: Boolean): BasePickerView {
        if (rootView != null) {
            val view =
                rootView!!.findViewById<View>(R.id.outmost_container)
            if (isCancelable) {
                view.setOnTouchListener(onCancelableTouchListener)
            } else {
                view.setOnTouchListener(null)
            }
        }
        return this
    }

    private fun createDialog() {
        if (dialogView != null) {
            mDialog = Dialog(context, R.style.MyCustomDialog)
            mDialog!!.setCancelable(cancelable) //不能点外面取消,也不 能点back取消
            mDialog!!.setContentView(dialogView!!)
            mDialog!!.setCanceledOnTouchOutside(cancelable)
            mDialog!!.window!!.setWindowAnimations(R.style.MyCustomDialog)
            mDialog!!.setOnDismissListener(DialogInterface.OnDismissListener {
                onDismissListener?.let {
                    it(this@BasePickerView)
                }
            })
        }
    }
}
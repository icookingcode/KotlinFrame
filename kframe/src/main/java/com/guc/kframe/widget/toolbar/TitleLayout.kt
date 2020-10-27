package com.guc.kframe.widget.toolbar

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import com.guc.kframe.R
import com.guc.kframe.adapter.CommonAdapter4ListView
import com.guc.kframe.adapter.ViewHolder4ListView
import kotlinx.android.synthetic.main.layout_title.view.*

/**
 * Created by guc on 2020/4/28.
 * 描述：自定义标题栏
 */
class TitleLayout(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs),
    View.OnClickListener {

    companion object {
        const val TYPE_NONE = 0
        const val LEFT_TYPE_FINISH = 1
        const val LEFT_TYPE_CLICKED = 2
        const val GRAVITY_START = 0
        const val GRAVITY_CENTER = 1

        const val SPINNER_TYPE_TEXT = 0
        const val SPINNER_TYPE_IMAGE_TEXT = 1

        // 右侧
        const val TYPE_RIGHT_TEXT = 7
        const val TYPE_RIGHT_IMAGE = 8
        const val TYPE_RIGHT_IMAGE_TEXT = 9
        const val TYPE_RIGHT_TEXT_SPINNER = 10
        const val TYPE_RIGHT_IMAGE_SPINNER = 11
        private val DEFAULT_TEXT_COLOR = Color.parseColor("#000000")

    }


    var onLeftClicked: ((View?) -> Unit)? = null
    var onRightClicked: ((View?) -> Unit)? = null
    var onRightSpinnerClicked: ((Int, ToolbarSpinnerBean) -> Unit)? = null

    var title: CharSequence = ""
        set(value) {
            titleText.text = value
            field = value
        }

    var rightString: CharSequence = ""
        set(value) {
            rightText.text = value
            field = value
        }
    var titleGravity = GRAVITY_START
        set(value) {
            val lp: RelativeLayout.LayoutParams =
                titleText.layoutParams as RelativeLayout.LayoutParams
            if (value == GRAVITY_CENTER) {
                lp.addRule(RelativeLayout.CENTER_IN_PARENT)
            } else {
                lp.removeRule(RelativeLayout.CENTER_IN_PARENT)
                lp.addRule(RelativeLayout.CENTER_VERTICAL)
                lp.addRule(RelativeLayout.RIGHT_OF, R.id.leftImgV)
            }
            titleText.layoutParams = lp
            field = value
        }
    var leftType: Int = TYPE_NONE
        set(value) {
            field = value
            initView()
        }
    var rightType: Int = TYPE_NONE
        set(value) {
            field = value
            initView()
        }

    var rightImageDrawable: Drawable? = getDrawable(R.drawable.more)
        set(value) {
            rightImgV.setImageDrawable(value)
            field = value
        }
    var leftImageDrawable: Drawable? = getDrawable(R.drawable.back_arrow)
        set(value) {
            leftImgV.setImageDrawable(value)
            field = value
        }

    private var imageSize = 0
        set(value) {
            field = if (value > dp2px(40))
                dp2px(40)
            else
                value
        }
    var titleTextColor = DEFAULT_TEXT_COLOR
        set(value) {
            titleText.setTextColor(value)
            field = value
        }

    var titleTextSize = sp2px(20)
        set(value) {
            titleText.textSize = px2sp(value).toFloat()
            field = value
        }
    var rightTextColor = DEFAULT_TEXT_COLOR
        set(value) {
            rightText.setTextColor(value)
            field = value
        }

    //右侧下拉选择菜单
    private lateinit var rightSpinnerDataList: MutableList<ToolbarSpinnerBean>
    private var rightAdapter: CommonAdapter4ListView<ToolbarSpinnerBean>? = null
    private var popSpinnerRight: PopupWindow? = null
    var rightIndex = -1
    var rightSaveState = false // 是否保留选中状态
    private var rightSpinnerType = SPINNER_TYPE_IMAGE_TEXT

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_title, this)
        val array = context.obtainStyledAttributes(attrs, R.styleable.TitleLayout)
        leftType = array.getInt(
            R.styleable.TitleLayout_leftType,
            TYPE_NONE
        )

        title = array.getString(R.styleable.TitleLayout_title) ?: "标题"
        rightString = array.getString(R.styleable.TitleLayout_rightText) ?: "新增"

        titleGravity = array.getInt(R.styleable.TitleLayout_titleGravity, GRAVITY_CENTER)

        leftImageDrawable = array.getDrawable(R.styleable.TitleLayout_leftImage)
            ?: getDrawable(R.drawable.back_arrow)
        rightImageDrawable = array.getDrawable(R.styleable.TitleLayout_rightImage)
            ?: getDrawable(R.drawable.more)

        titleTextColor = array.getColor(R.styleable.TitleLayout_titleTextColor, DEFAULT_TEXT_COLOR)
        imageSize =
            array.getDimensionPixelSize(R.styleable.TitleLayout_imageSize, dp2px(30))
        titleTextSize =
            array.getDimensionPixelSize(R.styleable.TitleLayout_titleTextSize, sp2px(20))
        rightTextColor = array.getColor(R.styleable.TitleLayout_rightTextColor, DEFAULT_TEXT_COLOR)
        rightType = array.getInt(
            R.styleable.TitleLayout_rightType,
            TYPE_NONE
        )
        rightSpinnerType = array.getInt(R.styleable.TitleLayout_rightSpinnerType, SPINNER_TYPE_TEXT)
        array.recycle()
        initView()
    }

    private fun initView() {
        leftImgV.layoutParams.apply {
            width = imageSize
            height = imageSize
        }
        rightImgV.layoutParams.apply {
            width = imageSize
            height = imageSize
        }
        rightSpinnerDataList = ArrayList()
        rightLayout.setOnClickListener(this)
        when (leftType) {
            TYPE_NONE -> leftImgV.visibility = View.GONE
            LEFT_TYPE_FINISH, LEFT_TYPE_CLICKED -> {
                leftImgV.visibility = View.VISIBLE
                leftImgV.setOnClickListener(this)
                leftLayout.setOnClickListener(this)
            }

        }
        when (rightType) {
            TYPE_NONE -> rightLayout.visibility = View.GONE
            TYPE_RIGHT_IMAGE, TYPE_RIGHT_IMAGE_SPINNER -> {
                rightLayout.visibility = View.VISIBLE
                rightImgV.visibility = View.VISIBLE
                rightText.visibility = View.GONE
            }
            TYPE_RIGHT_TEXT, TYPE_RIGHT_TEXT_SPINNER -> {
                rightLayout.visibility = View.VISIBLE
                rightImgV.visibility = View.GONE
                rightText.visibility = View.VISIBLE
            }
            TYPE_RIGHT_IMAGE_TEXT -> {
                rightLayout.visibility = View.VISIBLE
                rightImgV.visibility = View.VISIBLE
                rightText.visibility = View.VISIBLE
            }
            else -> {
                rightLayout.visibility = View.GONE
                rightLayout.setOnClickListener(null)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.leftImgV, R.id.leftLayout -> {
                when (leftType) {
                    LEFT_TYPE_FINISH -> (context as Activity).finish()
                    LEFT_TYPE_CLICKED -> onLeftClicked?.let { it(v) }
                }
            }
            R.id.rightLayout -> {
                when (rightType) {
                    TYPE_RIGHT_TEXT, TYPE_RIGHT_IMAGE, TYPE_RIGHT_IMAGE_TEXT -> onRightClicked?.let {
                        it(
                            v
                        )
                    }
                    TYPE_RIGHT_TEXT_SPINNER, TYPE_RIGHT_IMAGE_SPINNER -> showRightWindow()
                }
            }
        }
    }

    /**
     * 设置右侧下拉菜单数据
     */
    fun setRightSpinnerData(
        dataList: List<ToolbarSpinnerBean>,
        selectedIndex: Int = 0
    ) {
        rightIndex = selectedIndex
        rightSpinnerDataList.clear()
        rightSpinnerDataList.addAll(dataList)
        // 刷新Adaper
        if (rightAdapter != null) {
            rightAdapter?.notifyDataSetChanged()
        }
    }

    private fun showRightWindow() {
        if (popSpinnerRight == null) {
            val view =
                View.inflate(context, R.layout.view_common_toolbar_right_spinner, null)
            val lsvSpinner = view.findViewById<ListView>(R.id.lsv_spinner)
            rightAdapter = object : CommonAdapter4ListView<ToolbarSpinnerBean>(
                context as Activity,
                R.layout.layout_adapter_spinner_list,
                rightSpinnerDataList
            ) {
                override fun bindData(
                    viewHolder: ViewHolder4ListView,
                    position: Int,
                    data: ToolbarSpinnerBean?
                ) {
                    if (rightSpinnerType == SPINNER_TYPE_IMAGE_TEXT) {
                        viewHolder.apply {
                            getView<TextView>(R.id.txtv_spinner_list).gravity =
                                Gravity.START or Gravity.CENTER_VERTICAL
                            setVisible(R.id.imgv_spinner_list, true)
                            setImageDrawable(R.id.imgv_spinner_list, data?.drawable)
                            setText(R.id.txtv_spinner_list, data?.text)
                        }
                    } else {
                        viewHolder.apply {
                            getView<TextView>(R.id.txtv_spinner_list).gravity = Gravity.CENTER
                            setVisible(R.id.imgv_spinner_list, false)
                            setText(R.id.txtv_spinner_list, data?.text)
                        }
                    }
                    if (rightSaveState) {
                        viewHolder.setTextColor(
                            R.id.txtv_spinner_list,
                            if (rightIndex == position) R.color.colorPrimary else R.color.colorBlack
                        )
                    }

                }

            }
            lsvSpinner.setOnItemClickListener { _, _, position, _ ->
                popSpinnerRight!!.dismiss()
                if (rightIndex == position && rightSaveState) {
                    return@setOnItemClickListener
                }
                rightIndex = position
                val item = rightSpinnerDataList[position]
                if (rightSaveState) {
                    rightAdapter?.notifyDataSetChanged()
                }
                onRightSpinnerClicked?.apply {
                    this(position, item)
                }
            }
            lsvSpinner.adapter = rightAdapter
            popSpinnerRight = PopupWindow(
                view,
                LayoutParams.WRAP_CONTENT,
                getContentHeight(),
                true
            ).apply {
                isOutsideTouchable = true
                setBackgroundDrawable(
                    ColorDrawable(
                        ResourcesCompat.getColor(
                            resources,
                            android.R.color.transparent,
                            null
                        )
                    )
                )
            }
            view.findViewById<View>(R.id.llay_common_toolbar_right)
                .setOnClickListener { popSpinnerRight!!.dismiss() }

        }
        popSpinnerRight?.showAsDropDown(rightLayout)

    }

    private fun getContentHeight(): Int {
        return dp2px(200)
    }

    private fun getDrawable(resId: Int) = ResourcesCompat.getDrawable(resources, resId, null)

    /**
     * sp -> px
     */
    fun sp2px(sp: Int): Int =
        (context.resources.displayMetrics.scaledDensity * sp + 0.5).toInt()

    fun dp2px(dp: Int): Int =
        (context.resources.displayMetrics.density * dp + 0.5).toInt()

    /**
     * px -> sp
     */
    fun px2sp(px: Int): Int =
        (px / context.resources.displayMetrics.scaledDensity + 0.5f).toInt()

}
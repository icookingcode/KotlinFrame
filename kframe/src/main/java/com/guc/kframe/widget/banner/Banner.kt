package com.guc.kframe.widget.banner

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.guc.kframe.R
import com.guc.kframe.utils.LogG
import com.guc.kframe.widget.CornerImageView
import kotlinx.android.synthetic.main.layout_banner.view.*

/**
 * Created by guc on 2020/6/11.
 * Description：Banner
 */
class Banner(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FrameLayout(context, attrs, defStyleAttr), ViewPager.OnPageChangeListener {

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    companion object {
        const val TAG = "Banner"
    }

    private val handler = WeakHandler()
    private var mIndicatorMargin = 5
    private var indicatorSize = 0
    private var count = 0
    private val scrollTime = 800
    private val delayTime = 3000
    private var currentItem = 0
    private var currentIndex = 0 //当前广告的下下标
    private var lastPosition = 0
    private var isAutoPlay = true
    private val isScroll = true
    private var mScroller: BannerScroller? = null
    private var imageUrls: MutableList<String>? = null
    var imageViews: MutableList<View>? = null
    private var indicatorImages: MutableList<ImageView>? = null
    private var adapter: BannerPagerAdapter? = null

    var onPageClicked: ((Int) -> Unit)? = null

    init {
        imageUrls = ArrayList()
        imageViews = ArrayList()
        indicatorImages = ArrayList()
        indicatorSize = context.resources.displayMetrics.widthPixels / 80
        initView()
    }

    /**
     * set banner images
     */
    fun setImages(imageUrls: List<String>): Banner {
        this.imageUrls?.clear()
        this.imageUrls?.addAll(imageUrls)
        this.count = imageUrls.size
        return this
    }

    /**
     * set auto play
     */
    fun setAutoPlay(isAutoPlay: Boolean): Banner {
        this.isAutoPlay = isAutoPlay
        return this
    }

    /**
     * start banner
     */
    fun start() {
        setImageList(imageUrls)
        setData()
    }


    private fun setImageList(imageUrls: MutableList<String>?) {
        if (imageUrls == null || imageUrls.size <= 0) {
            LogG.loge(TAG, "The image data set is empty.")
            return
        }
        imageViews?.clear()
        createIndicator()
        for (i in 0..count + 1) {
            val imageView = CornerImageView(context)
            imageView.setCornerRadius(8)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            var url = when (i) {
                0 -> {
                    imageUrls[count - 1]
                }
                count + 1 -> {
                    imageUrls[0]
                }
                else -> {
                    imageUrls[i - 1]
                }
            }
            imageViews?.add(imageView)
            Glide.with(context).load(url).into(imageView)
        }
    }

    private fun setData() {
        currentItem = 1
        if (adapter == null) {
            adapter = BannerPagerAdapter()
            viewPager.addOnPageChangeListener(this)
        }
        viewPager.adapter = adapter
//        viewPager.focusable = View.NOT_FOCUSABLE
        viewPager.setCurrentItem(currentItem, false)
        viewPager.scrollable = isScroll && count > 1
        if (isAutoPlay)
            startAutoPlay()
    }

    private val task: Runnable = object : Runnable {
        override fun run() {
            if (count > 1 && isAutoPlay) {
                currentItem = currentItem % (count + 1) + 1
                if (currentItem == 1) {
                    viewPager.setCurrentItem(currentItem, false)
                    handler.post(this)
                } else {
                    viewPager.currentItem = currentItem
                    handler.postDelayed(this, delayTime.toLong())
                }
            }
        }
    }

    /**
     * auto play
     */
    private fun startAutoPlay() {
        handler.removeCallbacks(task)
        handler.postDelayed(task, delayTime.toLong())
    }

    /**
     * stop auto play
     */
    fun stopAutoPlay() {
        handler.removeCallbacks(task)
    }

    /**
     * create indicator
     */
    private fun createIndicator() {
        indicatorImages?.clear()
        indicatorLayout.removeAllViews()
        for (i in 0 until count) {
            val imageView = ImageView(context)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            val params = LinearLayout.LayoutParams(indicatorSize, indicatorSize)
            params.leftMargin = mIndicatorMargin
            params.rightMargin = mIndicatorMargin
            imageView.layoutParams = params
            if (i == 0) {
                imageView.setImageResource(R.drawable.dra_indicator_selected)
            } else {
                imageView.setImageResource(R.drawable.dra_indicator_unselected)
            }
            indicatorImages!!.add(imageView)
            indicatorLayout.addView(imageView)
        }
    }

    /**
     * init view
     */
    private fun initView() {
        View.inflate(context, R.layout.layout_banner, this)
        initViewPagerScroll()
    }

    /**
     * init viewpager scroll
     */
    private fun initViewPagerScroll() {
        try {
            val mField =
                ViewPager::class.java.getDeclaredField("mScroller")
            mField.isAccessible = true
            mScroller = BannerScroller(context)
            mScroller?.mDuration = scrollTime
            mField[viewPager] = mScroller
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        when (state) {
            0 -> if (currentItem == 0) {
                viewPager.setCurrentItem(count, false)
            } else if (currentItem == count + 1) {
                viewPager.setCurrentItem(1, false)
            }
            1 -> if (currentItem == 0) {
                viewPager.setCurrentItem(count, false)
            } else if (currentItem == count + 1) {
                viewPager.setCurrentItem(1, false)
            }
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(i: Int) {
        currentItem = i
        indicatorImages!![(lastPosition - 1 + count) % count]
            .setImageResource(R.drawable.dra_indicator_unselected)
        indicatorImages!![(i - 1 + count) % count]
            .setImageResource(R.drawable.dra_indicator_selected)
        lastPosition = i
        var iFix = i
        if (i == 0) iFix = count
        if (i > count) iFix = 1
        currentIndex = iFix - 1
    }

    inner class BannerPagerAdapter : PagerAdapter() {
        override fun getCount(): Int = this@Banner.imageViews?.size ?: 0
        override fun isViewFromObject(view: View, obj: Any): Boolean = view == obj
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = imageViews!![position]
            container.addView(view)
            view.setOnClickListener {
                this@Banner.onPageClicked?.let {
                    it(this@Banner.currentIndex)
                }
            }
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            container.removeView(obj as View?)
        }
    }
}
package com.guc.kframe.widget.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.guc.kframe.R;
import com.guc.kframe.widget.CornerImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guc on 2019/12/6.
 * 描述：广告栏
 */
public class Banner extends FrameLayout implements ViewPager.OnPageChangeListener {
    private static final String TAG = "Banner";
    private int mIndicatorMargin = 5;
    private Context context;
    private int count;
    private List imageUrls;
    private List<View> imageViews;
    private List<ImageView> indicatorImages;
    private WeakHandler handler = new WeakHandler();
    private int indicatorSize;
    private int scrollTime = 800;
    private int delayTime = 3000;
    private int currentItem = 0;
    private int currentIndex = 0; //当前广告的下下标
    private int lastPosition = 0;
    private BannerScroller mScroller;
    private boolean isAutoPlay = true;
    private boolean isScroll = true;

    private BannerViewPager mViewPager;
    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (count > 1 && isAutoPlay) {
                currentItem = currentItem % (count + 1) + 1;
//                Log.i(tag, "curr:" + currentItem + " count:" + count);
                if (currentItem == 1) {
                    mViewPager.setCurrentItem(currentItem, false);
                    handler.post(task);
                } else {
                    mViewPager.setCurrentItem(currentItem);
                    handler.postDelayed(task, delayTime);
                }
            }
        }
    };
    private BannerPagerAdapter adapter;
    private LinearLayout mIndicatorLayout;
    private OnPageClickListener onPageClickListener;

    public Banner(@NonNull Context context) {
        this(context, null);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        imageUrls = new ArrayList();
        imageViews = new ArrayList<>();
        indicatorImages = new ArrayList<>();
        indicatorSize = context.getResources().getDisplayMetrics().widthPixels / 80;
        initView(context, attrs);
    }

    public Banner setImages(List<?> imageUrls) {
        this.imageUrls = imageUrls;
        this.count = imageUrls.size();
        return this;
    }

    public Banner setAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
        return this;
    }

    public void start() {
        setImageList(imageUrls);
        setData();
    }

    public void setOnPageClickListener(OnPageClickListener onPageClickListener) {
        this.onPageClickListener = onPageClickListener;
    }

    private void setData() {
        currentItem = 1;
        if (adapter == null) {
            adapter = new BannerPagerAdapter();
            mViewPager.addOnPageChangeListener(this);
        }
        mViewPager.setAdapter(adapter);
        mViewPager.setFocusable(true);
        mViewPager.setCurrentItem(currentItem, false);
        if (isScroll && count > 1) {
            mViewPager.setScrollable(true);
        } else {
            mViewPager.setScrollable(false);
        }
        if (isAutoPlay)
            startAutoPlay();
    }

    /**
     * 自动播放
     */
    public void startAutoPlay() {
        handler.removeCallbacks(task);
        handler.postDelayed(task, delayTime);
    }

    public void stopAutoPlay() {
        handler.removeCallbacks(task);
    }

    /**
     * 设置图片资源
     *
     * @param imageUrls 图片url
     */
    private void setImageList(List<?> imageUrls) {
        if (imageUrls == null || imageUrls.size() <= 0) {
            Log.e(TAG, "The image data set is empty.");
            return;
        }
        imageViews.clear();
        createIndicator();
        for (int i = 0; i <= count + 1; i++) {
            ImageView imageView = new CornerImageView(context);
            ((CornerImageView) imageView).setCornerRadius(8);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Object url;
            if (i == 0) {
                url = imageUrls.get(count - 1);
            } else if (i == count + 1) {
                url = imageUrls.get(0);
            } else {
                url = imageUrls.get(i - 1);
            }
            imageViews.add(imageView);
            Glide.with(context).load(url).into(imageView);
        }
    }


    /**
     * 创建指示器
     */
    private void createIndicator() {
        indicatorImages.clear();
        mIndicatorLayout.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(indicatorSize, indicatorSize);
            params.leftMargin = mIndicatorMargin;
            params.rightMargin = mIndicatorMargin;
            imageView.setLayoutParams(params);
            if (i == 0) {
                imageView.setImageResource(R.drawable.dra_indicator_selected);
            } else {
                imageView.setImageResource(R.drawable.dra_indicator_unselected);
            }
            indicatorImages.add(imageView);
            mIndicatorLayout.addView(imageView);

        }
    }

    /**
     * 初始化控件
     *
     * @param context context
     * @param attrs   attrs
     */
    private void initView(Context context, AttributeSet attrs) {
        View root = LayoutInflater.from(context).inflate(R.layout.layout_banner, this, true);
        mIndicatorLayout = root.findViewById(R.id.ll_indicator);
        mViewPager = root.findViewById(R.id.vp_images);
        initViewPagerScroll();
    }

    /**
     * 初始化ViewPager Scroller
     */
    private void initViewPagerScroll() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new BannerScroller(context);
            mScroller.setDuration(scrollTime);
            mField.set(mViewPager, mScroller);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        currentItem = i;
        indicatorImages.get((lastPosition - 1 + count) % count).setImageResource(R.drawable.dra_indicator_unselected);
        indicatorImages.get((i - 1 + count) % count).setImageResource(R.drawable.dra_indicator_selected);
        lastPosition = i;
        if (i == 0) i = count;
        if (i > count) i = 1;
        currentIndex = i - 1;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case 0://No operation
                if (currentItem == 0) {
                    mViewPager.setCurrentItem(count, false);
                } else if (currentItem == count + 1) {
                    mViewPager.setCurrentItem(1, false);
                }
                break;
            case 1://start Sliding
                if (currentItem == count + 1) {
                    mViewPager.setCurrentItem(1, false);
                } else if (currentItem == 0) {
                    mViewPager.setCurrentItem(count, false);
                }
                break;
            case 2://end Sliding
                break;
        }
    }

    public interface OnPageClickListener {
        void onClicked(int index);
    }

    class BannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(imageViews.get(position));
            View view = imageViews.get(position);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPageClickListener != null)
                        onPageClickListener.onClicked(currentIndex);
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}

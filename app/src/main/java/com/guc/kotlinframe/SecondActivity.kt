package com.guc.kotlinframe

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.guc.kframe.adapter.CommonPagerAdapter
import com.guc.kframe.adapter.ViewPager2FragmentStateAdapter
import com.guc.kframe.adapter.ViewPagerAdapter
import com.guc.kframe.base.BaseActivity
import com.guc.kframe.utils.ToastUtil
import com.guc.kframe.utils.quickStartActivity
import com.guc.kframe.widget.toolbar.ToolbarSpinnerBean
import com.guc.kotlinframe.ui.fragment.FragmentContent
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : BaseActivity() {
    lateinit var adapter2: ViewPager2FragmentStateAdapter
    lateinit var adapter: ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        titleLayout.onRightClicked = { ToastUtil.toast("点击了更多") }
        titleLayout.onRightSpinnerClicked = { i, _ -> ToastUtil.toast("点击了第${i + 1}选项") }
        titleLayout.setRightSpinnerData(
            listOf(
                ToolbarSpinnerBean(
                    "1",
                    getDrawable(R.drawable.back_arrow),
                    "新增"
                ),
                ToolbarSpinnerBean(
                    "2",
                    getDrawable(R.drawable.back_arrow),
                    "编辑"
                ),
                ToolbarSpinnerBean(
                    "3",
                    getDrawable(R.drawable.back_arrow),
                    "备份"
                )
            )
        )
        adapter2 = ViewPager2FragmentStateAdapter(
            this,
            listOf(
                FragmentContent.newInstance("页码：", "第一页"),
                FragmentContent.newInstance("页码：", "第二页"),
                FragmentContent.newInstance("页码：", "第三页")
            )
        )
        viewPager2.adapter = adapter2

        tabLayout.setupWithViewPager(viewPager)
        adapter = ViewPagerAdapter(
            supportFragmentManager, listOf(
                FragmentContent.newInstance("页码：", "第五页"),
                FragmentContent.newInstance("页码：", "第六页"),
                FragmentContent.newInstance("页码：", "第七页")
            ),
            listOf("第五页", "第六页", "第七页")
        )
        viewPager.adapter = adapter

        tvNextPage.setOnClickListener { quickStartActivity<ThirdActivity>(this) {} }

        val adapter3 = object : CommonPagerAdapter<String>(
            this,
            listOf("第十三页", "第八页", "第九页", "第十页", "第十一页", "第十二页", "第十三页", "第八页"),
            R.layout.item_select
        ) {
            override fun bindData(parent: View, position: Int, data: String) {
                parent.findViewById<TextView>(R.id.tvName).text = data
            }
        }
        viewPager3.adapter = adapter3
        bannerIndicator.setUpWidthViewPager(viewPager3)
    }

}
package com.guc.kotlinframe

import android.os.Bundle
import com.guc.kframe.adapter.ViewPager2FragmentStateAdapter
import com.guc.kframe.adapter.ViewPagerAdapter
import com.guc.kframe.base.BaseActivity
import com.guc.kframe.utils.quickStartActivity
import com.guc.kotlinframe.ui.fragment.FragmentContent
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : BaseActivity() {
    lateinit var adapter2: ViewPager2FragmentStateAdapter
    lateinit var adapter: ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

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
    }

}
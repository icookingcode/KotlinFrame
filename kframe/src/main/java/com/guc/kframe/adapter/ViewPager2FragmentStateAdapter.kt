package com.guc.kframe.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by guc on 2020/6/12.
 * Description：Adapter for ViewPager2 + Fragment
 */
class ViewPager2FragmentStateAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private var fragments: List<Fragment>? = null
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    constructor(fragmentActivity: FragmentActivity, fragments: List<Fragment>) : this(
        fragmentActivity.supportFragmentManager,
        fragmentActivity.lifecycle,
        fragments
    )

    constructor(fragment: Fragment, fragments: List<Fragment>) : this(
        fragment.childFragmentManager,
        fragment.lifecycle,
        fragments
    )

    override fun getItemCount(): Int = fragments?.size ?: 0

    override fun createFragment(position: Int): Fragment = fragments?.get(position) ?: Fragment()

}
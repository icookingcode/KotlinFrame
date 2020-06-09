package com.guc.kframe.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Created by guc on 2020/6/9.
 * 描述：ViewPagerAdapter
 */
class ViewPagerAdapter(
    val fm: FragmentManager,
    val fragments: List<Fragment>,
    var titles: List<String>?
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    constructor(fm: FragmentManager, fragments: List<Fragment>) : this(fm, fragments, null)

    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getPageTitle(position: Int): CharSequence? = titles?.let { it[position % it.size] }

}
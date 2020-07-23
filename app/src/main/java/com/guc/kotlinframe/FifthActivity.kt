package com.guc.kotlinframe

import android.os.Bundle
import com.guc.kframe.adapter.GroupAdapter
import com.guc.kframe.adapter.ViewHolder4ListView
import com.guc.kframe.base.BaseActivity
import kotlinx.android.synthetic.main.activity_fifth.*

/**
 * Created by Guc on 2020/7/23.
 * Description：
 */
class FifthActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fifth)

        loadData()
    }

    private fun loadData() {
        val parents = listOf("水果", "蔬菜", "鲜花")
        val children = listOf(
            listOf("苹果", "香蕉", "梨"),
            listOf("菠菜", "南瓜", "茄子"),
            listOf("百合花", "玫瑰花", "菊花")
        )

        val adapter = object : GroupAdapter<String, String>(
            this,
            parents,
            children,
            R.layout.item_parent,
            R.layout.item_child
        ) {
            override fun bindParentData(
                holder: ViewHolder4ListView,
                parent: String,
                groupPosition: Int,
                isExpanded: Boolean
            ) {
                holder.setText(R.id.tvParent, parent)
            }

            override fun bindChildData(
                holder: ViewHolder4ListView,
                child: String,
                groupPosition: Int,
                childPosition: Int,
                isLastChild: Boolean
            ) {
                holder.setText(R.id.tvChild, child)
            }
        }
        expandableListView.setAdapter(adapter)
        for (i in 0 until expandableListView.count) {
            expandableListView.expandGroup(i)
        }
    }
}
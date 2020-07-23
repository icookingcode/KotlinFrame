package com.guc.kframe.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter

/**
 * Created by guc on 2020/7/23.
 * Description：分组Adapter for ExpandableListView
 */
abstract class GroupAdapter<Parent, Child>(
    val context: Context,
    val groups: List<Parent>,
    val children: List<List<Child>>,
    val parentLayoutId: Int,
    val childLayoutId: Int
) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return groups.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return children[groupPosition].size
    }

    override fun getGroup(groupPosition: Int): Parent {
        return groups[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Child {
        return children[groupPosition][childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean,
        convertView: View?, parent: ViewGroup?
    ): View {
        val holder: ViewHolder4ListView =
            ViewHolder4ListView.get(context, convertView, null, parentLayoutId)
        bindParentData(holder, getGroup(groupPosition), groupPosition, isExpanded)
        return holder.parent
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int,
        isLastChild: Boolean, convertView: View?, parent: ViewGroup?
    ): View? {
        val holder: ViewHolder4ListView =
            ViewHolder4ListView.get(context, convertView, null, childLayoutId)
        bindChildData(
            holder,
            getChild(groupPosition, childPosition),
            groupPosition,
            childPosition,
            isLastChild
        )
        return holder.parent
    }

    // 子选项是否可以选择
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    fun hasChildren(groupPosition: Int) = children[groupPosition].isNotEmpty()

    abstract fun bindParentData(
        holder: ViewHolder4ListView,
        parent: Parent,
        groupPosition: Int,
        isExpanded: Boolean
    )

    abstract fun bindChildData(
        holder: ViewHolder4ListView, child: Child, groupPosition: Int, childPosition: Int,
        isLastChild: Boolean
    )
}
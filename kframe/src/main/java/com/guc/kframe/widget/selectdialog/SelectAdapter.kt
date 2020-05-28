package com.guc.kframe.widget.selectdialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.guc.kframe.R
import com.guc.kframe.adapter.CommonAdapter4Rcv
import com.guc.kframe.adapter.ViewHolder4RecyclerView

/**
 * Created by guc on 2020/5/27.
 * 描述：选择适配器
 */
class SelectAdapter<T>(datas: List<T>, var isSingleSel: Boolean = true) :
    CommonAdapter4Rcv<T>(datas) {

    private var selIndicators: BooleanArray = BooleanArray(datas.size)
    private var lastSelectIndex = -1

    override fun getRootView(parent: ViewGroup, viewType: Int): View =
        LayoutInflater.from(parent.context).inflate(R.layout.item_select, parent, false)

    override fun bindData(
        viewHolder: ViewHolder4RecyclerView,
        position: Int,
        data: T,
        itemType: Int
    ) {
        viewHolder.setText(R.id.tvName, data.toString())
        val ivSel = viewHolder.getView<ImageView>(R.id.ivSelect)
        ivSel.isSelected = selIndicators[position]
        ivSel.setOnClickListener {
            val sel: Boolean = !ivSel.isSelected
            ivSel.isSelected = sel
            selIndicators[position] = sel
            if (isSingleSel && lastSelectIndex != -1) {
                selIndicators[lastSelectIndex] = false
                notifyDataSetChanged()
            }
            if (sel) lastSelectIndex = position else {
                if (isSingleSel) lastSelectIndex = -1
            }
        }
    }


    //获取选中
    fun getSelected(): List<T> {
        val list = ArrayList<T>();
        for ((index, b) in selIndicators.withIndex()) {
            if (b) list.add(datas[index])
        }
        return list
    }
}
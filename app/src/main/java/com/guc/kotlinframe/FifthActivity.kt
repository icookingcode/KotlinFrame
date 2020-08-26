package com.guc.kotlinframe

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import com.guc.kframe.adapter.GroupAdapter
import com.guc.kframe.adapter.ViewHolder4ListView
import com.guc.kframe.base.BaseActivity
import com.guc.kframe.system.SystemPermission
import com.guc.kframe.utils.AssetsUtils
import com.guc.kframe.utils.LocationUtils
import com.guc.kframe.utils.LogG
import com.guc.kframe.utils.ToastUtil
import com.guc.kotlinframe.logic.model.KeyValue
import kotlinx.android.synthetic.main.activity_fifth.*


/**
 * Created by Guc on 2020/7/23.
 * Description：
 */
class FifthActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fifth)

        findLocation()
        loadData()
        getAssets2String()
    }

    override fun onDestroy() {
        LocationUtils.unregister()
        super.onDestroy()
    }

    @SuppressLint("MissingPermission")
    private fun findLocation() {
        btnFindLocation.setOnClickListener {
            getSystem<SystemPermission>()?.request(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) { granted, _ ->
                if (granted) {
                    LocationUtils.register(
                        this,
                        5000,
                        5f,
                        object : LocationUtils.OnLocationChangeListener {
                            override fun getLastKnownLocation(location: Location) {
                                LogG.loge("findLocation", location.toString())
                            }

                            override fun onLocationChanged(location: Location) {
                                LogG.loge("findLocation", location.toString())
                                LogG.loge(
                                    "findLocation",
                                    LocationUtils.getCountryName(
                                        this@FifthActivity,
                                        location.latitude,
                                        location.longitude
                                    )
                                )
                                LogG.loge(
                                    "findLocation",
                                    LocationUtils.getLocality(
                                        this@FifthActivity,
                                        location.latitude,
                                        location.longitude
                                    )
                                )
                            }

                            override fun onStatusChanged(provider: String?, enable: Boolean) {
                                LogG.loge("findLocation", provider + enable)
                            }

                        })
                } else {
                    ToastUtil.toast("您没有权限")
                }
            }
        }
    }

    private fun getAssets2String() {
        btnGetAssets.setOnClickListener {
            tvShow.text = AssetsUtils.getAssets2String(this, "control_degree.json")
        }
        btnGetAssetsObject.setOnClickListener {
            val list = AssetsUtils.getAssets2Object<List<KeyValue>>(this, "control_degree.json")
            LogG.loge("Assets", list.toString())
        }
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
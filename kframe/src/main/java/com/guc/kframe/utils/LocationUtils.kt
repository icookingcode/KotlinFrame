package com.guc.kframe.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.*
import android.os.Bundle
import android.provider.Settings
import java.io.IOException
import java.util.*


/**
 * Created by guc on 2020/8/26.
 * Description：定位工具
 */
object LocationUtils {
    private lateinit var mListener: OnLocationChangeListener
    private var myLocationListener: MyLocationListener? = null
    private var mLocationManager: LocationManager? = null

    /**
     * 判断GPS是否可用
     */
    fun isGpsEnabled(context: Context): Boolean {
        val lm =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * 判断定位是否可用
     */
    fun isLocationEnable(context: Context): Boolean {
        val lm =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    /**
     * 打开Gps设置界面
     */
    fun openGpsSetting(context: Context) {
        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    /**
     * 注册
     * <p>使用完记得调用{@link #unregister()}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>}</p>
     * <p>如果{@code minDistance}为0，则通过{@code minTime}来定时更新；</p>
     * <p>{@code minDistance}不为0，则以{@code minDistance}为准；</p>
     * <p>两者都为0，则随时刷新。</p>
     *
     * @param minTime     位置信息更新周期（单位：毫秒）
     * @param minDistance 位置变化最小距离：当位置距离变化超过此值时，将更新位置信息（单位：米）
     * @param listener    位置刷新的回调接口
     * @return {@code true}: 初始化成功<br>{@code false}: 初始化失败
     */
    @SuppressLint("MissingPermission")
    fun register(
        context: Context,
        minTime: Long,
        minDistance: Float,
        listener: OnLocationChangeListener
    ): Boolean {
        mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mListener = listener
        if (!isLocationEnable(context)) {
            ToastUtil.toast("无法定位，请打开定位服务")
            return false
        }
        val provider = mLocationManager?.getBestProvider(Criteria().apply {
            accuracy =
                Criteria.ACCURACY_COARSE ////设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
            isSpeedRequired = false //不需要速度
            isAltitudeRequired = false //不要求海拔
            isBearingRequired = false //不要求方位
            isCostAllowed = false //设置是否允许运营商收费
            powerRequirement = Criteria.POWER_LOW //低功耗
        }, true)
        val location = mLocationManager?.getLastKnownLocation(provider!!)
        if (location != null) {
            listener.getLastKnownLocation(location)
        }
        if (myLocationListener == null) {
            myLocationListener = MyLocationListener()
        }
        mLocationManager?.requestLocationUpdates(
            provider!!,
            minTime,
            minDistance,
            myLocationListener!!
        )
        return true
    }

    fun unregister() {
        if (mLocationManager != null) {
            if (myLocationListener != null) {
                mLocationManager!!.removeUpdates(myLocationListener!!)
                myLocationListener = null
            }
            mLocationManager = null
        }
    }

    /**
     * 根据经纬度获取地理位置
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude 经度
     * @return [Address]
     */
    fun getAddress(
        context: Context?,
        latitude: Double,
        longitude: Double
    ): Address? {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses: List<Address> =
                geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses.isNotEmpty()) return addresses[0]
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 根据经纬度获取所在国家
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在国家
     */
    fun getCountryName(
        context: Context?,
        latitude: Double,
        longitude: Double
    ): String {
        val address = getAddress(context, latitude, longitude)
        return if (address == null) "unknown" else address.countryName ?: address.countryCode
        ?: "unknown"
    }

    /**
     * 根据经纬度获取所在地
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在地
     */
    fun getLocality(
        context: Context?,
        latitude: Double,
        longitude: Double
    ): String {
        val address = getAddress(context, latitude, longitude)
        return if (address == null) "unknown" else address.locality
    }

    /**
     * 根据经纬度获取所在街道
     *
     * @param context   上下文
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在街道
     */
    fun getStreet(
        context: Context?,
        latitude: Double,
        longitude: Double
    ): String {
        val address = getAddress(context, latitude, longitude)
        return if (address == null) "unknown" else address.getAddressLine(0)
    }

    class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location?) {
            location?.let {
                mListener.onLocationChanged(it)
            }
        }

        /**
         * This callback will never be invoked and providers can be considers as always in the
         * {@link LocationProvider#AVAILABLE} state.
         *
         * @deprecated This callback will never be invoked.
         */
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

        }

        override fun onProviderEnabled(provider: String?) {
            mListener.onStatusChanged(provider, true)
        }

        override fun onProviderDisabled(provider: String?) {
            mListener.onStatusChanged(provider, false)
        }

    }

    interface OnLocationChangeListener {
        /**
         * 获取最后一次保留的坐标
         */
        fun getLastKnownLocation(location: Location)


        /**
         * 当坐标发生改变时触发
         */
        fun onLocationChanged(location: Location)

        /**
         * provider在可用、不可用和状态直接切换时触发此函数
         */
        fun onStatusChanged(provider: String?, enable: Boolean)
    }
}
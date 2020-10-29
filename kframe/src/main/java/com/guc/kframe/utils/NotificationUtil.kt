package com.guc.kframe.utils

import android.app.*
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.guc.kframe.Engine
import com.guc.kframe.R

/**
 * Created by guc on 2020/5/9.
 * 描述：通知工具
 */
object NotificationUtil {
    const val channelId_NORMAL = "1"
    private const val channelName_NORMAL = "基本通知"
    const val channelId_IMPORTANT = "2"
    private const val channelName_IMPORTANT = "重要通知"

    fun createChannel(context: Context = Engine.context): NotificationManager {
        //获取NotificationManager
        val manager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        //创建通知渠道 大于等于Android8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 =
                NotificationChannel(
                    channelId_NORMAL,
                    channelName_NORMAL,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            val channel2 =
                NotificationChannel(
                    channelId_IMPORTANT,
                    channelName_IMPORTANT,
                    NotificationManager.IMPORTANCE_HIGH
                )
            manager.createNotificationChannels(listOf(channel1, channel2))
        }
        return manager
    }

    fun createNotification(
        channelId: String = channelId_NORMAL,
        title: String,
        text: String,
        smallIcon: Int = R.drawable.orange,
        largeIcon: Int = R.drawable.large_orange,
        autoCancel: Boolean = true,
        pi: PendingIntent? = null
    ): Notification = NotificationCompat.Builder(Engine.context, channelId).run {
        setContentTitle(title)
        setContentText(text)
        setSmallIcon(smallIcon)
        setLargeIcon(BitmapFactory.decodeResource(Engine.context.resources, largeIcon))
        setAutoCancel(autoCancel)
        pi?.let { this.setContentIntent(it) }
        val notification = build()
        notification
    }
}
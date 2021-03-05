package com.guc.kframe.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.util.Base64
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.*

/**
 * Created by guc on 2020/5/8.
 * 描述：图片工具
 */
object ImageUtils {

    /**
     * bitmap图片进行旋转
     */
    fun rotateBitmapIfRequired(filePath: String, bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(filePath)
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotateBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle()
        return rotateBitmap
    }

    /**
     * 从图片uri获取bitmap
     */
    fun getBitmapFromUri(uri: Uri, contentResolver: ContentResolver) =
        contentResolver.openFileDescriptor(uri, "r")?.use {
            BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
        }

    /**
     * 将Bitmap对象 保存到本地
     * 需要读写内存卡的权限，否则会发生异常
     *
     * @param context
     * @param mBitmap
     * @param filePath 图片路径  xxx/xxx/xx.jpg
     * @return
     */
    fun saveBitmap(
        context: Context,
        mBitmap: Bitmap,
        filePath: String
    ): String? {
        val filePic: File
        try {
            filePic = File(filePath)
            if (!filePic.exists()) {
                filePic.parentFile.mkdirs()
                filePic.createNewFile()
            }
            FileOutputStream(filePic).use {
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }

        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return filePic.absolutePath
    }

    /**
     * 压缩图片至指定大小（默认500K）以内
     *
     * @param bitmap
     * @param filePath
     * @return
     */
    fun compressBitmap2FileIn500K(bitmap: Bitmap, filePath: String, maxSize: Int = 500): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos) //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        var options = 100
        while (baos.toByteArray().size / 1024 > maxSize) { //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset() //重置baos即清空baos
            options -= 10 //每次都减少10
            if (options < 0) options = 0
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                options,
                baos
            ) //这里压缩options%，把压缩后的数据存放到baos中
            if (options == 0) break
        }
        return try {
            val file = File(filePath)
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            FileOutputStream(file).use {
                it.write(baos.toByteArray())
            }//将压缩后的图片保存的本地上指定路径中
            baos.close()
            filePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 文件保存至应用缓存中
     *
     * @param originalPath
     * @param fileName 文件名
     */
    fun pictureSaveToCache(
        originalPath: String,
        cachePath: String,
        fileName: String
    ): String? = compressBitmap2FileIn500K(
        BitmapFactory.decodeFile(
            originalPath,
            BitmapFactory.Options().apply { inSampleSize = 2 }),
        cachePath + fileName
    )

    /**
     * 异步压缩图片
     */
    fun compressImagesWithIoThread(
        images: List<String>,
        cachePath: String,
        observer: Observer<List<String>>
    ) {
        Observable.create(ObservableOnSubscribe<List<String>> {
            val imageListPath = mutableListOf<String>()
            for (imageItem in images) {

                val path = pictureSaveToCache(
                    imageItem,
                    cachePath,
                    imageItem.substring(imageItem.lastIndexOf("/") + 1)
                )
                if (path != null)
                    imageListPath.add(path)
            }
            it.onNext(imageListPath)
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)
    }

    /**
     * 在bitmap左上角添加水印
     *
     * @param context
     * @param bitmap      原bitmap
     * @param text        水印文字
     * @param size        文字大小
     * @param color       文字颜色
     * @param paddingLeft 距左侧距离
     * @param paddingTop  距顶部距离
     * @return 添加过水印后的Bitmap
     */
    fun drawTextToLeftTop(
        context: Context,
        bitmap: Bitmap,
        text: String,
        size: Int = 18,
        color: Int = Color.parseColor("#000000"),
        paddingLeft: Int = 30,
        paddingTop: Int = 30
    ): Bitmap? {
        val paint =
            Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = color
        paint.textSize = ScreenUtils.dp2px(size, context).toFloat()
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        return drawTextToBitmap(
            bitmap, text, paint,
            ScreenUtils.dp2px(paddingLeft, context),
            ScreenUtils.dp2px(paddingTop, context) + bounds.height()
        )
    }

    /**
     * 在bitmap左下角添加文字水印
     *
     * @param context
     * @param bitmap        原bitmap
     * @param text          水印文字
     * @param size          文字大小
     * @param color         文字颜色
     * @param paddingLeft   距左侧距离
     * @param paddingBottom 距底部距离
     * @return 添加过水印后的Bitmap
     */
    fun drawTextToLeftBottom(
        context: Context,
        bitmap: Bitmap,
        text: String,
        size: Int = 18,
        color: Int = Color.parseColor("#000000"),
        paddingLeft: Int = 30,
        paddingBottom: Int = 30
    ): Bitmap? {
        val paint =
            Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = color
        paint.textSize = ScreenUtils.dp2px(size, context).toFloat()
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        return drawTextToBitmap(
            bitmap, text, paint, ScreenUtils.dp2px(paddingLeft, context),
            bitmap.height - ScreenUtils.dp2px(paddingBottom, context)
        )
    }


    //图片上绘制文字
    private fun drawTextToBitmap(
        bitmap: Bitmap, text: String,
        paint: Paint, paddingLeft: Int, paddingTop: Int
    ): Bitmap {
        paint.isDither = true // 获取跟清晰的图像采样
        paint.isFilterBitmap = true // 过滤一些
        val w = bitmap.width
        val h = bitmap.height
        val bitmapNew = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565)
        Canvas(bitmapNew).apply {
            //在画布 0，0坐标上开始绘制原始图片
            drawBitmap(bitmap, 0f, 0f, null)
            drawText(text, paddingLeft.toFloat(), paddingTop.toFloat(), paint)
            save()
            restore()
        }
        bitmap.recycle()
        return bitmapNew
    }


    /**
     * 图片转为base64
     */
    fun picture2Base64(path: String): String? {
        var base64: String? = null
        try {
            FileInputStream(File(path)).use {
                val buffer = ByteArray(it.available())
                it.read(buffer)
                base64 = Base64.encodeToString(buffer, Base64.DEFAULT)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return base64
    }
}
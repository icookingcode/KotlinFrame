package com.guc.kframe.system.update

import android.os.Parcel
import android.os.Parcelable

/**
 * 版本升级
 */
class BeanVersion : Parcelable {
    var needUpdate: Boolean = false
    var forceUpdate: Boolean = false
    var fileUrl: String = ""
    var fileMd5: String = ""
    var fileSize: String = ""
    var newVersion: String = ""
    var updateJournal: String = ""
    var mSaveFileDir: String = ""
    var mSaveFileName: String = ""
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByte(if (needUpdate) 1.toByte() else 0.toByte())
        dest.writeByte(if (forceUpdate) 1.toByte() else 0.toByte())
        dest.writeString(fileUrl)
        dest.writeString(fileMd5)
        dest.writeString(fileSize)
        dest.writeString(newVersion)
        dest.writeString(updateJournal)
        dest.writeString(mSaveFileDir)
        dest.writeString(mSaveFileName)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<BeanVersion> = object : Parcelable.Creator<BeanVersion> {
            override fun createFromParcel(source: Parcel): BeanVersion? {
                return BeanVersion().apply {
                    needUpdate = source.readByte() != 0.toByte()
                    forceUpdate = source.readByte() != 0.toByte()
                    fileUrl = source.readString() ?: ""
                    fileMd5 = source.readString() ?: ""
                    fileSize = source.readString() ?: ""
                    newVersion = source.readString() ?: ""
                    updateJournal = source.readString() ?: ""
                    mSaveFileDir = source.readString() ?: ""
                    mSaveFileName = source.readString() ?: ""
                }
            }

            override fun newArray(size: Int): Array<BeanVersion?> {
                return arrayOfNulls(size)
            }
        }
    }

}
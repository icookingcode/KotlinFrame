package com.guc.kotlinframe.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import com.guc.kframe.adapter.CommonAdapter4ListView
import com.guc.kframe.adapter.ViewHolder4ListView
import com.guc.kframe.base.BaseActivity
import com.guc.kframe.utils.FileUtils
import com.guc.kframe.utils.LogG
import com.guc.kotlinframe.R
import kotlinx.android.synthetic.main.activity_file_select.*
import java.io.File

class FileSelectActivity : BaseActivity() {
    private lateinit var privateRootDir: File

    // The path to the "images" subdirectory
    private lateinit var imagesDir: File

    // Array of files in the images subdirectory
    private lateinit var imageFiles: Array<File>

    // Array of filenames corresponding to imageFiles
    private lateinit var imageFilenames: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_select)
        privateRootDir = filesDir
        imagesDir = File(privateRootDir, "images")
        imageFiles = imagesDir.listFiles() ?: arrayOf()
        imageFilenames = mutableListOf()
        imageFiles.forEach {
            imageFilenames.add(it.absolutePath)
        }
        lvFiles.adapter = object : CommonAdapter4ListView<String>(
            this,
            android.R.layout.simple_list_item_1,
            imageFilenames
        ) {
            override fun bindData(viewHolder: ViewHolder4ListView, position: Int, data: String?) {
                viewHolder.getView<TextView>(android.R.id.text1).text = data
            }
        }
        titleLayout.onLeftClicked = { _ ->
            setResult(Activity.RESULT_CANCELED, null)
            this.finish()
        }
        lvFiles.setOnItemClickListener { _, _, position, _ ->
            val requestFile = File(imageFilenames[position])
            val fileUri: Uri? = try {
                FileUtils.getUriForFile(requestFile)
            } catch (e: IllegalArgumentException) {
                LogG.loge(
                    "File Selector",
                    "The selected file can't be shared: $requestFile"
                )
                null
            }
            setResult(Activity.RESULT_OK, Intent().apply {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                data = fileUri
            })
            finish()
        }
    }
}
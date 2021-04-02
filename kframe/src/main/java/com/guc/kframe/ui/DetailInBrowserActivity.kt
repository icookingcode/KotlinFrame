package com.guc.kframe.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import com.guc.kframe.R
import com.guc.kframe.base.BaseActivity
import com.guc.kframe.utils.LogG
import kotlinx.android.synthetic.main.activity_detail_in_browser.*

/**
 * Created by guc on 2019/12/9.
 * 描述：通过url加载详情
 */
class DetailInBrowserActivity : BaseActivity() {
    private var mUrl: String? = null
    private var mTitle: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_in_browser)
        initView()
        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        //加上下面这段代码可以使网页中的链接不以浏览器的方式打开
        webViewContent.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                val url = request.url.toString()
                val hit = view.hitTestResult
                if (TextUtils.isEmpty(hit.extra) || hit.type == 0) {
                    LogG.logv("DetailInBrowserActivity", "网页发生了重定向")
                }
                return if (url.startsWith("http://") || url.startsWith("https://")) {
                    view.loadUrl(url)
                    false //不拦截
                } else {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        LogG.logv("DetailInBrowserActivity", e.toString())
                    }
                    true //拦截
                }
            }
        }
        val webSettings: WebSettings = webViewContent.settings
        webSettings.domStorageEnabled = true
        webSettings.blockNetworkImage = false //解决图片不显示
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.loadsImagesAutomatically = true
        webSettings.javaScriptEnabled = true
        try {
            webViewContent.removeJavascriptInterface("searchBoxJavaBridge")
            webViewContent.removeJavascriptInterface("accessibilityTraversal")
            webViewContent.removeJavascriptInterface("accessibility")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        webViewContent.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(
                view: WebView,
                newProgress: Int
            ) {
                super.onProgressChanged(view, newProgress)
                progressBar.progress = newProgress
                progressBar.visibility =
                    if (newProgress < 100) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            }
        }
        webViewContent.loadUrl(mUrl)
    }

    private fun initView() {
        mUrl = intent.getStringExtra("url")
        mTitle = intent.getStringExtra("title")
        progressBar.visibility = View.VISIBLE
        titleLayout.title = mTitle ?: getString(R.string.str_detail)
    }

    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webViewContent.canGoBack()) {
            webViewContent.goBack() // goBack()表示返回WebView的上一页面
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        @JvmStatic
        fun showDetail(
            context: Context,
            url: String?,
            title: String = "详情"
        ) {
            val intent = Intent(context, DetailInBrowserActivity::class.java)
            intent.putExtra("url", url)
            intent.putExtra("title", title)
            context.startActivity(intent)
        }
    }
}
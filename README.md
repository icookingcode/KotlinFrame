# KotlinFrame
Kotlin App 框架（MVVM）
* Model :数据模型部分
* View ：界面展示部分
* ViewModel ：Model和View的桥梁，从而实现业务与界面展示的分离

![MVVM架构示意图](https://github.com/icookingcode/KotlinFrame/blob/master/snapshoot/mvvm.png)
## 自定义控件
* TitleLayout   自定义标题栏
* LoadingDialog 加载框
* DialogSelect  单选/多选框
* DialogUpdate  升级框，带下载功能
```
  // 仅需URL即可下载
  val dialogUpdate = DialogUpdate.getInstanceWithArguments {
       putParcelable(DialogUpdate.DATA,BeanVersion().apply { fileUrl = "https://down.qq.com/qqweb/QQ_1/android_apk/Android_8.3.6.4590_537064458.apk" })
  }
  dialogUpdate.show(supportFragmentManager,"dialog")
```
 <img src="https://github.com/icookingcode/KotlinFrame/blob/master/snapshoot/Screenshot_1591588183.png"  height="640" width="360">

* LooperTextView  滚动展示的TextView
* NoScrollViewPager  禁止滑动的ViewPager
* RichTextView  标题和内容同行不同样式
* FixedGridView/FixedListView  解决ScrollView中嵌套高度显示不正常的问题（1行半）
* SyncHorizontalScrollView  实现同步滚动HorizontalScrollView
* CornerImageView  圆角矩形/圆形头像
* Banner  广告轮播

## 系统工具
* SystemHttp 网络请求工具系统
* SystemCrash 崩溃处理系统
* SystemPermission 权限申请系统
* SystemImageLoader 图片加载工具
* SystemDownload  下载工具

## Android 进程间通讯
* Intent
* 使用文件共享
* 使用Messager(底层AIDL实现)
* 使用AIDL
* 使用ContentProvider
* 使用Socket
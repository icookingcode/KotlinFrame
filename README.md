# KotlinFrame
Kotlin App 框架（MVVM）
* Model :数据模型部分
* View ：界面展示部分
* ViewModel ：Model和View的桥梁，从而实现业务与界面展示的分离

![MVVM架构示意图](https://github.com/icookingcode/KotlinFrame/blob/master/snapshoot/mvvm.png)
## How to use
### adding to project
To use this frmame,add this dependency to the build.gradle of the app:
```
implementation 'com.guc.kframe:kframe:1.0.0'
```
### Simple usage
1. Config Engine at the app entry:
```
 val config = Config().apply {
            currentMode = Config.MODEL_DEBUG
            urlDebug = "http://192.168.44.141:8099/"
            urlBeta = "http://192.168.44.141:8099/"
            urlRelease = "http://192.168.44.141:8099/"
        }
 Engine.init(this, config)
```
2. Your Activity inheritance BaseActiviy,then you can use AcitvityCollector to manage you App.
3. Get the http system:
```
val http = SystemManager.getSystem(SystemHttp::class.java)
```
4. More functions wait for you to discover.

## 自定义控件
* TitleLayout   自定义标题栏
* LoadingDialog 加载框
* DialogSelect  单选/多选框
* DialogUpdate  升级框，带下载功能
```
   val dialogUpdate = DialogUpdate.getInstanceWithArguments {
        putParcelable(
             DialogUpdate.DATA,
             BeanVersion().apply {
                  fileUrl =
                       "https://down.qq.com/qqweb/QQ_1/android_apk/Android_8.3.6.4590_537064458.apk"
                  fileSize = "84161244"
                  updateJournal = "1.bug修复"
                  newVersion = "V1.0.0"
           })
       }
   dialogUpdate.show(supportFragmentManager, "dialog")
```
<center class="half">
 <img src="https://github.com/icookingcode/KotlinFrame/blob/master/snapshoot/Screenshot_1591588183.png"  height="576" width="306"/><img src="https://github.com/icookingcode/KotlinFrame/blob/master/snapshoot/Screenshot_1591922802.png"  height="576" width="306"/>
</center>

* LooperTextView  滚动展示的TextView
* NoScrollViewPager  禁止滑动的ViewPager
* RichTextView  标题和内容同行不同样式
* FixedGridView/FixedListView  解决ScrollView中嵌套高度显示不正常的问题（1行半）
* SyncHorizontalScrollView  实现同步滚动HorizontalScrollView
* CornerImageView  圆角矩形/圆形头像
* Banner  广告轮播
* WaterMarkView  自定义水印

## 系统工具
* SystemHttp 网络请求工具系统
* SystemCrash 崩溃处理系统
* SystemPermission 权限申请系统
* SystemImageLoader 图片加载工具
* SystemDownload  下载工具
* SystemWaterMark  水印工具
```
    //开启水印  activity 需继承 BaseActivity
    SystemManager.getSystem(SystemWaterMark::class.java)?.apply {
        enable = true
        text = "自定义水印"
    }
```
<img src="https://github.com/icookingcode/KotlinFrame/blob/master/snapshoot/Screenshot_1592188658.png"  height="576" width="306"/>

## 适配器封装
* CommonAdapter4ListView  通用ListView适配器封装
* CommonAdapter4Rcv  通用RecyclerView适配器封装
* ViewPager2FragmentStateAdapter  通用ViewPager2+Fragment适配器封装
* ViewPagerAdapter  通用ViewPagerAdapter封装

## Android 进程间通讯
* Intent
* 使用文件共享
* 使用Messager(底层AIDL实现)
* 使用AIDL
* 使用ContentProvider
* 使用Socket
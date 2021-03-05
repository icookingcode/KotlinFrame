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
implementation 'com.guc.kframe:kframe:1.1.6'
```
### Proguard rule
```
-dontwarn com.guc.kframe.**
-keep class com.guc.kframe.** { *;}
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
```
自定义xml属性
leftType ：左侧图标类型。（none:不显示  finish:返回）
leftImage：设置左侧图标。
title：设置标题。
titleGravity：设置标题位置。（start：左侧，左图标右侧  center：居中）
titleTextColor：设置标题颜色。
titleTextSize：设置标题字体大小。
rightType：右侧功能样式。（none：不显示  text：文字样式  image：图标样式  image_text：图标文字样式  text_spinner：文字带功能弹窗样式  image_spinner：图标文字带功能弹窗样式）
rightSpinnerType：右侧功能弹窗样式。（text：文字样式  image_text：图标文字样式）
rightText：右侧功能文字。
rightTextColor：设置右侧文字颜色。
rightImage：设置右侧图标。
```
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
* AdaptiveWithListView  宽度自适应ListView
* SyncHorizontalScrollView  实现同步滚动HorizontalScrollView
* CornerImageView  圆角矩形/圆形头像
* Banner  广告轮播
* WaterMarkView  自定义水印
* OptionsPickerView  仿ios参数选择
```
 var mOptionPicker = OptionsPickerView<String>(this).apply {
          //参数及样式设置
          titleBackgroundColor = Color.parseColor("#FFFFFF")
      }.create(object :
      OptionsPickerView.OnOptionsSelectListener {
           override fun onOptionsSelect(
                options1: Int,
                options2: Int,
                options3: Int,
                v: View?
           ) {
                    //回调
           }
      }).apply {
            setPicker(mOptions!!, mOptions2Opt)//设置数据 必须再create()后调用才有效
      }
      mOptionPicker.show()
```
* TimePickerView  仿ios时间选择
```
  var mOptionPickerDate = TimePickerView(this).apply {
                //设置参数
                type = WheelTime.Type.YEAR_MONTH_DAY //选择日期类型
                titleText = "请选择日期"
                titleBackgroundColor = Color.parseColor("#FFFFFF")
                submitTextColor = Color.parseColor("#1E90FF")
                cancelTextColor = Color.parseColor("#B0C4DE")
                isDialogM = true  //窗口样式
                cancelable = true  //点击外部可取消
            }.create(object : TimePickerView.OnTimeSelectListener {
                @SuppressLint("SimpleDateFormat")
                override fun onTimeSelect(date: Date?, v: View?) {
                    date?.let {
                        //回调
                    }
                }
            })
            mOptionPickerDate.show()
```
* SearchView 搜索框
* RiseNumberTextView 带自增动画效果的数字展示TextView
* PieChartView  自定义PieChart
* DialogConfirm  自定义Dialog
* EmptyView  空提示控件
* BannerIndicator  轮播指示控件

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
* CommonPagerAdapter  通用ViewPagerAdapter封装
* GroupAdapter  ExpandableListView 分组Adapter封装

## 工具类
* TimeFormatUtils  时间格式转换工具
* ImageUtils  图片处理工具
* KeyWordUtils  文本关键字改色处理工具
* SPUtils  SharedPreferences操作工具，请在使用前调用 SPUtils.getSharedPreferences()获取preference实例
* CacheManagerUtils  缓存管理工具（获取缓存大小/清理缓存） 
* AssetsUtils  读取assets资源
* LocationUtils  原生定位工具
* RunStateRegister  监听App前台/后台运行

## Android 进程间通讯
* Intent
* 使用文件共享
* 使用Messager(底层AIDL实现)
* 使用AIDL
* 使用ContentProvider
* 使用Socket

## Kotlin协程知识
* GlobalScope.launch{}  创建一个顶级协程 //不阻塞当前线程
* runBlocking{}  创建一个协程作用域 //阻塞当前线程 
* launch{}  在协程作用域内创建一个协程  
* coroutineScope{}  在协程作用域内创建一个子协程作用域 //阻塞当前协程
* async{}.await()  代码块中的代码会立刻执行，当调用await()时，会阻塞当前协程，直到获取结果
* withContext(Dispatchers.Default){} 代码块会立即执行，同时阻塞协程，直到获取结果
```
//协程作用域的常用创建方式(实际项目中)
    val job = Job()
    val scope = CoroutineScope(job)
    scope.launch {//不阻塞线程
        //
    }
    job.cancel()
```
* suspendCoroutine{continuation -> } 必须在挂起函数或协程作用域中才可调用，将当前协程挂起，然后在普通线程中执行lambda表达式中的代码，再调用resume() 或 resumeWithException(e)让协程恢复


## 关于我
Name: Guchao  
Email: happygc913@gmail.com / happygc@126.com  
CSDN: [snow_lyGirl](https://blog.csdn.net/qq_31028313)  
GitHub: [GuchaoGit](https://github.com/GuchaoGit?tab=repositories)  
Gitee:[GuChaoGitee](https://gitee.com/guchaogitee/projects)  
加入QQ群:128937635  
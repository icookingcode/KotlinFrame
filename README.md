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

## 系统工具
* SystemHttp 网络请求工具系统
* SystemCrash 崩溃处理系统
* SystemPermission 权限申请系统
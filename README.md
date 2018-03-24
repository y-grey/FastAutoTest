FastAutoTest
============

 FastAutoTest是一个基于Appium的快速自动化框架.

 * 自动采集设备信息，无需手动获取，当USB口接入一台新设备可直接开启自动化测试工作；
 * 自动配置大部分信息，无需手动配置，摒弃TestNG群控时需要手工配置多个suite.xml的方式；
 * 自动启动Appium服务，无需手动打开，由自动化工作开始的时候通过代码打开；
 * 自动安装新版本软件，无需手动安装，由自动化工作开始前通过对比版本进行软件更新。

使用
--------
__1、做少量必要的配置__

__2、填写Bugly所需要的权限__
__3、添加依赖__
```groovy
```
__4、初始化__
```java
        FastAuto.run(Configure.get()
                .setAdb("adb")
                .setNode("node")
                .setApkPath("C:/Users/dell1/android-studio/workspace/workspace-2018/AppiumAutoTest/app/apk/app-debug.apk")
                .setAppPackage("com.tencent.mobileqq")
                .setAppActivity("com.tencent.mobileqq.activity.SplashActivity")
                .setAppiumMainJs("C:/Users/dell1/AppData/Local/Programs/appium-desktop/resources/app/node_modules/appium/build/lib/main.js")
                .addTestBean(new TestBean().setName("testqq").setClasses(new Class[]{TestMessage.class, TestContacts.class})));
```
说明：当你有多个Test可以通过addTestBean方法添加，每个Test通过setName方法设置名字，通过setClasses方法设置Class.
      如果你配置好了adb和node环境，待测Apk已经安装，那么初始化可以省略这些步骤，如下
```java
        FastAuto.run(Configure.get()
                .setAppPackage("com.tencent.mobileqq")
                .setAppActivity("com.tencent.mobileqq.activity.SplashActivity")
                .setAppiumMainJs("C:/Users/dell1/AppData/Local/Programs/appium-desktop/resources/app/node_modules/appium/build/lib/main.js")
                .addTestBean(new TestBean().setName("testqq").setClasses(new Class[]{TestMessage.class, TestContacts.class})));
```

原理
--------

 <!-- [卡顿监测之真正轻量级的卡顿监测工具BlockDetectUtil（仅一个类）](http://blog.csdn.net/u012874222/article/details/79400154) -->
 <!--  -->
 <!-- [卡顿监测之远程收集log（潜入Bugly这趟顺风车）](http://blog.csdn.net/u012874222/article/details/79417549) -->

 License
 -------
    Copyright [2018] [yph]
 
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
 
      http://www.apache.org/licenses/LICENSE-2.0
 
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 

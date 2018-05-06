FastAutoTest
============

 FastAutoTest是一个基于Appium的快速自动化框架.

 * 自动采集设备信息，无需手动获取，当USB口接入一台新设备可直接开启自动化测试工作；
 * 自动配置大部分信息，无需手动配置，摒弃TestNG群控时需要手工配置多个suite.xml的方式；
 * 自动启动Appium服务，无需手动打开，由自动化工作开始的时候通过代码打开；
 * 自动安装新版本软件，无需手动安装，由自动化工作开始前通过对比版本进行软件更新；
 * 自动采集性能数据（CPU，内存，流量）和对应执行栈，并在报告中展示；
 * 自动进行卡顿监测，内存泄漏监测，crash监测，收集日志和对应执行栈，并在报告中展示；
 * 内部DefaultReport已替换为修改版的  [ReportNG](https://github.com/dwdyer/reportng)，更清晰地展示测试结果。

使用
--------
__1、Download__

_Gradle_
* Step 1. 在项目根build.gradle文件中增加maven仓库依赖
```groovy
    allprojects {
        repositories {
            ...
            maven { url "https://dl.bintray.com/yph/maven" }
        }
    }
```
* Step 2. 在项目module的build.gradle添加依赖
```groovy
    dependencies {
      compile 'yph:fastautotest:1.3.0'
    }
```
_Maven_
```xml
    <dependency>
      <groupId>yph</groupId>
      <artifactId>fastautotest</artifactId>
      <version>1.3.0</version>
      <type>pom</type>
    </dependency>
```
__2、初始化__

以手Q为例子，创建一个启动类TestFastAuto，然后进行一些必要的配置
```java
public class TestFastAuto {
    public static void main(String[] args) {
        FastAuto.run(Configure.get()
                .setNode("node")//node路径
                .setApkPath("C:/Users/dell1/android-studio/workspace/workspace-2018/AppiumAutoTest/app/apk/app-debug.apk")
                .setAppPackage("com.tencent.mobileqq")
                .setAppActivity("com.tencent.mobileqq.activity.SplashActivity")
                .setAppiumMainJs("C:/Users/dell1/AppData/Local/Programs/appium-desktop/resources/app/node_modules/appium/build/lib/main.js")
                .addTestBean(new TestBean().setName("testqq").setClasses(new Class[]{TestMessage.class, TestContacts.class})));
    }
}
```
_说明：当你有多个Test可以通过addTestBean方法添加，每个Test通过setName方法设置名字，通过setClasses方法设置Class，
       每个Test原则上是执行前要重启应用，执行完成后关闭应用，但你可以通过调用TestBean的notRestart()方法来实现不重启.
      如果你配置好了node环境，并且待测Apk已经安装，那么初始化可以省略这些步骤，如下_
      
```java
        FastAuto.run(Configure.get()
                .setAppPackage("com.tencent.mobileqq")
                .setAppActivity("com.tencent.mobileqq.activity.SplashActivity")
                .setAppiumMainJs("C:/Users/dell1/AppData/Local/Programs/appium-desktop/resources/app/node_modules/appium/build/lib/main.js")
                .addTestBean(new TestBean().setName("testqq").setClasses(new Class[]{TestMessage.class, TestContacts.class})));
```
__3、编写Test__
```java
public class TestMessage extends BaseTest {
    @FindBys({@FindBy(className = "android.widget.TabWidget"),@FindBy(className = "android.widget.FrameLayout")})
    List<WebElement> list;
    @Override
    protected void addCap(DesiredCapabilities caps){//假如你想添加参数，可重写此方法添加
    }
    @Test
    public void operation() {
        list.get(0).click();
    }
}
```
_说明：编写Test类继承自BaseTest,然后写一个方法，添加@Test注解后就可以编写逻辑代码了.
假如你想添加参数，可重写addCap方法添加,但一般不需要_

__4、结果展示__
待传图...

Detail
--------

  [Appium自动化之框架搭建](https://blog.csdn.net/u012874222/article/details/79485222)
  
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
 

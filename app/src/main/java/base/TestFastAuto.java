package base;

import testqq.TestContacts;
import testqq.TestMessage;
import yph.base.FastAuto;
import yph.bean.Configure;
import yph.bean.TestBean;

public class TestFastAuto {
    public static void main(String[] args) {
        FastAuto.run(Configure.get()
//                .setNode("node")
//                .setApkPath("C:/Users/dell1/android-studio/workspace/workspace-2018/AppiumAutoTest/app/apk/app-debug.apk")
                .setRetryCount(3)
                .setAppPackage("com.tencent.mobileqq")
                .setAppActivity("com.tencent.mobileqq.activity.SplashActivity")
                .setAppiumMainJs("C:/Users/dell1/AppData/Local/Programs/appium-desktop/resources/app/node_modules/appium/build/lib/main.js")
                .addTestBean(new TestBean("登录模块",new Class[]{TestMessage.class}))
                .addTestBean(new TestBean("消息模块",new Class[]{TestContacts.class}).notRestart()));
    }
}

package yph.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yph on 2018/3/21.
 */

public class Configure {
    private String adb = "adb";
    private String node = "node";
    private String appiumMainJs = "";
    private String appPackage = "";
    private String appActivity = "";
    private String apkPath = "";
    private List<TestBean> testBeans = new ArrayList();

    public List<TestBean> getTestBeans() {
        return testBeans;
    }

    public Configure addTestBean(TestBean testBean) {
        testBeans.add(testBean);
        return this;
    }

    public static Configure get(){
        return new Configure();
    }

    public Configure setAdb(String adb) {
        this.adb = adb;
        return this;
    }

    public Configure setNode(String node) {
        this.node = node;
        return this;
    }

    public Configure setAppiumMainJs(String appiumMainJs) {
        this.appiumMainJs = appiumMainJs;
        return this;
    }

    public Configure setAppPackage(String appPackage) {
        this.appPackage = appPackage;
        return this;
    }

    public Configure setAppActivity(String appActivity) {
        this.appActivity = appActivity;
        return this;
    }

    public Configure setApkPath(String apkPath) {
        this.apkPath = apkPath;
        return this;
    }


    public String getAdb() {
        return adb;
    }

    public String getNode() {
        return node;
    }

    public String getAppiumMainJs() {
        return appiumMainJs;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public String getAppActivity() {
        return appActivity;
    }

    public String getApkPath() {
        return apkPath;
    }

}

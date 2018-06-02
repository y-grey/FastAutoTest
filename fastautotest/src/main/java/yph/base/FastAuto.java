package yph.base;

import org.testng.TestNG;
import org.testng.reporters.JUnitXMLReporter;
import org.testng.xml.XmlSuite;
import org.uncommons.reportng.HTMLReporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import yph.bean.Configure;
import yph.helper.XmlSuiteBuilder;
import yph.listener.AnnotationListener;
import yph.listener.TestResultListener;
import yph.utils.ApkUtil;
import yph.utils.CmdUtil;
import yph.utils.Log;

public class FastAuto {
    public static void run(Configure configure) {
        CmdUtil.get().init(configure.getAppPackage());
        List<XmlSuite> testList = getTestList(configure);
        System.setProperty("org.uncommons.reportng.escape-output", "false");
        TestNG testng = new TestNG();

        testng.setXmlSuites(testList);
        testng.setUseDefaultListeners(false);
        testng.addListener(new AnnotationListener(configure.getRetryCount()));
        testng.addListener(new TestResultListener());
        testng.addListener(new HTMLReporter());
        testng.addListener(new JUnitXMLReporter());
        testng.setSuiteThreadPoolSize(testList.size());

        testng.run();
    }

    private static List<XmlSuite> getTestList(Configure configure) {
        List<XmlSuite> testList = new ArrayList<>();
        List<String> devices = CmdUtil.get().getDevices();
        for (int i = 0; i < devices.size(); i++) {
            String deviceUdid = devices.get(i);
//            checkUpdate(deviceName,configure);
            testList.add(new XmlSuiteBuilder(i,
                    deviceUdid,
                    CmdUtil.get().getDeviceName(deviceUdid),
                    CmdUtil.get().getPlatformVersion(deviceUdid),
                    configure)
                    .build());
            CmdUtil.get().clearLog(deviceUdid);
        }
        return testList;
    }

    private static void checkUpdate(String deviceName,Configure configure){
        String apkPath = configure.getApkPath();
        if(!apkPath.equals("")) {
            Map map = ApkUtil.readApk(apkPath);
            int newV = (int) map.get("versionCode");
            int curV = CmdUtil.get().getVersionCode(deviceName);
            Log.i("curV:"+curV + "   newV:"+newV);
            if(newV > curV){
//                RuntimeUtil.installApk(configure.getAdb(),device,configure.getApkPath());
            }
        }
    }
}

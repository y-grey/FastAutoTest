package yph.base;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;
import org.uncommons.reportng.HTMLReporter;
import org.uncommons.reportng.JUnitXMLReporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import yph.bean.Configure;
import yph.utils.ApkUtil;
import yph.utils.RuntimeUtil;
import yph.utils.XmlSuiteBuilder;

public class FastAuto {

    public static void run(Configure configure) {
        List<XmlSuite> testList = getTestList(configure);
        if(testList.isEmpty())return;

        TestListenerAdapter tla = new TestListenerAdapter();
        TestNG testng = new TestNG();

        testng.setXmlSuites(testList);
        testng.addListener(tla);
        testng.setUseDefaultListeners(false);
        testng.addListener(new HTMLReporter());
        testng.addListener(new JUnitXMLReporter());
        testng.setSuiteThreadPoolSize(testList.size());

        testng.run();
        System.out.println("ConfigurationFailures: "+tla.getConfigurationFailures());
        System.out.println("getFailedTests()" + tla.getFailedTests());
    }

    private static List<XmlSuite> getTestList(Configure configure) {
        List<XmlSuite> testList = new ArrayList<>();
        List<String> devices = RuntimeUtil.getDevices(configure.getAdb());
        for (int i = 0; i < devices.size(); i++) {
            String deviceName = devices.get(i);
//            checkUpdate(deviceName,configure);
            testList.add(new XmlSuiteBuilder(i,
                    deviceName,
                    RuntimeUtil.getPlatformVersion(configure.getAdb(),deviceName),
                    configure)
                    .build());
        }
        return testList;
    }

    private static void checkUpdate(String device, Configure configure){
        String apkPath = configure.getApkPath();
        if(!apkPath.equals("")) {
            Map map = ApkUtil.readApk(apkPath);
            int newV = (int) map.get("versionCode");
            int curV = RuntimeUtil.getVersionCode(configure.getAdb(),device,configure.getAppPackage());
            System.out.println("curV:"+curV + "   newV:"+newV);
            if(newV > curV){
//                RuntimeUtil.installApk(configure.getAdb(),device,configure.getApkPath());
            }
        }
    }
}

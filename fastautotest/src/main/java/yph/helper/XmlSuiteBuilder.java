package yph.helper;

import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yph.bean.Configure;
import yph.bean.TestBean;


/**
 * Created by yph on 2018/3/21.
 */

public class XmlSuiteBuilder {

    List<XmlTest> mTests = new ArrayList<>();
    Map<String, String> parameters = new HashMap<>();
    XmlSuite xmlSuite = new XmlSuite();

    public XmlSuiteBuilder(int index, String deviceUdid, String deviceName, String platformVersion, Configure configure) {
        parameters.put("port",(4723+2*index)+"");
        parameters.put("bootstrap_port",(4724+2*index)+"");
        parameters.put("chromedriver_port",(9515+index)+"");
        parameters.put("udid", deviceUdid);
        parameters.put("platformName","Android");
        parameters.put("platformVersion",platformVersion);
        parameters.put("deviceName", deviceName);
        parameters.put("node", configure.getNode());
        parameters.put("appiumMainJs", configure.getAppiumMainJs());
        parameters.put("appPackage", configure.getAppPackage());
        parameters.put("appActivity", configure.getAppActivity());
        parameters.put("app",configure.getApkPath());

        parseTestBeans(configure.getTestBeans());

        xmlSuite.setName(deviceName);
        xmlSuite.setTests(mTests);
    }


    private void parseTestBeans(List<TestBean> testBeans){
        for(TestBean testBean : testBeans){
            String testName = testBean.getName();
            Class[] testClasses = testBean.getClasses();
            List<XmlClass> xmlClassListt = new ArrayList<>();
            for (Class testClass : testClasses) {
                xmlClassListt.add(new XmlClass(testClass));
            }
            XmlTest xmlTest = new XmlTest();
            xmlTest.setName(testName);
            xmlTest.setClasses(xmlClassListt);
            xmlTest.setXmlSuite(xmlSuite);
            mTests.add(xmlTest);
        }
    }

    public XmlSuite build() {
        xmlSuite.setParameters(parameters);
        return xmlSuite;
    }
}

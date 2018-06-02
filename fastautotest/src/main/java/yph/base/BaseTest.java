package yph.base;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import java.net.URL;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AutomationName;
import yph.helper.RestartTestHelper;
import yph.performance.PerforMonitor;
import yph.utils.Log;
import yph.utils.SleepUtil;

import static io.appium.java_client.remote.AndroidMobileCapabilityType.APP_ACTIVITY;
import static io.appium.java_client.remote.AndroidMobileCapabilityType.APP_PACKAGE;
import static io.appium.java_client.remote.AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS;
import static io.appium.java_client.remote.AndroidMobileCapabilityType.DONT_STOP_APP_ON_RESET;
import static io.appium.java_client.remote.AndroidMobileCapabilityType.NO_SIGN;
import static io.appium.java_client.remote.AndroidMobileCapabilityType.RESET_KEYBOARD;
import static io.appium.java_client.remote.AndroidMobileCapabilityType.UNICODE_KEYBOARD;
import static io.appium.java_client.remote.MobileCapabilityType.APP;
import static io.appium.java_client.remote.MobileCapabilityType.AUTOMATION_NAME;
import static io.appium.java_client.remote.MobileCapabilityType.DEVICE_NAME;
import static io.appium.java_client.remote.MobileCapabilityType.NO_RESET;
import static io.appium.java_client.remote.MobileCapabilityType.PLATFORM_NAME;
import static io.appium.java_client.remote.MobileCapabilityType.PLATFORM_VERSION;

public class BaseTest {

    @Parameters({"node", "appiumMainJs", "port", "bootstrap_port", "chromedriver_port", "udid"})
    @BeforeSuite
    public void startServer(String node, String appiumMainJs, String port, String bootstrapPort, String chromeDriverPort, String udid) {
        AppiumServer.start(node, appiumMainJs, port, bootstrapPort, chromeDriverPort, udid);
    }

    @Parameters({"port", "platformName", "platformVersion", "deviceName", "appPackage", "appActivity", "app", "udid"})
    @BeforeTest
    public void setUp(String appiumPort, String platformName, String platformVersion, String deviceName, String appPackage,
                      String appActivity, String app, String udid) {
        if (!RestartTestHelper.isCurTestRestart()) return;
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(PLATFORM_NAME, platformName);
        caps.setCapability(PLATFORM_VERSION, platformVersion);
        caps.setCapability(DEVICE_NAME, deviceName);
        caps.setCapability(NO_RESET, true);
        caps.setCapability(APP, app);
        caps.setCapability(APP_PACKAGE, appPackage);
        caps.setCapability(APP_ACTIVITY, appActivity);
        caps.setCapability(UNICODE_KEYBOARD, true);
        caps.setCapability(RESET_KEYBOARD, true);
        caps.setCapability(AUTO_GRANT_PERMISSIONS, true);
        caps.setCapability(DONT_STOP_APP_ON_RESET, true);
        caps.setCapability(NO_SIGN, true);//表示不重签名app在设置为true的情况下
        if (isLargeThan4d4(platformVersion))
            caps.setCapability(AUTOMATION_NAME, AutomationName.ANDROID_UIAUTOMATOR2);
        addCap(caps);
        AndroidDriver driver = newAndroidDriver(appiumPort, caps);
        androidDriverTl.set(driver);
        PerforMonitor perforMonitor = new PerforMonitor(deviceName, udid, appPackage);
        perforMonitor.start(Thread.currentThread());
        perforMonitorTl.set(perforMonitor);
    }

    private AndroidDriver newAndroidDriver(String appiumPort, DesiredCapabilities caps) {
        try {
            return new AndroidDriver(new URL("http://127.0.0.1:" + appiumPort + "/wd/hub"), caps);
        } catch (Exception e) {
            Log.e("AndroidDriver init fail.");
            stopServer(appiumPort);
            throw new IllegalStateException(e);
        }
    }

    private boolean isLargeThan4d4(String platformVersion) {
        platformVersion = platformVersion.replace(".", "");
        int v = Integer.valueOf(platformVersion);
        String v4d4s = "44";
        for (int i = 2; i < platformVersion.length(); i++) {
            v4d4s = v4d4s + "0";
        }
        int v4d4 = Integer.valueOf(v4d4s);
        return v >= v4d4;
    }

    protected void addCap(DesiredCapabilities caps) {
    }

    @AfterTest
    public void tearDown() throws Exception {
        if (!RestartTestHelper.isNextTestRestart()) return;
        perforMonitorTl.get().stop();
        androidDriverTl.get().quit();
    }

    @Parameters("port")
    @AfterSuite
    public void stopServer(String port) {
        AppiumServer.stop(port);
    }

    public static ThreadLocal<AndroidDriver> androidDriverTl = new ThreadLocal<>();
    public static ThreadLocal<PerforMonitor> perforMonitorTl = new ThreadLocal<>();
    protected AndroidDriver driver;

    @BeforeClass
    public void findPage() {
        driver = androidDriverTl.get();
        SleepUtil.s(2000);
        PageFactory.initElements(driver, this);
    }
}

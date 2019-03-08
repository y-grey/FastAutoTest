package yph.listener;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import io.appium.java_client.android.Activity;
import io.appium.java_client.android.connection.ConnectionState;
import yph.constant.Constant;

import static io.appium.java_client.remote.AndroidMobileCapabilityType.APP_ACTIVITY;
import static io.appium.java_client.remote.AndroidMobileCapabilityType.APP_PACKAGE;
import static yph.base.BaseTest.androidDriverTl;
import static yph.base.BaseTest.perforMonitorTl;

public class TestRetryListener implements IRetryAnalyzer {
	private int retryCount = 1;
	public static int maxRetryCount = 2;

	@Override
	public boolean retry(ITestResult result) {
		if(!isNormalCase(result))return false;
		if (retryCount <= maxRetryCount) {
			Reporter.setCurrentTestResult(result);
			Reporter.log("RunCount=" + (retryCount + 1));
			retryCount++;
			return true;
		}
		if(maxRetryCount - retryCount == -1) {
			retryCount = 1;
		}
		return false;
	}

	private boolean isNormalCase(ITestResult result){
		if(retryCount != 1 ) return true;
		String crashLog = perforMonitorTl.get().getCrashLog();
		if(!crashLog.equals("")){
			Reporter.log("应用崩溃(CRASH)："+crashLog);
			recoverScene(result);
			return false;
		}
		String anrLog = perforMonitorTl.get().getAnrLog();
		if(!anrLog.equals("")){
			Reporter.log("应用无响应(ANR)："+anrLog);
			return false;
		}
		ConnectionState connection = androidDriverTl.get().getConnection();
		if(!connection.isDataEnabled() && !connection.isWiFiEnabled()){
			Reporter.log("网络未打开(Net Close)");
			return false;
		}
		return true;
	}

	private void recoverScene(ITestResult result){
		String pkg = (String) androidDriverTl.get().getCapabilities().getCapability(APP_PACKAGE);
		String act = (String) androidDriverTl.get().getCapabilities().getCapability(APP_ACTIVITY);
		androidDriverTl.get().startActivity(new Activity(pkg,act));
		try {
			Class<?> clazz = Class.forName(result.getTestClass().getName());
			Method[] methods = clazz.getDeclaredMethods();
			if (methods != null && methods.length > 0) {
				for (Method method : methods) {
					Test ano = method.getAnnotation(Test.class);
					if (ano != null && method.getName().equals(Constant.ENTRY)) {
						method.invoke(clazz.newInstance());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package yph.listener;

import io.appium.java_client.android.Connection;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;

import static yph.base.BaseTest.androidDriverTl;
import static yph.base.BaseTest.perforMonitorTl;

public class TestRetryListener implements IRetryAnalyzer {
	public static int retryCount = 1;
	public static int maxRetryCount = 2;

	@Override
	public boolean retry(ITestResult result) {
		String crashLog = perforMonitorTl.get().getCrashLog();
		if(!crashLog.equals("")){
			Reporter.log(crashLog);
			return false;
		}
		String anrLog = perforMonitorTl.get().getAnrLog();
		if(!anrLog.equals("")){
			Reporter.log(anrLog);
			return false;
		}
		Connection connection = androidDriverTl.get().getConnection();
		if(connection == Connection.NONE){
			Reporter.log("网络未打开");
			return false;
		}
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
}

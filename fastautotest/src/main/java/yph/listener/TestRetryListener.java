package yph.listener;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;

public class TestRetryListener implements IRetryAnalyzer {
	public static int retryCount = 1;
	public static int maxRetryCount = 2;

	@Override
	public boolean retry(ITestResult result) {
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

package yph.listener;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import yph.base.BaseTest;

public class TestResultListener extends TestListenerAdapter {

    @Override
    public void onTestFailure(ITestResult tr) {
        saveScreenShot(tr);
        super.onTestFailure(tr);
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        super.onTestSkipped(tr);
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        super.onTestSuccess(tr);
    }

    @Override
    public void onTestStart(ITestResult tr) {
        super.onTestStart(tr);
    }

    @Override
    public void onFinish(ITestContext testContext) {
        super.onFinish(testContext);
        Iterator<ITestResult> skippedTests = testContext.getSkippedTests().getAllResults().iterator();
        while (skippedTests.hasNext()) {
            ITestResult skippedTest = skippedTests.next();
            if (testContext.getSkippedTests().getResults(skippedTest.getMethod()).size() > 1) {//去重
                skippedTests.remove();
            }else {
                String skipRunCount = "RunCount=1";
                List<String> list =  Reporter.getOutput(skippedTest);
                for(String s : list){
                    if(s.contains("RunCount=")){//获取skipcount
                        skipRunCount = s;
                    }
                }
                for (ITestResult passedTest : testContext.getPassedTests().getAllResults()) {//如果pass了
                    if(getId(skippedTest) == getId(passedTest)){//那就把skipcount赋值给passcount
                        Reporter.setCurrentTestResult(passedTest);
                        Reporter.log(skipRunCount);
                        skippedTests.remove();
                    }
                }
                for (ITestResult failTest : testContext.getFailedTests().getAllResults()) {//如果fail了
                    if(getId(skippedTest) == getId(failTest)){//那就把skipcount赋值给failcount
                        Reporter.setCurrentTestResult(failTest);
                        Reporter.log(skipRunCount);
                        skippedTests.remove();
                    }
                }
            }
        }
    }

    private int getId(ITestResult result) {
        int id = result.getTestClass().getName().hashCode();
        id = id + result.getMethod().getMethodName().hashCode();
        id = id + (result.getParameters() != null ? Arrays.hashCode(result.getParameters()) : 0);
        return id;
    }

    private void saveScreenShot(ITestResult tr) {
        String filePath = new StringBuilder("test-output/html/screenshot/")
                .append(tr.getTestContext().getSuite().getParameter("deviceName"))
                .append("/")
                .append(tr.getTestClass().getName().replace(".", "_"))
                .append("_")
                .append(tr.getName())
                .append(".png")
                .toString();
        File destFile = new File(filePath);
        try {
            File screenshot = BaseTest.mThreadLocal.get().getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, destFile);
        } catch (Exception e) {
            Reporter.log("截图失败："+e.toString());
            return;
        }
        if (destFile.exists()) {
            Reporter.setCurrentTestResult(tr);
            Reporter.log("<img class='pimg' src='" + filePath.replace("test-output/html/","") + "' hight='100' width='100'/>");
        }
    }
}

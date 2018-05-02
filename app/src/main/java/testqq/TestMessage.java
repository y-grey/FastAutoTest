package testqq;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import yph.annotations.Authod;
import yph.base.BaseTest;
import yph.listener.TestRetryListener;
import yph.utils.SleepUtil;


public class TestMessage extends BaseTest{
    @FindBys({@FindBy(className = "android.widget.TabWidget"), @FindBy(className = "android.widget.FrameLayout")})
    List<WebElement> list;

    @Override
    protected void addCap(DesiredCapabilities caps) {//假如你想添加参数，可重写此方法添加
    }

    @Test(description = "测试消息")
    public void test() {
        SleepUtil.s(2000);
        list.get(0).click();
    }

    @Authod("叶鹏辉")
    @Test(description = "测试消息1")
    public void test1() {
        if(TestRetryListener.retryCount < 2)
            Assert.assertTrue(false);
    }

}

package testqq;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import yph.annotation.FastAuto;
import yph.base.BaseTest;


public class TestMessage extends BaseTest{
    @FindBy(id = "com.tencent.mobileqq:id/conversation_head")
    WebElement element;
    @Override
    protected void addCap(DesiredCapabilities caps) {//假如你想添加参数，可重写此方法添加
    }

    @Test(description = "测试消息")
    public void test() {
        element.click();
        driver.pressKey(new KeyEvent(AndroidKey.BACK));
    }

    @FastAuto(author = "张三")
    @Test(description = "测试Assert")
    public void test1() {
        Assert.assertTrue(false);
    }

}

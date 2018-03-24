package testqq;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.testng.annotations.Test;

import java.util.List;

import yph.base.BaseTest;


public class TestMessage extends BaseTest {
    @FindBys({@FindBy(className = "android.widget.TabWidget"),@FindBy(className = "android.widget.FrameLayout")})
    List<WebElement> list;
    @Override
    protected void addCap(DesiredCapabilities caps){//假如你想添加参数，可重写此方法添加
    }

    @Test
    public void operation() {
        list.get(0).click();
    }

}

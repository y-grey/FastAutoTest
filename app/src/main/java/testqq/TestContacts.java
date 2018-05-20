package testqq;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.testng.annotations.Test;

import java.util.List;

import yph.base.BaseTest;


public class TestContacts extends BaseTest {
    @FindBys({@FindBy(className = "android.widget.TabWidget"), @FindBy(className = "android.widget.FrameLayout")})
    List<WebElement> list;
    @FindBy(id = "com.tencent.mobileqq:id/ivTitleBtnRightText")
    WebElement element;

    @Test
    public void entry() {
        list.get(0).click();
    }

    @Test
    public void test() {
        element.click();
    }
}

package testqq;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.annotations.Test;

import yph.base.BaseTest;


public class TestContacts extends BaseTest {
    @FindBy(id = "com.tencent.mobileqq:id/ivTitleBtnRightText")
    WebElement element;
    @Test
    public void test() {
        element.click();
    }
}

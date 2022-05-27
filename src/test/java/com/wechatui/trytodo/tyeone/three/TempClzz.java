package com.wechatui.trytodo.tyeone.three;

import com.wechatui.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @author law
 * @create 2022-05-2022/5/27 17:13
 */
public class TempClzz extends BasePage {
    public TempClzz(WebDriver driver) {
        super(driver);
    }
    public void search(){
        sendKeys(By.id("kw"),"hello");
        click(By.id("su"));
        sleep(2);
        click(By.linkText("百度首页"));
        sleep(5);
    }
}

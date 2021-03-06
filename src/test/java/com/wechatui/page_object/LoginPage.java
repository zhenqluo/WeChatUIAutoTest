package com.wechatui.page_object;

import com.wechatui.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @author law
 * @create 2022-05-2022/5/27 10:41
 */
public class LoginPage extends BasePage {
    private By changeLanLoc = By.xpath("//span[@class='ww_btn_Dropdown_arrow']");
    private By selectCNLoc = By.xpath("//li[@data-value='zh']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void switchChinese(){
        click(changeLanLoc);
        click(selectCNLoc);
    }

}

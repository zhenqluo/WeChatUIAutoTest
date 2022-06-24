package com.wechatui.pages;

import com.wechatui.base.BasePage;
import com.wechatui.base.UiMutual;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @author law
 * @create 2022-05-2022/5/27 10:41
 */
public class LoginPage extends BasePage {
    private By changeLanLoc = By.xpath("//span[@class='ww_btn_Dropdown_arrow']");
    private By selectCNLoc = By.xpath("//li[@data-value='zh']");

    public LoginPage(UiMutual uiMutual) {
        super(uiMutual);
    }

    public void switchChinese(){
        uiMutual.click(changeLanLoc);
        uiMutual.click(selectCNLoc);
    }

}

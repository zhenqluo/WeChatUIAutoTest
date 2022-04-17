package com.wechatui.page_object;

import com.wechatui.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @author luo
 * @create 2022/4/17 上午11:15
 */
public class MainPage extends BasePage {
    public MainPage(WebDriver driver) {
        super(driver);
    }

    private By addMemberLoc=By.xpath("//span[text()='添加成员']");

    public ContactsPage gotoAddMember(){
        click(addMemberLoc);
        return new ContactsPage(driver);
    }
}

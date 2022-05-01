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
    //页面URL
    private String URL = "https://work.weixin.qq.com/wework_admin/frame#index";
    //页面核心元素---添加成员页面定位符
    private By addMemberLoc=By.xpath("//span[text()='添加成员']");
    private By menuContactsLoc = By.id("menu_contacts");

    public ContactsPage gotoAddMember(){
        open(URL);  //（重新）请求BasePage地址，解决为不重新打开新的浏览器而连续执行两条测试用例的问题
        click(addMemberLoc);
        return new ContactsPage(driver);
    }
    public ContactsPage gotoContacts(){
        open(URL);
        click(menuContactsLoc);
        return new ContactsPage(driver);
    }
}












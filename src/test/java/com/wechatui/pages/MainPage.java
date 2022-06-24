package com.wechatui.pages;

import com.wechatui.base.BasePage;
import com.wechatui.base.UiMutual;
import com.wechatui.pages.member.ContactsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @author luo
 * @create 2022/4/17 上午11:15
 */
public class MainPage extends BasePage {
    public MainPage(UiMutual uiMutual) {
        super(uiMutual);
    }
    //页面URL
    private String URL = "https://work.weixin.qq.com/wework_admin/frame#index";
    //页面核心元素---添加成员页面定位符
    private By addMemberLoc=By.xpath("//span[text()='添加成员']");
    private By menuContactsLoc = By.id("menu_contacts");

    public ContactsPage gotoAddMember(){
        uiMutual.open(URL);  //（重新）请求BasePage地址，解决为不重新打开新的浏览器而连续执行两条测试用例的问题
        uiMutual.click(addMemberLoc);
        return new ContactsPage(uiMutual);
    }
    //从gotoContacts和gotoPartyManage其实都是跳转到同一个页面，但这里分为两个方法，是因为在设计用例时把这个页面分为两个部分：部门管理、成员管理
    public ContactsPage gotoContacts(){
        uiMutual.open(URL);
        uiMutual.click(menuContactsLoc);
        return new ContactsPage(uiMutual);
    }
    public PartyPage gotoPartyManage(){
        uiMutual.open(URL);
        uiMutual.click(menuContactsLoc);
        return new PartyPage(uiMutual);
    }
}












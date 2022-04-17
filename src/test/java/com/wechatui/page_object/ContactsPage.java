package com.wechatui.page_object;

import com.wechatui.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.lang.annotation.Retention;
import java.util.HashMap;

/**
 * @author luo
 * @create 2022/4/17 上午11:16
 */
public class ContactsPage extends BasePage {
    public ContactsPage(WebDriver driver) {
        super(driver);
    }

    //添加成员页面定位符
    private By userNameLoc = By.name("username");
    private By englishNameLoc = By.name("english_name");
    private By acctidLoc = By.name("acctid");
    private By genderLoc = By.xpath("//input[@name='gender' and @value='2']");
    private By bizMailLoc = By.name("biz_mail");
    private By wwTelLoc = By.xpath("//div[@class='ww_telInput']/div/div/input");
    private By mobileLoc = By.name("mobile");
    private By extTelLoc = By.name("ext_tel");
    private By xcxCorpAddressLoc = By.name("xcx_corp_address");
    private By aliasLoc = By.name("alias");
    private By positionLoc = By.name("position");
    private By sendInviteLoc = By.name("sendInvite");
    private By saveLoc = By.linkText("保存");

    public ContactsPage addMember(HashMap<String,Object> member){
        judgeToSendKeys(userNameLoc,member,"username");
        judgeToSendKeys(englishNameLoc,member,"english_name");
        judgeToSendKeys(acctidLoc,member,"acctid");
        click(genderLoc);
        clear(bizMailLoc);
        judgeToSendKeys(bizMailLoc,member,"biz_mail");
        clear(wwTelLoc);
        judgeToSendKeys(wwTelLoc,member,"ww_tel");
        judgeToSendKeys(mobileLoc,member,"mobile");
        judgeToSendKeys(extTelLoc,member,"ext_tel");
        judgeToSendKeys(xcxCorpAddressLoc,member,"xcx_corp_address");
        judgeToSendKeys(aliasLoc,member,"alias");
        judgeToSendKeys(positionLoc,member,"position");
        click(sendInviteLoc);
        click(saveLoc);
        return this;
    }




}

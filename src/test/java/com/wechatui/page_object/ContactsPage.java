package com.wechatui.page_object;

import com.wechatui.api.MemberManage;
import com.wechatui.base.BasePage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Retention;
import java.util.HashMap;
import java.util.List;

/**
 * @author luo
 * @create 2022/4/17 上午11:16
 */
public class ContactsPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(ContactsPage.class);
    public ContactsPage(WebDriver driver) {
        super(driver);
    }

    //页面URL
    private String URL = "https://work.weixin.qq.com/wework_admin/frame#contacts";
    //页面核心元素---添加成员页面定位符
    private By addButtonLoc = By.linkText("添加成员");
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
    //页面核心元素---删除成员页面定位符
    private By memberSearchInputLoc = By.id("memberSearchInput");
    private By delMemberLoc = By.className("js_del_member");
    private By submitLoc = By.linkText("确认");
    //页面核心元素---编辑成员信息
    private By editLoc = By.linkText("编辑");
    private By cancleLoc = By.linkText("取消");
    private By quitPageLoc = By.linkText("离开此页");
    //页面核心元素--文件导入
    private By bulkButtonLoc = By.xpath("//div[text()='批量导入/导出']");
    private By fileImportLoc = By.xpath("//div[@class='ww_operationBar']/div/div/ul/li/a[contains(text(),'文件导入')]");
    private By templateImportLoc = By.linkText("填写通讯录模板后导入");
    private By fileInputLoc = By.xpath("//input[@class='ww_fileImporter_fileContainer_uploadInputMask']");
    private By selectDepLoc = By.linkText("修改");
    private By deleteSelectedLoc = By.xpath("//span[contains(@class,'js_delete')]");
    //cssSelector常用定位方法:https://www.cnblogs.com/ixtao/p/13412020.html
    private By liTreeCloseLoc = By.cssSelector("div[id='partyTree'][style='display: block;'] li[class*='jstree-close']>i");   //jstree-closed是折叠的，找到所有的折叠的部门，展开部门树结构
    private By allDepLoc = By.cssSelector("div[id='partyTree'][style='display: block;'] a");  //css selector定位，用于找到所有部门，包括子部门 [父元素>子元素] 是找直接子元素、[父元素 子元素] 是找后代元素
    private By selectedSubmitLoc = By.linkText("确认");
    private By importButtonLoc = By.linkText("导入");


    public ContactsPage addMember(HashMap<String,Object> member){
        //open(URL);
        //sleep(1); 上一行打开URL后要强行等待一段时间，否则会有陈旧元素引用的问题，这里隐藏掉，在BasePage的open函数中统一添加，另外该方法的open(URL)也关闭，只保留mainpage的open(URL)
        logger.info("当前在添加成员页，开始添加成员{}",member.get("username"));
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
        sleep(1);
        logger.info("当前在添加成员页，添加成员{}操作结束",member.get("username"));
        return this;
    }
    public ContactsPage deleteMember(HashMap<String,Object> memberInfo){
        //页面删除流程
        logger.info("删除成员：{}",memberInfo.get("name"));
        sendKeys(memberSearchInputLoc,memberInfo.get("name").toString());
        click(delMemberLoc);
        click(submitLoc);

        return this;
    }
    /*
     ** accid:表示要修改的成员id，member表示的是要修改哪些信息
     */
    public ContactsPage updateMember(String acctid,HashMap<String,Object> member){
        //更新成员信息
        logger.info("更新成员信息：acctid[{}]",acctid);
        click(memberSearchInputLoc);
        sendKeys(memberSearchInputLoc,acctid);
        click(editLoc);
        judgeToSendKeys(userNameLoc,member,"username");
        judgeToSendKeys(englishNameLoc,member,"english_name");
        clear(wwTelLoc);
        judgeToSendKeys(wwTelLoc,member,"ww_tel");
        judgeToSendKeys(mobileLoc,member,"mobile");
        judgeToSendKeys(extTelLoc,member,"ext_tel");
        judgeToSendKeys(xcxCorpAddressLoc,member,"xcx_corp_address");
        judgeToSendKeys(aliasLoc,member,"alias");
        judgeToSendKeys(positionLoc,member,"position");
        click(saveLoc);
        sleep(0.2);
        return this;
    }
    public ContactsPage cancelUpdate(){
        click(cancleLoc);
        click(quitPageLoc);
        return this;
    }
    public ContactsPage importTemplate(String filePath){
        click(bulkButtonLoc);
        click(fileImportLoc);
        click(templateImportLoc);
        sendKeys(fileInputLoc,filePath);
        click(selectDepLoc);
        //先点击取消右边已选择的部门
        List<WebElement> delEles = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(deleteSelectedLoc));
        for (WebElement ele : delEles){
            try {
                ele.click();
            }catch (ElementNotInteractableException ex){  //上面找出的delEles包含两个元素，其中一个是不可交互的，不可交互的元素会抛出ElementNotInteractableException异常，这里进行处理
            }
        }
        //打开左边部门列表所有折叠的部门
        List<WebElement> closedDeps=wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(liTreeCloseLoc));
        for (WebElement ele : closedDeps){
            ele.click();
        }
        //找出所有部门，单击任意一个
        List<WebElement> deps = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(allDepLoc));
        int index = (int) (Math.random()*(deps.size()));
        WebElement ele = deps.get(index);
        ele.click();
        //点击选择部门页面的确认按钮
        List<WebElement> selectedSubmitEles = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(selectedSubmitLoc));
        for (WebElement elem : selectedSubmitEles){
            try {
                elem.click();
            }catch (ElementNotInteractableException| StaleElementReferenceException ex){   //通过异常捕获忽略不可交互元素
            }
        }
        click(importButtonLoc);
        return this;
    }




}

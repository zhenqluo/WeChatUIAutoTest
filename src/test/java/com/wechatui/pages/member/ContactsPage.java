package com.wechatui.pages.member;

import com.wechatui.base.BasePage;
import com.wechatui.base.UiMutual;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.HashMap;
import java.util.List;

/**
 * @author luo
 * @create 2022/4/17 上午11:16
 */
public class ContactsPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(ContactsPage.class);
    public ContactsPage(UiMutual uiMutual) {
        super(uiMutual);
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
        uiMutual.judgeToSendKeys(userNameLoc,member,"username");
        uiMutual.judgeToSendKeys(englishNameLoc,member,"english_name");
        uiMutual.judgeToSendKeys(acctidLoc,member,"acctid");
        uiMutual.click(genderLoc);
        uiMutual.clear(bizMailLoc);
        uiMutual.judgeToSendKeys(bizMailLoc,member,"biz_mail");
        uiMutual.clear(wwTelLoc);
        uiMutual.judgeToSendKeys(wwTelLoc,member,"ww_tel");
        uiMutual.judgeToSendKeys(mobileLoc,member,"mobile");
        uiMutual.judgeToSendKeys(extTelLoc,member,"ext_tel");
        uiMutual.judgeToSendKeys(xcxCorpAddressLoc,member,"xcx_corp_address");
        uiMutual.judgeToSendKeys(aliasLoc,member,"alias");
        uiMutual.judgeToSendKeys(positionLoc,member,"position");
        uiMutual.click(sendInviteLoc);
        uiMutual.click(saveLoc);
        uiMutual.sleep(1);
        logger.info("当前在添加成员页，添加成员{}操作结束",member.get("username"));
        return this;
    }
    public ContactsPage deleteMember(HashMap<String,Object> memberInfo){
        //页面删除流程
        logger.info("删除成员：{}",memberInfo.get("name"));
        uiMutual.sendKeys(memberSearchInputLoc,memberInfo.get("name").toString());
        uiMutual.click(delMemberLoc);
        uiMutual.click(submitLoc);

        return this;
    }
    /*
     ** accid:表示要修改的成员id，member表示的是要修改哪些信息
     */
    public ContactsPage updateMember(String acctid,HashMap<String,Object> member){
        //更新成员信息
        logger.info("更新成员信息：acctid[{}]",acctid);
        uiMutual.click(memberSearchInputLoc);
        uiMutual.sendKeys(memberSearchInputLoc,acctid);
        uiMutual.click(editLoc);
        uiMutual.judgeToSendKeys(userNameLoc,member,"username");
        uiMutual.judgeToSendKeys(englishNameLoc,member,"english_name");
        uiMutual.clear(wwTelLoc);
        uiMutual.judgeToSendKeys(wwTelLoc,member,"ww_tel");
        uiMutual.judgeToSendKeys(mobileLoc,member,"mobile");
        uiMutual.judgeToSendKeys(extTelLoc,member,"ext_tel");
        uiMutual.judgeToSendKeys(xcxCorpAddressLoc,member,"xcx_corp_address");
        uiMutual.judgeToSendKeys(aliasLoc,member,"alias");
        uiMutual.judgeToSendKeys(positionLoc,member,"position");
        uiMutual.click(saveLoc);
        uiMutual.sleep(0.2);
        return this;
    }
    public ContactsPage cancelUpdate(){
        uiMutual.click(cancleLoc);
        uiMutual.click(quitPageLoc);
        return this;
    }
    public ContactsPage importTemplate(String filePath){
        uiMutual.click(bulkButtonLoc);
        uiMutual.click(fileImportLoc);
        uiMutual.click(templateImportLoc);
        /*
        //click(templateImportLoc);
        //filePath = PathUtil.getRootPath(filePath);
        String relativePath = PathUtil.getTestDataFilePath(filePath);
        String absolutePath = PathUtil.getRootPath(relativePath);
        logger.info("批量导入成员测试，导入文件地址：{}",absolutePath);
        //sendKeys(fileInputLoc,absolutePath);
        sendKeys(fileInputLoc,absolutePath);
        */
        if (uiMutual.isRemoteExec()){ //判断是否为远程分布式执行
            uiMutual.remoteFileUpload(fileInputLoc,filePath);
        }else {
            uiMutual.sendKeys(fileInputLoc,filePath);
        }

        uiMutual.click(selectDepLoc);
        //先点击取消右边已选择的部门
        List<WebElement> delEles = uiMutual.getAllElements(deleteSelectedLoc);
        uiMutual.interactiveElementsClick(delEles);

        //打开左边部门列表所有折叠的部门
        List<WebElement> closedDeps = uiMutual.getAllElements(liTreeCloseLoc);
        uiMutual.interactiveElementsClick(closedDeps);

        //找出所有部门，单击任意一个
        List<WebElement> deps = uiMutual.getAllElements(allDepLoc);
        int index = (int) (Math.random()*(deps.size()));
        WebElement ele = deps.get(index);
        ele.click();
        logger.info("共有{}部门，随机点击第{}个",deps.size(),index+1);
        //点击选择部门页面的确认按钮
        List<WebElement> selectedSubmitEles = uiMutual.getAllElements(selectedSubmitLoc);
        uiMutual.interactiveElementsClick(selectedSubmitEles);

        uiMutual.click(importButtonLoc);
        return this;
    }

}

package com.wechatui.page_object;

import com.wechatui.base.BasePage;
import com.wechatui.utils.LogService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;

/**
 * @author luo
 * @create 2022/5/3 下午4:18
 */
public class PartyPage extends BasePage {
    private static final Logger logger = LogService.getInstance(PartyPage.class).getLogger();
    //页面核心元素
    private By addButtonLoc= By.xpath("//a[@class='member_colLeft_top_addBtnWrap js_create_dropdown']");
    private By createPartyLoc = By.linkText("添加部门");
    private By nameLoc = By.xpath("//input[@name='name']");
    private By ppnLoc = By.xpath("//span[@class='js_parent_party_name']");
    private By liTreeCloseLoc = By.cssSelector("div[id='__dialog__MNDialog__'] div[aria-activedescendant] li[class*='jstree-closed']>i");
    private By allPartyLoc = By.cssSelector("div[id='__dialog__MNDialog__'] div[aria-activedescendant] a");
    private By sumitButLoc = By.linkText("确定");

    public PartyPage(WebDriver driver){
        super(driver);
    }


    public PartyPage addParty(HashMap<String,Object> partyData){
        logger.info("开始添加部门，父部门{}下添加子部门{}",partyData.get("ppn").toString(),partyData.get("name").toString());
        click(addButtonLoc);
        click(createPartyLoc);
        sendKeys(nameLoc,partyData.get("name").toString());
        click(ppnLoc);
        //找到所有折叠的部门并点击展开
        //todo:封装wait.util()函数，捕获可能会抛出的TimeoutException异常
        List<WebElement> treeCloseds = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(liTreeCloseLoc));
        for (WebElement ele : treeCloseds){
            ele.click();
        }
        //获取所有的部门WebElement，获取innerHTML进行与yaml中的ppn比较，找出父部门，如果没找到默认添加到第一个部门
        List<WebElement> allPartys = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(allPartyLoc));
        String parentName = partyData.get("ppn").toString();
        int index = 0;
        for (int i = 0; i < allPartys.size(); i++) {
            WebElement ele = allPartys.get(i);
            if (ele.getAttribute("innerHTML").contains(parentName)){  //获取innerHTML使用getAttribute函数
                index = i;
                logger.info("匹配到父部门index为{}",i);
                break;
            }
        }

        allPartys.get(index).click();
        click(sumitButLoc);

        return this;
    }


}

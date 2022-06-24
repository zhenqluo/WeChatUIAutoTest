package com.wechatui.pages;

import com.wechatui.base.BasePage;
import com.wechatui.base.UiMutual;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

/**
 * @author luo
 * @create 2022/5/3 下午4:18
 */
public class PartyPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(PartyPage.class);

    public PartyPage(UiMutual uiMutual){
        super(uiMutual);
    }

    //页面核心元素--添加部门
    private By addButtonLoc= By.xpath("//a[@class='member_colLeft_top_addBtnWrap js_create_dropdown']");
    private By createPartyLoc = By.linkText("添加部门");
    private By nameLoc = By.xpath("//input[@name='name']");
    private By ppnLoc = By.xpath("//span[@class='js_parent_party_name']");
    private By liTreeCloseLoc = By.cssSelector("div[id='__dialog__MNDialog__'] div[aria-activedescendant] li[class*='jstree-closed']>i");
    private By allPartyLoc = By.cssSelector("div[id='__dialog__MNDialog__'] div[aria-activedescendant] a");
    private By sumitButLoc = By.linkText("确定");
    //页面核心元素--删除部门
    private By deLiTreeCloseLoc = By.cssSelector("div[aria-activedescendant] li[class*='jstree-closed']>i");
    private By deAllPartyLoc = By.cssSelector("div[aria-activedescendant] a");
    private By opPartyLoc = By.tagName("span");
    private By delPartyLoc = By.xpath("//ul/li/a[text()='删除']");
    private By delSumitLoc = By.linkText("确定");
    private By delCancelLoc = By.linkText("确定"); //删除带子部门的部门时出现的弹出的确定按钮（取消删除）
    //页面核心元素--修改部门名称
    private By updateLoc = By.linkText("修改名称");
    private By updateNameLoc = By.xpath("//input[@name='name']");
    private By updateSubmitLoc = By.linkText("保存");







    public PartyPage addParty(HashMap<String,Object> partyData){
        logger.info("开始添加部门，父部门{}下添加子部门{}",partyData.get("ppn").toString(),partyData.get("name").toString());
        uiMutual.refresh();//因为打开页面后准备测试用例数据时调用了接口改变了后台数据，使用需要重新刷新页面
        uiMutual.click(addButtonLoc);
        uiMutual.click(createPartyLoc);
        uiMutual.sendKeys(nameLoc,partyData.get("name").toString());
        uiMutual.click(ppnLoc);
        //找到所有折叠的部门并点击展开
        //todo:封装wait.util()函数，捕获可能会抛出的TimeoutException异常
        List<WebElement> treeCloseds = uiMutual.getAllElements(liTreeCloseLoc);
        uiMutual.interactiveElementsClick(treeCloseds);

        //获取所有的部门WebElement，获取innerHTML进行与yaml中的ppn比较，找出父部门，如果没找到默认添加到第一个部门
        List<WebElement> allPartys = uiMutual.getAllElements(allPartyLoc);//wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(allPartyLoc));
        String parentName = partyData.get("ppn").toString();
        int index = 0;
        for (int i = 0; i < allPartys.size(); i++) {
            WebElement ele = allPartys.get(i);
            if (uiMutual.getElemAttributeValue(ele,"innerHTML").contains(parentName)){  //获取innerHTML使用getAttribute函数
                index = i;
                logger.info("匹配到父部门index为{}",i);
                break;
            }
        }

        uiMutual.elementClick(allPartys.get(index));
        uiMutual.click(sumitButLoc);

        return this;
    }
    //
    public PartyPage deleteParty(String partyName){
        openPartyOpMenu(partyName);
        uiMutual.click(delPartyLoc);

        return this;
    }
    //deletePartySubmit()和deleteParty()的区别就是多了一个click(delSumitLoc)操作，之所以这样分是因为不同测试用例的断言有区别，具体参考PartyPageTest类的调用这两个方法的case
    public PartyPage deletePartySubmit(String partyName){
        deleteParty(partyName);
        uiMutual.click(delSumitLoc);
        return this;
    }
    public PartyPage updateParty(String newName,String partyName){
        openPartyOpMenu(partyName);
        uiMutual.click(updateLoc);
        uiMutual.sendKeys(updateNameLoc,newName);
        uiMutual.click(updateSubmitLoc);
        return this;
    }
    //公共操作，打开实参partyName指定的部门在部门树中操作菜单
    private void openPartyOpMenu(String partyName){
        uiMutual.refresh(); //打开微信企业页面-->调用接口创建部门/成员-->需再次刷新页面才能加载出接口新创建的部门和成员
        //找到所有折叠的部门并点击展开
        //TODO：当前只能展开二级部门，三重以上部门无法展开，所以无法对三级以上部门进行删除操作
        /*
        //这种方式容易抛出StaleElementReferenceException异常,采用另外一种写法，参考：http://t.zoukankan.com/z-x-y-p-9805272.html 但该例子中采用这种方式可能不行，因为存在多级部门的情况
        List<WebElement> treeCloseds = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(deLiTreeCloseLoc));
        int ind=0;
        for (WebElement ele : treeCloseds){
            //ele.click();
            System.out.println(ind++);
            wait.until(ExpectedConditions.elementToBeClickable(ele)).click();
        }*/
        /* --这种方式可以展开所有部门层级，也可解决StaleElementReferenceException异常，但每次都要等待抛出NoSuchElement异常，如何处理这个等待时间？
        WebDriverWait wait = new WebDriverWait(this.driver,1);
        int pos = 0;
        try {
            while (wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(deLiTreeCloseLoc)).size()>0){
                System.out.println(pos++);
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(deLiTreeCloseLoc)).get(0).click();
            }
        }catch (Exception ex){

        }*/

        //    -- 利用异常捕获的方式处理StaleElementReferenceException
        try{
            List<WebElement> treeCloseds = uiMutual.getAllElements(deLiTreeCloseLoc);
            int ind=0;
            for (WebElement ele : treeCloseds){
                //ele.click();
                System.out.println(ind++);
                uiMutual.waitElementToBeClickableThenClick(ele);
            }
        }catch (StaleElementReferenceException ex){ //捕获StaleElementReferenceException异常并处理（重复执行）
            List<WebElement> treeCloseds = uiMutual.getAllElements(deLiTreeCloseLoc);
            int ind=0;
            for (WebElement ele : treeCloseds){
                //ele.click();
                System.out.println(ind++);
                uiMutual.waitElementToBeClickableThenClick(ele);
            }
        }

        //获取所有部门WebElements，获取innerHTML与实参partyName比较，找出需要删除的部门
        List<WebElement> allPartys = uiMutual.getAllElements(deAllPartyLoc);
        int index = 0;
        boolean flag = false;
        for (int i = allPartys.size()-1; i>=0; i--) {  //优化为倒序查找，因为测试用例数据准备时添加的部门都是id递增的，排在部门树的最后
            WebElement ele = allPartys.get(i);
            if (uiMutual.getElemAttributeValue(ele,"innerHTML").contains(partyName)){  //获取innerHTML使用getAttribute函数
                index = i;
                flag = true;
                logger.info("需要操作的部门是{}，匹配到部门index为{}",partyName,i+1);
                break;
            }
        }
        //todo:flag=false的情况

        //Actions actions = new Actions(driver);
        //actions.moveToElement(allPartys.get(index)).perform();
        //为了让后面的三个点显示出来，上面使用了把鼠标移入悬浮的方式，这种方式在实际运行过程中非常容易失败，会造成下面的opMenu.click()时提示selenium.ElementNotInteractableException: element not interactable
        //allPartys.get(index).click(); //通过直接点击的方式让后面的三个点显示出来
        //上面直接调用元素的click()还是会出现点击不到的情况，再改为下面的方式试下
        ///wait.until(ExpectedConditions.elementToBeClickable(allPartys.get(index))).click();
        uiMutual.waitElementToBeClickableThenClick(allPartys.get(index));
        //通过父元素找到子元素span,span对应的是部门后面的三个点，该元素包含在a标签中
        ///WebElement opMenu = allPartys.get(index).findElement(opPartyLoc);
        WebElement opMenu = uiMutual.getElementByParent(allPartys.get(index),opPartyLoc);
        logger.info(opMenu.toString());

        ///opMenu.click();
        uiMutual.elementClick(opMenu);
        //wait.until(ExpectedConditions.elementToBeClickable(allPartys.get(index).findElement(opPartyLoc))).click();
    }


}

package com.wechatui.base;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author law
 * @create 2022-06-2022/6/22 9:23
 */
public interface UiMutual {
    public  void initDriver();
    public WebDriver getDriver();
    public WebDriverWait getDriverWait();
    public boolean isRemoteExec();
    public void open(String URL);
    public Set<Cookie> getCookies();
    public void addCookie(Cookie cookie);
    public void closeBrowser();
    public void refresh();
    public WebElement getElement(String locMode, String locExpression);
    public WebElement getElementByParent(WebElement pElement,By loc);
    public boolean isElemDisappear(String locMode,String locExpression);
    public void sendKeys(By loc, String words);
    public void click(By loc);
    public void interactiveElementsClick(List<WebElement> elements);
    public void elementClick(WebElement ele);
    public void waitElementToBeClickableThenClick(WebElement ele);
    public void judgeToSendKeys(By loc, HashMap<String,Object> map, String key);
    public void clear(By loc);
    public void sleep(double lt);
    public List<WebElement> getAllElements(By loc);
    public void remoteFileUpload(By fileInputLoc,String filePath);
    public String getElemAttributeValue(WebElement ele,String attrName);
}

package com.wechatui.common;

import com.wechatui.base.UiMutual;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author law
 * @create 2022-06-2022/6/16 21:58
 */
public class SeleniumHelper implements UiMutual {
    private final  Logger logger = LoggerFactory.getLogger(SeleniumHelper.class);

    WebDriver driver = null;
    WebDriverWait driverWait = null;

    public  void initDriver(){

        String browserName = System.getProperty("browser");
        if (!("chrome".equalsIgnoreCase(browserName) || "firefox".equalsIgnoreCase(browserName) ||  "ie".equalsIgnoreCase(browserName))){
            browserName = "chrome";
        }

        logger.info("browser={}",browserName);

        if ("chrome".equals(browserName)){
            DesiredCapabilities cap = DesiredCapabilities.chrome();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-infobars");  // 禁止策略化
            options.addArguments("--no-sandbox"); // 解决DevToolsActivePort文件不存在的报错
            cap.setAcceptInsecureCerts(true);
            cap.setJavascriptEnabled(true);
            cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS,true);
            cap.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS,true);
            cap.setCapability(ChromeOptions.CAPABILITY,options);
            try {
                driver = new RemoteWebDriver(new URL("http://192.168.162.130:4444/wd/hub"), cap);
            }catch (MalformedURLException ex){
                logger.error(ex.toString(),ex);
            }
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            driverWait = new WebDriverWait(driver,5);

        }else if ("firefox".equals(browserName)){

        }else if ("ie".equals(browserName)){

        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    public WebDriverWait getDriverWait() {
        return driverWait;
    }

    public boolean isRemoteExec(){
        return this.driver != null && this.driver.getClass() == RemoteWebDriver.class;
    }

    public void open(String URL){
        logger.info("打开网站：{}",URL);
        driver.get(URL);
        //sleep(0.5);
    }
    public Set<Cookie> getCookies(){
        return driver.manage().getCookies();
    }
    public void addCookie(Cookie cookie){
        driver.manage().addCookie(cookie);
    }
    public void closeBrowser(){
        driver.quit();
    }
    public void refresh(){
        driver.navigate().refresh();
        //sleep(0.1);//解决StaleElementReferenceException的第一种方法：引入强制等待，这种方式不好把控时间，不考虑
        /*  --解决StaleElementReferenceException的第二中方法：原本想着是刷新页面引起的元素过时异常，那刷新后等待页面加载完成再进行下一步操作是否可性？经验证，结论是不可行
        JavascriptExecutor jsExec = (JavascriptExecutor) driver;
        while (true){
            boolean loaded = (Boolean) jsExec.executeScript("return document.readyState == \"complete\"");
            if (loaded) break;
        }
         */
    }
    //返回定位方式，yaml文件中通过字符串指定，这里把字符串转换为真正的By
    public By getLocType(String locMode, String locExpression){
        By locType = null;
        if (locMode.equalsIgnoreCase("by.id")){
            locType = By.id(locExpression);
        }
        if (locMode.equalsIgnoreCase("by.xpath")){
            locType = By.xpath(locExpression);
        }
        if (locMode.equalsIgnoreCase("by.name")){
            locType = By.name(locExpression);
        }
        if (locMode.equalsIgnoreCase("by.linkText")){
            locType = By.linkText(locExpression);
        }
        return locType;
    }
    //传入定位字符串返回一个元素
    public WebElement getElement(String locMode, String locExpression){
        WebElement ele=null;
        try {
            ele = driverWait.until(ExpectedConditions.presenceOfElementLocated(getLocType(locMode,locExpression)));
        }catch (NoSuchElementException | TimeoutException ex){  //如果元素没定位到会抛出异常，这里进行异常捕获
            //System.out.println("没定位到元素，请核实元素定位方式");
            //System.out.println(ex.getMessage());
            logger.error("没定位到元素{}:{}",locMode,locExpression);
            logger.error(ex.toString(),ex);
        }
        return ele;
    }

    public List<WebElement> getAllElements(By loc){
        return driverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(loc));
    }
    public WebElement getElementByParent(WebElement pElement,By loc){
     return pElement.findElement(loc);
    }

    public boolean isElemDisappear(String locMode,String locExpression){
        boolean isExist = true;
        try {
            isExist= driverWait.until(ExpectedConditions.invisibilityOfElementLocated(getLocType(locMode,locExpression)));
        }catch (TimeoutException ex){ //注意invisibilityOfElementLocated条件如果元素不存在返回true，但如果元素存在，不是返回false，而是抛出异常，所以需要对异常进行捕获并返回false
            logger.info("元素[{}:{}]仍在页面存在",locMode,locExpression);
            isExist = false;
        }
        return isExist;
    }
    public void sendKeys(By loc,String words){
        clear(loc); //进行文本输入前无论啥清空先清空
        logger.info("正在定位元素[{}]，输入文本{}",loc.toString(),words);
        try {
            driverWait.until(ExpectedConditions.visibilityOfElementLocated(loc)).sendKeys(words);
        }catch (Exception ex){
            logger.error("定位元素[{}]，输入文本{}失败",loc.toString(),words);
            throw new RuntimeException(ex.getMessage());
        }
        //sleep(0.5);  //为方便调试临时添加
    }
    public void remoteFileUpload(By fileInputLoc,String filePath){
        //参考：如何最好地处理Selenium Webdriver中的文件上传失败：https://www.codenong.com/48196109/
        //参考：https://cloud.tencent.com/developer/ask/sof/411388/answer/664959
        LocalFileDetector detector = new LocalFileDetector();
        File localFile = detector.getLocalFile(filePath);
        RemoteWebElement upload = (RemoteWebElement) driver.findElement(fileInputLoc);
        upload.setFileDetector(detector);
        String absolutePath = localFile.getAbsolutePath();
        upload.sendKeys(absolutePath);//getAbsolutePath()获取绝对路径，本测试用例因为传入的就是绝对路径，所以可以直接传入形参filePath
    }
    public void click(By loc){
        logger.info("正在定位元素[{}]进行点击操作",loc.toString());
        try {
            driverWait.until(ExpectedConditions.elementToBeClickable(loc)).click();
        }catch (Exception ex){
            logger.error("定位元素[{}]进行点击操作失败",loc.toString());
            //从业务流程上讲有一个元素定位失败，则整个流程就可以终止了，所以下面抛出另一个异常终止流程执行，
            // 如果不抛出，因为所有操作都捕抓了异常，导致下一个流程步骤可继续执行，假如每一个流程都是元素定位，导致不断的等待超时抛出异常，最后需超长等待才能发现流程问题
            throw new RuntimeException(ex.getMessage());
        }
        //sleep(2);//为方便观察调试临时添加
    }
    public void interactiveElementsClick(List<WebElement> elements){ //点击可交互元素，不可交互元素捕获异常并忽略
        for (WebElement ele : elements){
            try {
                ele.click();   //元素列表中可能存在不可交互的元素，不可交互的元素会抛出ElementNotInteractableException异常，下面进行捕获并忽略异常
            }catch (ElementNotInteractableException| StaleElementReferenceException ex){   //通过异常捕获忽略不可交互元素
            }
        }
    }
    public void elementClick(WebElement ele){
        ele.click();
    }
    public void waitElementToBeClickableThenClick(WebElement ele){
        driverWait.until(ExpectedConditions.elementToBeClickable(ele)).click();
    }
    public void judgeToSendKeys(By loc, HashMap<String,Object> map, String key){
        if (map.get(key) != null){//map.get(key) != null包含了没key和有key但没value的情况
            sendKeys(loc,map.get(key).toString());
        }
    }
    public void clear(By loc){
        logger.info("正在定位元素[{}]进行clear操作",loc.toString());
        try {
            driverWait.until(ExpectedConditions.presenceOfElementLocated(loc)).clear();
        }catch (Exception ex){
            logger.error("定位元素[{}]进行clear操作失败",loc.toString());
            throw new RuntimeException(ex.getMessage());
        }
    }
    public void sleep(double lt){
        try {
            Thread.sleep((long)lt*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public String getElemAttributeValue(WebElement ele,String attrName){
        return ele.getAttribute(attrName);
    }


}

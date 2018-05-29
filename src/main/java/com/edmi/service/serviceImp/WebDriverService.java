package com.edmi.service.serviceImp;

import com.edmi.dao.linkedin.Linkedin_linkRepository;
import com.edmi.service.service.LinkedinService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by viking on 2016/4/28.
 */
@Service
public class WebDriverService {

    Logger log = Logger.getLogger(WebDriverService.class);

    @Autowired
    private Linkedin_linkRepository linkDao;
    @Autowired
    private LinkedinService linkService;

    Map<Integer,RemoteWebDriver> drivers = new HashMap<Integer, RemoteWebDriver>();

    public void login_linkedin(int id,String accountName, String accountPwd){
        System.setProperty("webdriver.gecko.driver", "C:\\Program Files\\Mozilla Firefox\\geckodriver.exe");
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        RemoteWebDriver driver = new FirefoxDriver(firefoxProfile);
        driver.get("http://www.linkedin.com/uas/login?session_redirect=&goback=&trk=hb_signin");

        WebElement userName = driver.findElement(By.id("session_key-login"));
        userName.clear();
        userName.sendKeys(accountName);

        // 密码
        WebElement password = driver.findElement(By.id("session_password-login"));
        password.clear();
        password.sendKeys(accountPwd);

        WebElement btnLogin = driver.findElement(By.id("btn-primary"));
        btnLogin.click();

        try {
            Thread.sleep(5*1000);
            drivers.put(id,driver);
        } catch (InterruptedException e) {
            log.info(e.getMessage());
        }
    }
/*    public void login_linkedin(){
        this.login_linkedin(9999,"q8@bigdatademo.cn","Ab1234");
        RemoteWebDriver driver  = this.getDriver(9999);
        List<Linkedin_link> links = linkDao.getLinks();

        for(Linkedin_link link_obj:links){
            String link = link_obj.getLink();
            try{
                Document doc = this.getLinked_member_info(driver,link);
                linkService.analysisMembersToBase(doc,link);
                Thread.sleep(5*1000);
            }catch (Exception e){
                log.info(e.getMessage());
                continue;
            }

        }
    }*/
    public Document getLinked_member_info(RemoteWebDriver driver,String link){
        this.scrollRightBarToBottomSlowly(driver,link);
        Document doc = Jsoup.parse(driver.getPageSource().toString());
        return doc;
    }
    public  void scrollRightBarToBottomSlowly(RemoteWebDriver driver,String link) {
        driver.manage().window().maximize();
        if(!StringUtils.startsWith(link,"http://")){
            link = "http://"+StringUtils.trim(link);
        }
        driver.get(link);
        int winH = driver.manage().window().getSize().getHeight();
        WebElement el = driver.findElementByTagName("body");
        for (int i = -1; i <= el.getSize().getHeight(); i += winH / 2) {
            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.executeScript("window.scrollTo(0," + i + ")");
            log.info(el.getSize().getHeight() + ":" + i);
            if (i / winH >= 100) {//如果当前的页面长度大于我们的屏幕分辨率的100倍就滚动到页面底部，不再往下截取内容。
                int curHeight = el.getSize().getHeight();
                for (; i < curHeight; i += winH / 2) {
                    try {
                        Thread.sleep(2 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    driver.executeScript("window.scrollTo(0," + i + ")");
                }
                try {
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                driver.executeScript("window.scrollTo(0," + (i - curHeight) + ")");
                break;
            }

        }

        try {
            Thread.sleep(15 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void navigate(FirefoxDriver driver,String url){
        driver.navigate().to(url);
    }

    public void get(FirefoxDriver driver,String url){
        driver.get(url);
    }


    public RemoteWebDriver getDriver(int id){
         return drivers.get(id);
    }

    public Map<Integer, RemoteWebDriver> getDrivers() {
        return drivers;
    }

    public void setDrivers(Map<Integer, RemoteWebDriver> drivers) {
        this.drivers = drivers;
    }

    public void scrollTo(WebDriver driver,String location){
        JavascriptExecutor driver_js = (JavascriptExecutor) driver;
        String currentH = driver.getWindowHandle();
        driver.switchTo().window(currentH).manage().window().maximize();
        if(StringUtils.equalsIgnoreCase(location,"bottom")){
            //将页面滚动条拖到底部
            driver_js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
        }else if(StringUtils.equalsIgnoreCase(location,"top")){
            //#将页面滚动条拖到顶部
            driver_js.executeScript("window.scrollTo(0,0)");
        }
    }
    public void openWindows(FirefoxDriver driver,String href ){
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        log.info("正在打开新的窗口href："+href);
        executor.executeScript("window.open('" + href + "')");
        log.info("新窗口href："+href+"已经打开！");
    }
    public static boolean switchToWindow(WebDriver dr){

        boolean flag = false;
        String  handle = dr.getWindowHandle();
        dr.switchTo().window(handle).manage().window().maximize();
        /*try {
            Set<String> handles = dr.getWindowHandles();
            for (String s : handles) {
                    dr.switchTo().window(s).manage().window().maximize();
                    if (dr.getTitle().contains(windowTitle)) {
                        flag = true;
                        break;
                    } else {
                        continue;
                    }
            }
        } catch (Exception e) {
            flag = false;
        }*/
        return flag;
    }
    public static void switchToWindowClose(String windowTitle,WebDriver dr){
        try {
            Set<String> handles = dr.getWindowHandles();
            for (String s : handles) {

                if (dr.getTitle().contains(windowTitle)) {
                    dr.switchTo().window(s).close();
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

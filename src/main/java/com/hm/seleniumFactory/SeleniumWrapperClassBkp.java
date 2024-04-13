package com.hm.seleniumFactory;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.hm.enums.WaitStrategy;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumWrapperClassBkp {
  private static WebDriver driver;
  
  static Properties properties;
  
  private static ThreadLocal<WebDriver> localDriver = new ThreadLocal<>();
  
  private static ThreadLocal<JavascriptExecutor> executor = new ThreadLocal<>();
  
  private static ThreadLocal<TakesScreenshot> screeShot = new ThreadLocal<>();
  
  private static ThreadLocal<Actions> actions = new ThreadLocal<>();
  
  public static WebDriver getlocalDriver() {
    return localDriver.get();
  }
  
  public static JavascriptExecutor getexecutor() {
    return executor.get();
  }
  
  public static void openWebsite(String url, String pageName) {
    getlocalDriver().get(url);
    getlocalDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20L));
    (new WebDriverWait(getlocalDriver(), Duration.ofSeconds(15L))).until(webDriver -> Boolean.valueOf(((JavascriptExecutor)webDriver).executeScript("return document.readyState", new Object[0]).equals("complete")));
    ((ExtentTest)BaseTest.logger.get()).info("SuccesFully Page >>" + pageName + "<< is opened ", 
        MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
  }
  
  public static WebElement webElementManger(Object locator, WaitStrategy Strategy) {
    WebElement element = null;
    if (locator instanceof WebElement) {
      element = waitForElement((WebElement)locator, Strategy);
    } else if (locator instanceof By) {
      element = waitForElement((By)locator, Strategy);
    } else {
      wait(6000);
      element = getLocatorJs((String)locator);
    } 
    return element;
  }
  
  public static WebElement webElementManger(Object locator, WaitStrategy Strategy, int timeOut) {
    WebElement element = null;
    if (locator instanceof WebElement) {
      element = waitForElement((WebElement)locator, Strategy);
    } else if (locator instanceof By) {
      element = waitForElement((By)locator, Strategy);
    } else {
      element = waitForElement((String)locator, Strategy, timeOut);
    } 
    return element;
  }
  
  public static WebElement waitForElement(By locator, WaitStrategy Strategy, int timeOut) {
    WebElement result = null;
    switch (Strategy) {
      case CLICKABLE:
        result = (WebElement)(new WebDriverWait(localDriver.get(), Duration.ofSeconds(timeOut))).until((Function)ExpectedConditions.elementToBeClickable(locator));
        break;
      case VISIBLE:
        result = (WebElement)(new WebDriverWait(localDriver.get(), Duration.ofSeconds(timeOut))).until((Function)ExpectedConditions.visibilityOfElementLocated(locator));
        break;
      case PRESENCE:
        result = (WebElement)(new WebDriverWait(localDriver.get(), Duration.ofSeconds(timeOut))).until((Function)ExpectedConditions.presenceOfElementLocated(locator));
        break;
    } 
    return result;
  }
  
  public static WebElement waitForElement(By locator, WaitStrategy Strategy) {
    WebElement result = null;
    switch (Strategy) {
      case CLICKABLE:
        result = (WebElement)(new WebDriverWait(localDriver.get(), Duration.ofSeconds(15L))).until((Function)ExpectedConditions.elementToBeClickable(locator));
        break;
      case VISIBLE:
        result = (WebElement)(new WebDriverWait(localDriver.get(), Duration.ofSeconds(15L))).until((Function)ExpectedConditions.visibilityOfElementLocated(locator));
        break;
      case PRESENCE:
        result = (WebElement)(new WebDriverWait(localDriver.get(), Duration.ofSeconds(15L))).until((Function)ExpectedConditions.presenceOfElementLocated(locator));
        break;
    } 
    return result;
  }
  
  public static WebElement waitForElement(WebElement locator, WaitStrategy Strategy, int timeOut) {
    WebElement result = null;
    switch (Strategy) {
      case CLICKABLE:
        result = (WebElement)(new WebDriverWait(localDriver.get(), Duration.ofSeconds(timeOut))).until((Function)ExpectedConditions.elementToBeClickable(locator));
        break;
      case VISIBLE:
        result = (WebElement)(new WebDriverWait(localDriver.get(), Duration.ofSeconds(timeOut))).until((Function)ExpectedConditions.visibilityOf(locator));
        break;
    } 
    return result;
  }
  
  public static WebElement waitForElement(String locator, WaitStrategy Strategy, int timeOut) {
    WebElement result = null;
    switch (Strategy) {
      case CLICKABLE:
        result = (WebElement)(new WebDriverWait(localDriver.get(), Duration.ofSeconds(timeOut))).until((Function)ExpectedConditions.elementToBeClickable(getWebElement(locator)));
        return result;
      case VISIBLE:
        result = (WebElement)(new WebDriverWait(localDriver.get(), Duration.ofSeconds(timeOut))).until((Function)ExpectedConditions.visibilityOf(getWebElement(locator)));
        return result;
    } 
    result = getLocatorJs(locator);
    return result;
  }
  
  public static WebElement waitForElement(WebElement locator, WaitStrategy Strategy) {
    WebElement result = null;
    switch (Strategy) {
      case CLICKABLE:
        result = (WebElement)(new WebDriverWait(localDriver.get(), Duration.ofSeconds(Integer.parseInt(getProperties("smartTimeOut"))))).until((Function)ExpectedConditions.elementToBeClickable(locator));
        break;
      case VISIBLE:
        result = (WebElement)(new WebDriverWait(localDriver.get(), Duration.ofSeconds(Integer.parseInt(getProperties("smartTimeOut"))))).until((Function)ExpectedConditions.visibilityOf(locator));
        break;
    } 
    return result;
  }
  
  public static void click(Object locator, String locatorName) {
    try {
      WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);
      highLightWebElementInScreenSchot(element, locatorName);
      ((ExtentTest)BaseTest.logger.get()).info("Clicking onElement -->" + locatorName, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      element.click();
      unHighLightWebElementInScreenSchot(element);
    } catch (Exception e) {
      WebElement element = webElementManger(locator, WaitStrategy.PRESENCE);
      scrollToElement(locator, locatorName);
      highLightWebElementInScreenSchot(element, locatorName);
      ((ExtentTest)BaseTest.logger.get()).info("Clicking onElement -->" + locatorName, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      element.click();
      unHighLightWebElementInScreenSchot(element);
    } 
  }
  
  public static void click(Object locator) {
    try {
      WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);
      ((ExtentTest)BaseTest.logger.get()).info("Clicking onElement -->" + locator, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      highLightWebElementInScreenSchot(element);
      element.click();
      unHighLightWebElementInScreenSchot(element);
    } catch (Exception e) {
      WebElement element = webElementManger(locator, WaitStrategy.PRESENCE);
      scrollToElement(locator);
      highLightWebElementInScreenSchot(element);
      ((ExtentTest)BaseTest.logger.get()).info("Clicking onElement -->" + locator, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      element.click();
      unHighLightWebElementInScreenSchot(element);
    } 
  }
  
  public static void forceClick(Object locator) {
    try {
      WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);
      ((ExtentTest)BaseTest.logger.get()).info("Clicking onElement -->" + locator, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      highLightWebElementInScreenSchot(element);
      ((JavascriptExecutor)executor.get()).executeScript("arguments[0].click();", new Object[] { element });
    } catch (Exception e) {
      WebElement element = webElementManger(locator, WaitStrategy.PRESENCE);
      ((ExtentTest)BaseTest.logger.get()).info("SecondTry-Clicking onElement -->" + locator, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      scrollToElement(locator);
      highLightWebElementInScreenSchot(element);
      element.click();
      unHighLightWebElementInScreenSchot(element);
    } 
  }
  
  public static void forceClick(Object locator, String locatorName) {
    try {
      WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);
      ((ExtentTest)BaseTest.logger.get()).info("Clicking onElement -->" + locatorName, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      highLightWebElementInScreenSchot(element);
      ((JavascriptExecutor)executor.get()).executeScript("arguments[0].click();", new Object[] { element });
    } catch (Exception e) {
      WebElement element = webElementManger(locator, WaitStrategy.PRESENCE);
      ((ExtentTest)BaseTest.logger.get()).info("SecondTry-Clicking onElement -->" + locatorName, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      scrollToElement(locator);
      highLightWebElementInScreenSchot(element);
      element.click();
      unHighLightWebElementInScreenSchot(element);
    } 
  }
  
  public static void uploadFile(String locator, String fileName) {
    try {
      String elementPath = locator + "//input[@type='file']";
      WebElement element = webElementManger(elementPath, WaitStrategy.PRESENCE);
      ((JavascriptExecutor)executor.get()).executeScript("arguments[0].style.display='block';", new Object[] { element });
      String a = System.getProperty("user.dir") + "\\src\\test\\resources\\" + getProperties("UploadFilesPath") + "\\" + fileName;
      element.sendKeys(new CharSequence[] { a });
    } catch (Exception e) {
      String elementPath = "(" + locator + "//input[@type='file'])[2]";
      WebElement element = webElementManger(elementPath, WaitStrategy.PRESENCE);
      ((JavascriptExecutor)executor.get()).executeScript("arguments[0].style.display='block';", new Object[] { element });
      String a = System.getProperty("user.dir") + "\\src\\test\\resources\\" + getProperties("UploadFilesPath") + "\\" + fileName;
      element.sendKeys(new CharSequence[] { a });
    } 
  }
  
  public static void clearTextBox(Object locator, String locatorName) {
    try {
      WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);
      highLightWebElementInScreenSchot(element, locatorName);
      element.clear();
      ((ExtentTest)BaseTest.logger.get()).info("Clearing Text in Element -->" + locatorName, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      unHighLightWebElementInScreenSchot(element);
    } catch (Exception e) {
      WebElement element = webElementManger(locator, WaitStrategy.PRESENCE);
      scrollToElement(locator, locatorName);
      highLightWebElementInScreenSchot(element, locatorName);
      element.clear();
      ((ExtentTest)BaseTest.logger.get()).info("Clearing Text in Element -->" + locatorName, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      unHighLightWebElementInScreenSchot(element);
    } 
  }
  
  public static void selectByVisibleText(Object locator, String value, String locatorName) {
    try {
      WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);
      highLightWebElementInScreenSchot(element, locatorName);
      Select select = new Select(element);
      select.selectByVisibleText(value);
      ((ExtentTest)BaseTest.logger.get()).info("SelectingValue from Selector" + locatorName + " and selectedValue is " + value, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      unHighLightWebElementInScreenSchot(element);
    } catch (Exception e) {
      WebElement element = webElementManger(locator, WaitStrategy.PRESENCE);
      highLightWebElementInScreenSchot(element, locatorName);
      Select select = new Select(element);
      select.selectByVisibleText(value);
      ((ExtentTest)BaseTest.logger.get()).info("SelectingValue from Selector" + locatorName + " and selectedValue is " + value, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      unHighLightWebElementInScreenSchot(element);
    } 
  }
  
  public static void selectByValue(Object locator, String value, String locatorName) {
    try {
      WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);
      highLightWebElementInScreenSchot(element, locatorName);
      Select select = new Select(element);
      select.selectByValue(value);
      ((ExtentTest)BaseTest.logger.get()).info("SelectingValue from Selector" + locatorName + " and selectedValue is " + value, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      unHighLightWebElementInScreenSchot(element);
    } catch (Exception e) {
      WebElement element = webElementManger(locator, WaitStrategy.PRESENCE);
      highLightWebElementInScreenSchot(element, locatorName);
      Select select = new Select(element);
      select.selectByValue(value);
      ((ExtentTest)BaseTest.logger.get()).info("SelectingValue from Selector" + locatorName + " and selectedValue is " + value, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      unHighLightWebElementInScreenSchot(element);
    } 
  }
  
  public static void selectByIndex(Object locator, int value, String locatorName) {
    try {
      WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);
      highLightWebElementInScreenSchot(element, locatorName);
      Select select = new Select(element);
      select.selectByIndex(value);
      ((ExtentTest)BaseTest.logger.get()).info("SelectingValue from Selector" + locatorName + " and selectedValue is " + value, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      unHighLightWebElementInScreenSchot(element);
    } catch (Exception e) {
      WebElement element = webElementManger(locator, WaitStrategy.PRESENCE);
      highLightWebElementInScreenSchot(element, locatorName);
      Select select = new Select(element);
      select.selectByIndex(value);
      ((ExtentTest)BaseTest.logger.get()).info("SelectingValue from Selector" + locatorName + " and selectedValue is " + value, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      unHighLightWebElementInScreenSchot(element);
    } 
  }
  
  public static void clickAndHitBackSPace(Object locator) {
    WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE, 
        Integer.parseInt(getProperties("smartTimeOut")));
    element.sendKeys(new CharSequence[] { (CharSequence)Keys.BACK_SPACE });
  }
  
  public static void clickAndHitEnter(By locator) {
    WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE, 
        Integer.parseInt(getProperties("smartTimeOut")));
    element.sendKeys(new CharSequence[] { (CharSequence)Keys.ENTER });
  }
  
  public static void clickAndHitDelete(By locator) {
    WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE, 
        Integer.parseInt(getProperties("smartTimeOut")));
    element.sendKeys(new CharSequence[] { (CharSequence)Keys.DELETE });
  }
  
  public static void clickAndHitClear(By locator) {
    WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE, 
        Integer.parseInt(getProperties("smartTimeOut")));
    element.sendKeys(new CharSequence[] { (CharSequence)Keys.CLEAR });
  }
  
  public static WebElement clickActiveElement() {
    WebElement element = webElementManger(getActiveElement(), WaitStrategy.CLICKABLE, 
        Integer.parseInt(getProperties("smartTimeOut")));
    click(element, "ActiveElement");
    return element;
  }
  
  public static void enterTextInActiveElement(String value) {
    WebElement element = webElementManger(getActiveElement(), WaitStrategy.CLICKABLE, 
        Integer.parseInt(getProperties("smartTimeOut")));
    enterText(element, value, "ActiveElement");
  }
  
  public static void enterText(Object locator, String value, String locatorName) {
    WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE, 
        Integer.parseInt(getProperties("smartTimeOut")));
    try {
      element.sendKeys(new CharSequence[] { value });
      highLightWebElementInScreenSchot(element, locatorName);
      ((ExtentTest)BaseTest.logger.get()).info("Entered text to Element -->" + locatorName + " enteredValue is ==>" + value, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      unHighLightWebElementInScreenSchot(element);
    } catch (Exception e) {
      try {
        scrollToElement(element, locatorName);
        ((JavascriptExecutor)executor.get()).executeScript("arguments[0].click();", new Object[] { element });
        highLightWebElementInScreenSchot(element, locatorName);
        element.sendKeys(new CharSequence[] { value });
      } catch (Exception e2) {
        enterValueByJs(element, value, locatorName);
      } 
      ((ExtentTest)BaseTest.logger.get()).info("Entered text to Element -->" + locatorName + " enteredValue is ==>" + value, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      unHighLightWebElementInScreenSchot(element);
    } 
  }
  
  public static void enterValueByJs(Object locator, String value, String locatorName) {
    WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE, 
        Integer.parseInt(getProperties("smartTimeOut")));
    try {
      ((JavascriptExecutor)executor.get()).executeScript("arguments[0].value='" + value + "'", new Object[] { element });
      highLightWebElementInScreenSchot(element, locatorName);
      ((ExtentTest)BaseTest.logger.get()).info("Entered text to Element -->" + locatorName + " enteredValue is ==>" + value, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      unHighLightWebElementInScreenSchot(element);
    } catch (Exception e) {
      ((JavascriptExecutor)executor.get()).executeScript("arguments[0].click();", new Object[] { element });
      ((JavascriptExecutor)executor.get()).executeScript("arguments[0].value='" + value + "'", new Object[] { element });
      highLightWebElementInScreenSchot(element, locatorName);
      ((ExtentTest)BaseTest.logger.get()).info("Entered text to Element -->" + locatorName + " enteredValue is ==>" + value, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      unHighLightWebElementInScreenSchot(element);
    } 
  }
  
  public static WebElement getWebElement(String locator) {
    WebElement element = null;
    if (locator.startsWith("//")) {
      element = getlocalDriver().findElement(By.xpath(locator));
    } else if (locator.startsWith("id")) {
      element = getlocalDriver().findElement(By.id(locator));
    } else if (locator.startsWith("class")) {
      element = getlocalDriver().findElement(By.className(locator));
    } else if (locator.startsWith("css")) {
      element = getlocalDriver().findElement(By.cssSelector(locator));
    } 
    return element;
  }
  
  public static void enterJsByValueByNamuber(Object locator, String value, String locatorName) {
    WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE, 
        Integer.parseInt(getProperties("smartTimeOut")));
    try {
      highLightWebElementInScreenSchot(element, locatorName);
      ((JavascriptExecutor)executor.get()).executeScript("arguments[0].valueAsNumber='" + value + "'", new Object[] { element });
      ((ExtentTest)BaseTest.logger.get()).info("Entered text to Element -->" + locatorName + " enteredValue is ==>" + value, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      unHighLightWebElementInScreenSchot(element);
    } catch (Exception e) {
      highLightWebElementInScreenSchot(element, locatorName);
      ((JavascriptExecutor)executor.get()).executeScript("arguments[0].click();", new Object[] { element });
      ((JavascriptExecutor)executor.get()).executeScript("arguments[0].valueAsNumber='" + value + "'", new Object[] { element });
      ((ExtentTest)BaseTest.logger.get()).info("Entered text to Element -->" + locatorName + " enteredValue is ==>" + value, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      unHighLightWebElementInScreenSchot(element);
    } 
  }
  
  public static WebElement getLocatorJs(String locator) {
    return (WebElement)((JavascriptExecutor)executor.get()).executeScript("var element=document.evaluate(\"" + locator + "\", document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null).snapshotItem(0); return element;", new Object[0]);
  }
  
  public static void clickJs(Object locator, String locatorName) {
    try {
      WebElement element = webElementManger(locatorName, WaitStrategy.CLICKABLE);
      highLightWebElementInScreenSchot(element, locatorName);
      ((ExtentTest)BaseTest.logger.get()).info("Clicking onElement -->" + locatorName, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      ((JavascriptExecutor)executor.get()).executeScript("arguments[0].click();", new Object[] { element });
      unHighLightWebElementInScreenSchot(element);
    } catch (Exception e) {
      WebElement element = webElementManger(locatorName, WaitStrategy.VISIBLE);
      scrollToElement(element, locatorName);
      highLightWebElementInScreenSchot(element, locatorName);
      ((ExtentTest)BaseTest.logger.get()).info("Clicking onElement -->" + locatorName, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
      ((JavascriptExecutor)executor.get()).executeScript("arguments[0].click();", new Object[] { element });
      unHighLightWebElementInScreenSchot(element);
    } 
  }
  
  public static void scrollToElement(Object locator, String locatorName) {
    WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);
    ((JavascriptExecutor)executor.get()).executeScript("arguments[0].scrollIntoView();", new Object[] { element });
    wait(2000);
    highLightWebElementInScreenSchot(element, locatorName);
    ((ExtentTest)BaseTest.logger.get()).info("Clicking scrollingElemet -->" + locatorName, 
        MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
    unHighLightWebElementInScreenSchot(element);
  }
  
  public static String getText(Object locator, String locatorName) {
    WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);
    highLightWebElementInScreenSchot(element);
    String savedValue = "";
    savedValue = element.getText();
    if (savedValue.equals("")) {
      savedValue = element.getAttribute("value");
      if (savedValue.equals(""))
        savedValue = element.getAttribute("placeholder"); 
    } 
    ((ExtentTest)BaseTest.logger.get()).info("Saving value from Locator -->" + locatorName, 
        MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
    unHighLightWebElementInScreenSchot(element);
    return savedValue;
  }
  
  public static void scrollToElement(Object locator) {
    WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);
    ((JavascriptExecutor)executor.get()).executeScript("arguments[0].scrollIntoView();", new Object[] { element });
    wait(2000);
    highLightWebElementInScreenSchot(element);
    unHighLightWebElementInScreenSchot(element);
  }
  
  public static WebElement getActiveElement() {
    return ((WebDriver)localDriver.get()).switchTo().activeElement();
  }
  
  public static void wait(int timeout) {
    try {
      Thread.sleep(timeout);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } 
  }
  
  public static boolean isVisible(Object locator, String locatorName) {
    boolean result = false;
    try {
      WebElement element = webElementManger(locator, WaitStrategy.VISIBLE);
      result = true;
      highLightWebElementInScreenSchot(element, locatorName);
      ((ExtentTest)BaseTest.logger.get()).info("Given Element is Visble -->" + locatorName, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
    } catch (Exception e) {
      ((ExtentTest)BaseTest.logger.get()).info("Given Element is  Not Visble -->" + locatorName, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
    } 
    return result;
  }
  
  public static boolean isVisible(Object locator, String locatorName, int timeout) {
    boolean result = false;
    try {
      WebElement element = webElementManger(locator, WaitStrategy.VISIBLE, timeout);
      result = true;
      highLightWebElementInScreenSchot(element, locatorName);
      ((ExtentTest)BaseTest.logger.get()).info("Given Element is Visble -->" + locatorName, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
    } catch (Exception e) {
      ((ExtentTest)BaseTest.logger.get()).info("Given Element is  Not Visble -->" + locatorName, 
          MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
    } 
    return result;
  }
  
  public static boolean isNotVisible(Object locator, String locatorName, int timeout) {
    boolean result = false;
    try {
      WebElement element = webElementManger(locator, WaitStrategy.VISIBLE, timeout);
      if (element == null) {
        ((ExtentTest)BaseTest.logger.get()).info("Given Element is  Not Visble and Step is Passed -->" + locatorName);
        result = true;
      } else {
        ((ExtentTest)BaseTest.logger.get()).info("Given Element is Should not be Visble but the Element is Visble hence failing the  Step -->" + locatorName);
      } 
    } catch (Exception e) {
      ((ExtentTest)BaseTest.logger.get()).info("Given Element is  Not Visble and Step is Passed -->" + locatorName);
      result = true;
    } 
    return result;
  }
  
  public static boolean isNotVisible(Object locator, String locatorName) {
    boolean result = false;
    try {
      WebElement element = webElementManger(locator, WaitStrategy.VISIBLE, 10);
      if (element == null) {
        ((ExtentTest)BaseTest.logger.get()).info("Given Element is  Not Visble and Step is Passed -->" + locatorName);
        result = true;
      } else {
        ((ExtentTest)BaseTest.logger.get()).info("Given Element is Should not be Visble but the Element is Visble hence failing the  Step -->" + locatorName);
      } 
    } catch (Exception e) {
      ((ExtentTest)BaseTest.logger.get()).info("Given Element is  Not Visble and Step is Passed -->" + locatorName);
      result = true;
    } 
    return result;
  }
  
  public static void doubleClick(Object locator, String locatorName) {
    WebElement element = webElementManger(locatorName, WaitStrategy.CLICKABLE);
    highLightWebElementInScreenSchot(element, locatorName);
    ((ExtentTest)BaseTest.logger.get()).info("Clicking onElement -->" + locatorName, 
        MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
    ((Actions)actions.get()).doubleClick().perform();
    unHighLightWebElementInScreenSchot(element);
  }
  
  public static void contextClick(Object locator, String locatorName) {
    WebElement element = webElementManger(locatorName, WaitStrategy.CLICKABLE);
    highLightWebElementInScreenSchot(element, locatorName);
    ((ExtentTest)BaseTest.logger.get()).info("Clicking onElement -->" + locatorName, 
        MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
    ((Actions)actions.get()).contextClick().perform();
    unHighLightWebElementInScreenSchot(element);
  }
  
  public static WebDriver initBrowser(String browserName) {
    ChromeOptions option;
    String downloadFilepath;
    HashMap<String, Object> setPath, chromeOptionsMap;
    FirefoxOptions options;
    EdgeOptions edgeOptions;
    SafariOptions safari;
    SafariDriver safariDriver;
    switch (browserName.toLowerCase()) {
      case "chrome":
        option = new ChromeOptions();
        option.addArguments(new String[] { "--remote-allow-origins=*" });
        option.addArguments(new String[] { "incognito" });
        option.addArguments(new String[] { "--remote-allow-origins=*" });
        downloadFilepath = System.getProperty("user.home") + "/Downloads/";
        setPath = new HashMap<>();
        setPath.put("download.default_directory", downloadFilepath);
        setPath.put("safebrowsing.enabled", "false");
        chromeOptionsMap = new HashMap<>();
        option.setExperimentalOption("prefs", setPath);
        option.addArguments(new String[] { "--disable-extensions" });
        if (getProperties("headless").equalsIgnoreCase("true")) {
          Map<String, Object> prefs = new HashMap<>();
          prefs.put("browser.show_hub_popup_on_download_start", Boolean.valueOf(false));
          prefs.put("download.prompt_for_download", Boolean.valueOf(false));
          option.setExperimentalOption("prefs", prefs);
          option.addArguments(new String[] { "--headless=chrome" });
        } else {
          setPath.put("download.prompt_for_download", Boolean.valueOf(false));
          setPath.put("directory_upgrade", Boolean.valueOf(true));
        } 
        if (getProperties("executionMode").equalsIgnoreCase("remote")) {
          try {
            SeleniumWrapperClassBkp.driver = (WebDriver)new RemoteWebDriver(new URL(getProperties("hubUrl")), (Capabilities)option);
          } catch (Exception e) {
            System.out.println("Issue with Remote WebDrver Intialisation");
            e.printStackTrace();
          } 
        } else {
          SeleniumWrapperClassBkp.driver = (WebDriver)new ChromeDriver(option);
        } 
        localDriver.set(SeleniumWrapperClassBkp.driver);
        ((WebDriver)localDriver.get()).manage().window().maximize();
        executor.set((JavascriptExecutor)localDriver.get());
        screeShot.set((TakesScreenshot)localDriver.get());
        actions.set(new Actions(localDriver.get()));
        ((WebDriver)localDriver.get()).manage().timeouts().implicitlyWait(Duration.ofSeconds(10L));
        return localDriver.get();
      case "firefox":
        options = new FirefoxOptions();
        options.addArguments(new String[] { "--inprivate" });
        SeleniumWrapperClassBkp.driver = (WebDriver)new FirefoxDriver(options);
        localDriver.set(SeleniumWrapperClassBkp.driver);
        ((WebDriver)localDriver.get()).manage().window().maximize();
        executor.set((JavascriptExecutor)localDriver.get());
        screeShot.set((TakesScreenshot)localDriver.get());
        actions.set(new Actions(localDriver.get()));
        ((WebDriver)localDriver.get()).manage().timeouts().implicitlyWait(Duration.ofSeconds(10L));
        return localDriver.get();
      case "edge":
        edgeOptions = new EdgeOptions();
        edgeOptions.addArguments(new String[] { "-inprivate" });
        SeleniumWrapperClassBkp.driver = (WebDriver)new EdgeDriver(edgeOptions);
        localDriver.set(SeleniumWrapperClassBkp.driver);
        ((WebDriver)localDriver.get()).manage().window().maximize();
        executor.set((JavascriptExecutor)localDriver.get());
        screeShot.set((TakesScreenshot)localDriver.get());
        actions.set(new Actions(localDriver.get()));
        ((WebDriver)localDriver.get()).manage().timeouts().implicitlyWait(Duration.ofSeconds(10L));
        return localDriver.get();
      case "safari":
        safari = new SafariOptions();
        safariDriver = new SafariDriver(safari);
        localDriver.set(safariDriver);
        ((WebDriver)localDriver.get()).manage().window().maximize();
        executor.set((JavascriptExecutor)localDriver.get());
        screeShot.set((TakesScreenshot)localDriver.get());
        actions.set(new Actions(localDriver.get()));
        ((WebDriver)localDriver.get()).manage().timeouts().implicitlyWait(Duration.ofSeconds(10L));
        return localDriver.get();
    } 
    System.out.println("please pass the right browser name......");
    ((WebDriver)localDriver.get()).manage().window().maximize();
    executor.set((JavascriptExecutor)localDriver.get());
    screeShot.set((TakesScreenshot)localDriver.get());
    actions.set(new Actions(localDriver.get()));
    ((WebDriver)localDriver.get()).manage().timeouts().implicitlyWait(Duration.ofSeconds(10L));
    return localDriver.get();
  }
  
  public static Properties init_properties() {
    try {
      FileInputStream fileInputStream = new FileInputStream("./src/test/resources/config/config.properties");
      properties = new Properties();
      properties.load(fileInputStream);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } 
    return properties;
  }
  
  public static String getProperties(String key) {
    String value = properties.getProperty(key).trim();
    return value;
  }
  
  public static String takeScreenshot() {
    String SrcFile = (String)((TakesScreenshot)screeShot.get()).getScreenshotAs(OutputType.BASE64);
    return SrcFile;
  }
  
  public static void closeBrowser() {
    ((WebDriver)localDriver.get()).quit();
  }
  
  public static void highLightWebElementInScreenSchot(WebElement element, String locatorName) {}
  
  public static void highLightWebElementInScreenSchot(WebElement element) {}
  
  public static void unHighLightWebElementInScreenSchot(WebElement element) {}
  
  public static String getUtcDate() {
    LocalDateTime utcTime = LocalDateTime.now(ZoneOffset.UTC);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
    return utcTime.format(formatter).toString();
  }
  
  public static String getLocalDate() {
    LocalDateTime utcTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM");
    return utcTime.format(formatter).toString();
  }
  
  public static String getUtcTime() {
    LocalDateTime utcTime = LocalDateTime.now(ZoneOffset.UTC);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    return utcTime.format(formatter).toString();
  }
  
  public static String getLocalTime() {
    LocalDateTime utcTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh-mm a");
    return utcTime.format(formatter).toString();
  }
  
  public static String getUtcDateTimeStamp() {
    LocalDateTime utcTime = LocalDateTime.now(ZoneOffset.UTC);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy  HH:mm:ss");
    return utcTime.format(formatter).toString();
  }
}

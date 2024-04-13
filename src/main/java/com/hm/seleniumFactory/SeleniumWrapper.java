package com.hm.seleniumFactory;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.hm.enums.WaitStrategy;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
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

public class SeleniumWrapper {
	WebDriver driver = null;

	public JavascriptExecutor executor = null;

	public TakesScreenshot ts;

	public Actions actions = null;

	TakesScreenshot screenShot = null;

	public SeleniumWrapper(String browserName) {
		initBrowser(browserName);
		this.driver = this.driver;
	}

	public WebDriver getlocalDriver() {
		return this.driver;
	}

	public JavascriptExecutor getexecutor() {
		return this.executor;
	}

	public void openWebsite(String url, String pageName) {
		getlocalDriver().get(url);
		getlocalDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20L));
		(new WebDriverWait(getlocalDriver(), Duration.ofSeconds(15L)))
				.until(webDriver -> Boolean.valueOf(((JavascriptExecutor) webDriver)
						.executeScript("return document.readyState", new Object[0]).equals("complete")));

		if (PropertyLoader.isPassedScreenShotRequired.equalsIgnoreCase("true")) {
			(BaseTest.logger.get()).info("SuccesFully Page >>" + pageName + "<< is opened ",
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
		} else {

			(BaseTest.logger.get()).info("SuccesFully Page >>" + pageName + "<< is opened ");
		}

	}

	public WebElement webElementManger(Object locator, WaitStrategy Strategy) {
		WebElement element = null;
		if (locator instanceof WebElement) {
			element = waitForElement((WebElement) locator, Strategy);
		} else if (locator instanceof By) {
			element = waitForElement((By) locator, Strategy);
		} else {
			wait(6000);
			element = getLocatorJs((String) locator);
		}
		return element;
	}

	public WebElement webElementManger(Object locator, WaitStrategy Strategy, int timeOut) {
		WebElement element = null;
		if (locator instanceof WebElement) {
			element = waitForElement((WebElement) locator, Strategy);
		} else if (locator instanceof By) {
			element = waitForElement((By) locator, Strategy);
		} else {
			element = waitForElement((String) locator, Strategy, timeOut);
		}
		return element;
	}

	public WebElement waitForElement(By locator, WaitStrategy Strategy, int timeOut) {
		WebElement result = null;
		switch (Strategy) {
		case CLICKABLE:
			result = (WebElement) (new WebDriverWait(this.driver, Duration.ofSeconds(timeOut)))
					.until((Function) ExpectedConditions.elementToBeClickable(locator));
			break;
		case VISIBLE:
			result = (WebElement) (new WebDriverWait(this.driver, Duration.ofSeconds(timeOut)))
					.until((Function) ExpectedConditions.visibilityOfElementLocated(locator));
			break;
		case PRESENCE:
			result = (WebElement) (new WebDriverWait(this.driver, Duration.ofSeconds(timeOut)))
					.until((Function) ExpectedConditions.presenceOfElementLocated(locator));
			break;
		}
		return result;
	}

	public WebElement waitForElement(By locator, WaitStrategy Strategy) {
		WebElement result = null;
		switch (Strategy) {
		case CLICKABLE:
			result = (WebElement) (new WebDriverWait(this.driver, Duration.ofSeconds(15L)))
					.until((Function) ExpectedConditions.elementToBeClickable(locator));
			break;
		case VISIBLE:
			result = (WebElement) (new WebDriverWait(this.driver, Duration.ofSeconds(15L)))
					.until((Function) ExpectedConditions.visibilityOfElementLocated(locator));
			break;
		case PRESENCE:
			result = (WebElement) (new WebDriverWait(this.driver, Duration.ofSeconds(15L)))
					.until((Function) ExpectedConditions.presenceOfElementLocated(locator));
			break;
		}
		return result;
	}

	public WebElement waitForElement(WebElement locator, WaitStrategy Strategy, int timeOut) {
		WebElement result = null;
		switch (Strategy) {
		case CLICKABLE:
			result = (WebElement) (new WebDriverWait(this.driver, Duration.ofSeconds(timeOut)))
					.until((Function) ExpectedConditions.elementToBeClickable(locator));
			break;
		case VISIBLE:
			result = (WebElement) (new WebDriverWait(this.driver, Duration.ofSeconds(timeOut)))
					.until((Function) ExpectedConditions.visibilityOf(locator));
			break;
		}
		return result;
	}

	public WebElement waitForElement(String locator, WaitStrategy Strategy, int timeOut) {
		WebElement result = null;
		switch (Strategy) {
		case CLICKABLE:
			result = (WebElement) (new WebDriverWait(this.driver, Duration.ofSeconds(timeOut)))
					.until((Function) ExpectedConditions.elementToBeClickable(getWebElement(locator)));
			return result;
		case VISIBLE:
			result = (WebElement) (new WebDriverWait(this.driver, Duration.ofSeconds(timeOut)))
					.until((Function) ExpectedConditions.visibilityOf(getWebElement(locator)));
			return result;
		}
		result = getLocatorJs(locator);
		return result;
	}

	public WebElement waitForElement(WebElement locator, WaitStrategy Strategy) {
		WebElement result = null;
		switch (Strategy) {
		case CLICKABLE:
			result = (WebElement) (new WebDriverWait(this.driver,
					Duration.ofSeconds(Integer.parseInt(PropertyLoader.getProperties("smartTimeOut")))))
							.until((Function) ExpectedConditions.elementToBeClickable(locator));
			break;
		case VISIBLE:
			result = (WebElement) (new WebDriverWait(this.driver,
					Duration.ofSeconds(Integer.parseInt(PropertyLoader.getProperties("smartTimeOut")))))
							.until((Function) ExpectedConditions.visibilityOf(locator));
			break;
		}
		return result;
	}

	public void click(Object locator, String locatorName) {
		try {
			WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);
			highLightWebElementInScreenSchot(element, locatorName);
			if (PropertyLoader.isPassedScreenShotRequired.equalsIgnoreCase("true")) {
				(BaseTest.logger.get()).info("Clicking onElement -->" + locatorName,
						MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			} else {

				(BaseTest.logger.get()).info("Clicking onElement -->" + locatorName);
			}
			unHighLightWebElementInScreenSchot(element);

			element.click();
		} catch (Exception e) {
			WebElement element = webElementManger(locator, WaitStrategy.PRESENCE);
			scrollToElement(locator, locatorName);
			highLightWebElementInScreenSchot(element, locatorName);
			(BaseTest.logger.get()).info("Clicking onElement -->" + locatorName,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			element.click();
			unHighLightWebElementInScreenSchot(element);
		}
	}

	public void click(Object locator) {
		try {
			WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);

			if (PropertyLoader.isPassedScreenShotRequired.equalsIgnoreCase("true")) {
				(BaseTest.logger.get()).info("Clicking onElement -->" + locator,
						MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			} else {

				(BaseTest.logger.get()).info("Clicking onElement -->" + locator);
			}
			highLightWebElementInScreenSchot(element);
			element.click();
			unHighLightWebElementInScreenSchot(element);
		} catch (Exception e) {
			WebElement element = webElementManger(locator, WaitStrategy.PRESENCE);
			scrollToElement(locator);
			highLightWebElementInScreenSchot(element);
			(BaseTest.logger.get()).info("Clicking onElement -->" + locator,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			element.click();
			unHighLightWebElementInScreenSchot(element);
		}
	}

	public void forceClick(Object locator) {
		try {
			WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);
			if (PropertyLoader.isPassedScreenShotRequired.equalsIgnoreCase("true")) {
				(BaseTest.logger.get()).info("Clicking onElement -->" + locator,
						MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			} else {

				(BaseTest.logger.get()).info("Clicking onElement -->" + locator);
			}

			highLightWebElementInScreenSchot(element);
			this.executor.executeScript("arguments[0].click();", element);
		} catch (Exception e) {
			WebElement element = webElementManger(locator, WaitStrategy.PRESENCE);
			(BaseTest.logger.get()).info("SecondTry-Clicking onElement -->" + locator,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			scrollToElement(locator);
			highLightWebElementInScreenSchot(element);
			element.click();
			unHighLightWebElementInScreenSchot(element);
		}
	}

	public void forceClick(Object locator, String locatorName) {
		try {
			WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);
			if (PropertyLoader.isPassedScreenShotRequired.equalsIgnoreCase("true")) {
				(BaseTest.logger.get()).info("Clicking onElement -->" + locator,
						MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			} else {

				(BaseTest.logger.get()).info("Clicking onElement -->" + locator);
			}
			highLightWebElementInScreenSchot(element);
			this.executor.executeScript("arguments[0].click();", element);
		} catch (Exception e) {
			WebElement element = webElementManger(locator, WaitStrategy.PRESENCE);
			(BaseTest.logger.get()).info("SecondTry-Clicking onElement -->" + locatorName,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			scrollToElement(locator);
			highLightWebElementInScreenSchot(element);
			element.click();
			unHighLightWebElementInScreenSchot(element);
		}
	}

	public void uploadFile(String locator, String fileName) {
		try {
			String elementPath = locator + "//input[@type='file']";
			WebElement element = webElementManger(elementPath, WaitStrategy.PRESENCE);
			this.executor.executeScript("arguments[0].style.display='block';", element);
			String a = System.getProperty("user.dir") + "\\src\\test\\resources\\"
					+ PropertyLoader.getProperties("UploadFilesPath") + "\\" + fileName;
			element.sendKeys(new CharSequence[] { a });
		} catch (Exception e) {
			String elementPath = "(" + locator + "//input[@type='file'])[2]";
			WebElement element = webElementManger(elementPath, WaitStrategy.PRESENCE);
			this.executor.executeScript("arguments[0].style.display='block';", element);
			String a = System.getProperty("user.dir") + "\\src\\test\\resources\\"
					+ PropertyLoader.getProperties("UploadFilesPath") + "\\" + fileName;
			element.sendKeys(new CharSequence[] { a });
		}
	}

	public void clearTextBox(Object locator, String locatorName) {
		try {
			WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);
			highLightWebElementInScreenSchot(element, locatorName);
			element.clear();
			(BaseTest.logger.get()).info("Clearing Text in Element -->" + locatorName,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			unHighLightWebElementInScreenSchot(element);
		} catch (Exception e) {
			WebElement element = webElementManger(locator, WaitStrategy.PRESENCE);
			scrollToElement(locator, locatorName);
			highLightWebElementInScreenSchot(element, locatorName);
			element.clear();
			(BaseTest.logger.get()).info("Clearing Text in Element -->" + locatorName,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			unHighLightWebElementInScreenSchot(element);
		}
	}

	public void selectByVisibleText(Object locator, String value, String locatorName) {
		try {
			WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);
			highLightWebElementInScreenSchot(element, locatorName);
			Select select = new Select(element);
			select.selectByVisibleText(value);
			(BaseTest.logger.get()).info(
					"SelectingValue from Selector" + locatorName + " and selectedValue is " + value,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			unHighLightWebElementInScreenSchot(element);
		} catch (Exception e) {
			WebElement element = webElementManger(locator, WaitStrategy.PRESENCE);
			highLightWebElementInScreenSchot(element, locatorName);
			Select select = new Select(element);
			select.selectByVisibleText(value);
			(BaseTest.logger.get()).info(
					"SelectingValue from Selector" + locatorName + " and selectedValue is " + value,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			unHighLightWebElementInScreenSchot(element);
		}
	}

	public void selectByValue(Object locator, String value, String locatorName) {
		try {
			WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);
			highLightWebElementInScreenSchot(element, locatorName);
			Select select = new Select(element);
			select.selectByValue(value);

			if (PropertyLoader.isPassedScreenShotRequired.equalsIgnoreCase("true")) {
				(BaseTest.logger.get()).info(
						"SelectingValue from Selector" + locatorName + " and selectedValue is " + value,
						MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			} else {

				(BaseTest.logger.get())
						.info("SelectingValue from Selector" + locatorName + " and selectedValue is " + value);
			}

			unHighLightWebElementInScreenSchot(element);
		} catch (Exception e) {
			WebElement element = webElementManger(locator, WaitStrategy.PRESENCE);
			highLightWebElementInScreenSchot(element, locatorName);
			Select select = new Select(element);
			select.selectByValue(value);
			(BaseTest.logger.get()).info(
					"SelectingValue from Selector" + locatorName + " and selectedValue is " + value,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			unHighLightWebElementInScreenSchot(element);
		}
	}

	public void selectByIndex(Object locator, int value, String locatorName) {
		try {
			WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);
			highLightWebElementInScreenSchot(element, locatorName);
			Select select = new Select(element);
			select.selectByIndex(value);
			(BaseTest.logger.get()).info(
					"SelectingValue from Selector" + locatorName + " and selectedValue is " + value,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			unHighLightWebElementInScreenSchot(element);
		} catch (Exception e) {
			WebElement element = webElementManger(locator, WaitStrategy.PRESENCE);
			highLightWebElementInScreenSchot(element, locatorName);
			Select select = new Select(element);
			select.selectByIndex(value);
			(BaseTest.logger.get()).info(
					"SelectingValue from Selector" + locatorName + " and selectedValue is " + value,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			unHighLightWebElementInScreenSchot(element);
		}
	}

	public void clickAndHitBackSPace(Object locator) {
		WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE,
				Integer.parseInt(PropertyLoader.getProperties("smartTimeOut")));
		element.sendKeys(new CharSequence[] { (CharSequence) Keys.BACK_SPACE });
	}

	public void clickAndHitEnter(By locator) {
		WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE,
				Integer.parseInt(PropertyLoader.getProperties("smartTimeOut")));
		element.sendKeys(new CharSequence[] { (CharSequence) Keys.ENTER });
	}

	public void clickAndHitDelete(By locator) {
		WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE,
				Integer.parseInt(PropertyLoader.getProperties("smartTimeOut")));
		element.sendKeys(new CharSequence[] { (CharSequence) Keys.DELETE });
	}

	public void clickAndHitClear(By locator) {
		WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE,
				Integer.parseInt(PropertyLoader.getProperties("smartTimeOut")));
		element.sendKeys(new CharSequence[] { (CharSequence) Keys.CLEAR });
	}

	public WebElement clickActiveElement() {
		WebElement element = webElementManger(getActiveElement(), WaitStrategy.CLICKABLE,
				Integer.parseInt(PropertyLoader.getProperties("smartTimeOut")));
		click(element, "ActiveElement");
		return element;
	}

	public void enterTextInActiveElement(String value) {
		WebElement element = webElementManger(getActiveElement(), WaitStrategy.CLICKABLE,
				Integer.parseInt(PropertyLoader.getProperties("smartTimeOut")));
		enterText(element, value, "ActiveElement");
	}

	public void enterText(Object locator, String value, String locatorName) {
		WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE,
				Integer.parseInt(PropertyLoader.getProperties("smartTimeOut")));
		try {
			element.sendKeys(new CharSequence[] { value });
			highLightWebElementInScreenSchot(element, locatorName);

			if (PropertyLoader.isPassedScreenShotRequired.equalsIgnoreCase("true")) {
				(BaseTest.logger.get()).info(
						"Entered text to Element -->" + locatorName + " enteredValue is ==>" + value,
						MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			} else {

				(BaseTest.logger.get())
						.info("Entered text to Element -->" + locatorName + " enteredValue is ==>" + value);
			}

			unHighLightWebElementInScreenSchot(element);
		} catch (Exception e) {
			try {
				scrollToElement(element, locatorName);
				this.executor.executeScript("arguments[0].click();", element);
				highLightWebElementInScreenSchot(element, locatorName);
				element.sendKeys(new CharSequence[] { value });
			} catch (Exception e2) {
				enterValueByJs(element, value, locatorName);
			}
			(BaseTest.logger.get()).info("Entered text to Element -->" + locatorName + " enteredValue is ==>" + value,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			unHighLightWebElementInScreenSchot(element);
		}
	}

	public void enterValueByJs(Object locator, String value, String locatorName) {
		WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE,
				Integer.parseInt(PropertyLoader.getProperties("smartTimeOut")));
		try {
			this.executor.executeScript("arguments[0].value='" + value + "'", element);
			highLightWebElementInScreenSchot(element, locatorName);
			(BaseTest.logger.get()).info("Entered text to Element -->" + locatorName + " enteredValue is ==>" + value,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			unHighLightWebElementInScreenSchot(element);
		} catch (Exception e) {
			this.executor.executeScript("arguments[0].click();", element);
			this.executor.executeScript("arguments[0].value='" + value + "'", element);
			highLightWebElementInScreenSchot(element, locatorName);
			(BaseTest.logger.get()).info("Entered text to Element -->" + locatorName + " enteredValue is ==>" + value,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			unHighLightWebElementInScreenSchot(element);
		}
	}

	public WebElement getWebElement(String locator) {
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

	public void enterJsByValueByNamuber(Object locator, String value, String locatorName) {
		WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE,
				Integer.parseInt(PropertyLoader.getProperties("smartTimeOut")));
		try {
			highLightWebElementInScreenSchot(element, locatorName);
			this.executor.executeScript("arguments[0].valueAsNumber='" + value + "'", element);
			(BaseTest.logger.get()).info("Entered text to Element -->" + locatorName + " enteredValue is ==>" + value,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			unHighLightWebElementInScreenSchot(element);
		} catch (Exception e) {
			highLightWebElementInScreenSchot(element, locatorName);
			this.executor.executeScript("arguments[0].click();", element);
			this.executor.executeScript("arguments[0].valueAsNumber='" + value + "'", element);
			(BaseTest.logger.get()).info("Entered text to Element -->" + locatorName + " enteredValue is ==>" + value,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			unHighLightWebElementInScreenSchot(element);
		}
	}

	public WebElement getLocatorJs(String locator) {
		return (WebElement) this.executor.executeScript("var element=document.evaluate(\"" + locator
				+ "\", document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null).snapshotItem(0); return element;",
				new Object[0]);
	}

	public void clickJs(Object locator, String locatorName) {
		try {
			WebElement element = webElementManger(locatorName, WaitStrategy.CLICKABLE);
			highLightWebElementInScreenSchot(element, locatorName);
			(BaseTest.logger.get()).info("Clicking onElement -->" + locatorName,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			this.executor.executeScript("arguments[0].click();", element);
			unHighLightWebElementInScreenSchot(element);
		} catch (Exception e) {
			WebElement element = webElementManger(locatorName, WaitStrategy.VISIBLE);
			scrollToElement(element, locatorName);
			highLightWebElementInScreenSchot(element, locatorName);
			(BaseTest.logger.get()).info("Clicking onElement -->" + locatorName,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			this.executor.executeScript("arguments[0].click();", element);
			unHighLightWebElementInScreenSchot(element);
		}
	}

	public void scrollToElement(Object locator, String locatorName) {
		WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);
		this.executor.executeScript("arguments[0].scrollIntoView();", element);
		wait(2000);
		highLightWebElementInScreenSchot(element, locatorName);
		(BaseTest.logger.get()).info("Clicking scrollingElemet -->" + locatorName,
				MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
		unHighLightWebElementInScreenSchot(element);
	}

	public String getText(Object locator, String locatorName) {
		WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);
		highLightWebElementInScreenSchot(element);
		String savedValue = "";
		savedValue = element.getText();
		if (savedValue.equals("")) {
			savedValue = element.getAttribute("value");
			if (savedValue.equals(""))
				savedValue = element.getAttribute("placeholder");
		}
		(BaseTest.logger.get()).info("Saving value from Locator -->" + locatorName,
				MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
		unHighLightWebElementInScreenSchot(element);
		return savedValue;
	}

	public void scrollToElement(Object locator) {
		WebElement element = webElementManger(locator, WaitStrategy.CLICKABLE);
		this.executor.executeScript("arguments[0].scrollIntoView();", element);
		wait(2000);
		highLightWebElementInScreenSchot(element);
		unHighLightWebElementInScreenSchot(element);
	}

	public WebElement getActiveElement() {
		return this.driver.switchTo().activeElement();
	}

	public void wait(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean isVisible(Object locator, String locatorName) {
		boolean result = false;
		try {
			WebElement element = webElementManger(locator, WaitStrategy.VISIBLE);
			result = true;
			highLightWebElementInScreenSchot(element, locatorName);

			if (PropertyLoader.isPassedScreenShotRequired.equalsIgnoreCase("true")) {

				(BaseTest.logger.get()).info("Given Element is Visble -->" + locatorName,
						MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			} else {

				(BaseTest.logger.get()).info("Given Element is Visble -->" + locatorName);
			}

		} catch (Exception e) {
			(BaseTest.logger.get()).info("Given Element is  Not Visble -->" + locatorName,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
		}
		return result;
	}

	public boolean isVisible(Object locator, String locatorName, int timeout) {
		boolean result = false;
		try {
			WebElement element = webElementManger(locator, WaitStrategy.VISIBLE, timeout);
			result = true;
			highLightWebElementInScreenSchot(element, locatorName);
			if (PropertyLoader.isPassedScreenShotRequired.equalsIgnoreCase("true")) {

				(BaseTest.logger.get()).info("Given Element is Visble -->" + locatorName,
						MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
			} else {

				(BaseTest.logger.get()).info("Given Element is Visble -->" + locatorName);
			}

		} catch (Exception e) {
			(BaseTest.logger.get()).info("Given Element is  Not Visble -->" + locatorName,
					MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
		}
		return result;
	}

	public boolean isNotVisible(Object locator, String locatorName, int timeout) {
		boolean result = false;
		try {
			WebElement element = webElementManger(locator, WaitStrategy.VISIBLE, timeout);
			if (element == null) {
				(BaseTest.logger.get()).info("Given Element is  Not Visble and Step is Passed -->" + locatorName);
				result = true;
			} else {
				(BaseTest.logger.get()).info(
						"Given Element is Should not be Visble but the Element is Visble hence failing the  Step -->"
								+ locatorName);
			}
		} catch (Exception e) {
			(BaseTest.logger.get()).info("Given Element is  Not Visble and Step is Passed -->" + locatorName);
			result = true;
		}
		return result;
	}

	public boolean isNotVisible(Object locator, String locatorName) {
		boolean result = false;
		try {
			WebElement element = webElementManger(locator, WaitStrategy.VISIBLE, 10);
			if (element == null) {
				(BaseTest.logger.get()).info("Given Element is  Not Visble and Step is Passed -->" + locatorName);
				result = true;
			} else {
				(BaseTest.logger.get()).info(
						"Given Element is Should not be Visble but the Element is Visble hence failing the  Step -->"
								+ locatorName);
			}
		} catch (Exception e) {
			(BaseTest.logger.get()).info("Given Element is  Not Visble and Step is Passed -->" + locatorName);
			result = true;
		}
		return result;
	}

	public void doubleClick(Object locator, String locatorName) {
		WebElement element = webElementManger(locatorName, WaitStrategy.CLICKABLE);
		highLightWebElementInScreenSchot(element, locatorName);
		(BaseTest.logger.get()).info("Clicking onElement -->" + locatorName,
				MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
		this.actions.doubleClick().perform();
		unHighLightWebElementInScreenSchot(element);
	}

	public void contextClick(Object locator, String locatorName) {
		WebElement element = webElementManger(locatorName, WaitStrategy.CLICKABLE);
		highLightWebElementInScreenSchot(element, locatorName);
		(BaseTest.logger.get()).info("Clicking onElement -->" + locatorName,
				MediaEntityBuilder.createScreenCaptureFromBase64String(takeScreenshot()).build());
		this.actions.contextClick().perform();
		unHighLightWebElementInScreenSchot(element);
	}

	public WebDriver initBrowser(String browserName) {
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
			if (PropertyLoader.getProperties("headless").equalsIgnoreCase("true")) {
				Map<String, Object> prefs = new HashMap<>();
				prefs.put("browser.show_hub_popup_on_download_start", Boolean.valueOf(false));
				prefs.put("download.prompt_for_download", Boolean.valueOf(false));
				option.setExperimentalOption("prefs", prefs);
				option.addArguments(new String[] { "--headless=chrome" });
			} else {
				setPath.put("download.prompt_for_download", Boolean.valueOf(false));
				setPath.put("directory_upgrade", Boolean.valueOf(true));
			}
			if (PropertyLoader.getProperties("executionMode").equalsIgnoreCase("remote")) {
				try {
					this.driver = (WebDriver) new RemoteWebDriver(new URL(PropertyLoader.getProperties("hubUrl")),
							(Capabilities) option);
				} catch (Exception e) {
					System.out.println("Issue with Remote WebDrver Intialisation");
					e.printStackTrace();
				}
			} else {
				this.driver = (WebDriver) new ChromeDriver(option);
			}
			this.driver.manage().window().maximize();
			this.executor = (JavascriptExecutor) this.driver;
			this.screenShot = (TakesScreenshot) this.driver;
			this.actions = new Actions(this.driver);
			this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10L));
			return this.driver;
		case "firefox":
			options = new FirefoxOptions();
			options.addArguments(new String[] { "--inprivate" });
			this.driver = (WebDriver) new FirefoxDriver(options);
			this.driver.manage().window().maximize();
			this.executor = (JavascriptExecutor) this.driver;
			this.screenShot = (TakesScreenshot) this.driver;
			this.actions = new Actions(this.driver);
			this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10L));
			return this.driver;
		case "edge":
			edgeOptions = new EdgeOptions();
			edgeOptions.addArguments(new String[] { "-inprivate" });
			this.driver = (WebDriver) new EdgeDriver(edgeOptions);
			this.driver.manage().window().maximize();
			this.executor = (JavascriptExecutor) this.driver;
			this.screenShot = (TakesScreenshot) this.driver;
			this.actions = new Actions(this.driver);
			this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10L));
			return this.driver;
		case "safari":
			safari = new SafariOptions();
			safariDriver = new SafariDriver(safari);
			this.driver.manage().window().maximize();
			this.executor = (JavascriptExecutor) this.driver;
			this.screenShot = (TakesScreenshot) this.driver;
			this.actions = new Actions(this.driver);
			this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10L));
			return this.driver;
		}
		System.out.println("please pass the right browser name......");
		this.driver.manage().window().maximize();
		this.executor = (JavascriptExecutor) this.driver;
		this.screenShot = (TakesScreenshot) this.driver;
		this.actions = new Actions(this.driver);
		this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10L));
		return this.driver;
	}

	public String takeScreenshot() {
		String SrcFile = (String) this.screenShot.getScreenshotAs(OutputType.BASE64);
		return SrcFile;
	}

	public void closeBrowser() {
		this.driver.quit();
	}

	public void highLightWebElementInScreenSchot(WebElement element, String locatorName) {
	}

	public void highLightWebElementInScreenSchot(WebElement element) {
	}

	public void unHighLightWebElementInScreenSchot(WebElement element) {
	}

	public String getUtcDate() {
		LocalDateTime utcTime = LocalDateTime.now(ZoneOffset.UTC);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
		return utcTime.format(formatter).toString();
	}

	public String getLocalDate() {
		LocalDateTime utcTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM");
		return utcTime.format(formatter).toString();
	}

	public String getUtcTime() {
		LocalDateTime utcTime = LocalDateTime.now(ZoneOffset.UTC);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return utcTime.format(formatter).toString();
	}

	public String getLocalTime() {
		LocalDateTime utcTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh-mm a");
		return utcTime.format(formatter).toString();
	}

	public String getUtcDateTimeStamp() {
		LocalDateTime utcTime = LocalDateTime.now(ZoneOffset.UTC);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy  HH:mm:ss");
		return utcTime.format(formatter).toString();
	}
}

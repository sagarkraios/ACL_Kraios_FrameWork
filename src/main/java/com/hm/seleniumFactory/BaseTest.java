package com.hm.seleniumFactory;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseTest {
	String methodName;

	String userID;

	String className;

	String clientName;

	String browser;

	String tracingID;

	public static String URL;

	int count = 1;

	List<List<String>> valueToWrite = new ArrayList<>();

	public static String USER = "";

	public static String ENV = "";

	private static final String OUTPUT_FOLDER = "./build/HtmlReport/"
			+ (new SimpleDateFormat("MMM dd")).format(new Date()) + "/";

	public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

	public static ThreadLocal<String> methodNameFlag = new ThreadLocal<>();

	public static ThreadLocal<Integer> iteration = new ThreadLocal<>();

	public static ThreadLocal<ExtentTest> logger = new ThreadLocal<>();

	public static ThreadLocal<SeleniumWrapper> seleniumWrapperManager = new ThreadLocal<>();

	@BeforeSuite(alwaysRun = true)
	public void reports() {
		Reporter.setReport(OUTPUT_FOLDER, getClass().getSimpleName());
		PropertyLoader.init_properties();
	}

	@Parameters({ "browser" })
	@BeforeMethod(alwaysRun = true)
	public void setup(@Optional("chrome") String browserName, Method method) throws InterruptedException {
		if (!PropertyLoader.init_properties().containsKey("isItApiExecution"))
			PropertyLoader.init_properties().setProperty("isItApiExecution", "false");
		if (!PropertyLoader.getProperties("isItApiExecution").toString().equalsIgnoreCase("true"))
			if (browserName != null) {
				PropertyLoader.init_properties().setProperty("browser", browserName);
				if (!PropertyLoader.init_properties().containsKey("smartTimeOut"))
					PropertyLoader.init_properties().setProperty("smartTimeOut", "15");
				this.browser = browserName;
				method.getParameterCount();
				SeleniumWrapper seleniumWrapper = new SeleniumWrapper(browserName);
				seleniumWrapperManager.set(seleniumWrapper);
			}
		initTest(method);
	}

	public void initTest(Method method) {
		String methodName = method.getName();
		String className = method.getClass().getName();
		method.getParameters();
		if (methodNameFlag.get() == null || !((String) methodNameFlag.get()).equals(methodName)) {
			logger.remove();
			System.out.println("Before Extent Test");
			ExtentReports rep = Reporter.extentReport;
			ExtentTest extentTest = rep.createTest(methodName, "Description");
			System.out.println("After Extent Test");
			if (test.get() != null)
				test.remove();
			test.set(extentTest);
			iteration.set(Integer.valueOf(1));
			logger.set(((ExtentTest) test.get()).createNode("Iteration " + iteration.get(), "Data"));
			methodNameFlag.set(methodName);
		} else {
			logger.remove();
			iteration.set(Integer.valueOf(((Integer) iteration.get()).intValue() + 1));
			logger.set(((ExtentTest) test.get()).createNode("Iteration " + iteration.get(), "Data"));
		}
	}

	public void testReport(ITestResult result) {
		((ExtentTest) logger.get()).pass(result.getThrowable(),
				MediaEntityBuilder.createScreenCaptureFromBase64String(
						((SeleniumWrapper) seleniumWrapperManager.get()).takeScreenshot(),
						result.getMethod().getMethodName()).build());
		if (result.getStatus() != 1)
			if (result.getStatus() == 2) {
				((ExtentTest) logger.get())
						.fail(MarkupHelper.createLabel(result.getThrowable() + " FAILED ", ExtentColor.RED))
						.addScreenCaptureFromBase64String(
								((SeleniumWrapper) seleniumWrapperManager.get()).takeScreenshot());
				((ExtentTest) logger.get()).getModel().setEndTime(getTime(result.getEndMillis()));
			} else if (result.getStatus() == 3) {
				((ExtentTest) logger.get())
						.fail(MarkupHelper.createLabel(result.getName() + " SKIPPED ", ExtentColor.ORANGE))
						.addScreenCaptureFromBase64String(
								((SeleniumWrapper) seleniumWrapperManager.get()).takeScreenshot());
				((ExtentTest) logger.get()).getModel().setEndTime(getTime(result.getEndMillis()));
			}
	}

	public  void testReportNew(ITestResult result, Hashtable<String, Object> data) {
		if (result.getStatus() == 1) {
			String userUrl = "https://" + data.get("XL_ENV").toString().trim() + "/login?id_token="
					+ data.get("UserIdToken").toString();
			String[][] tableData = { { "<b>UserID</b>", "<b>Role</b>", "<b>UserIDLoginUrl</b>", "<b>ClientGroup</b>" },
					{ data.get("EmailID").toString().trim(), data.get("Role").toString().trim(),
							"<a href='" + userUrl + "' target='_blank'>UserLogin</a>",
							data.get("ClientGroup").toString().trim() } };
			Markup marktableData = MarkupHelper.createTable(tableData);
			((ExtentTest) logger.get()).info(marktableData);

		} else if (result.getStatus() == 2) {
			String userUrl = "https://" + data.get("XL_ENV").toString().trim() + "/login?id_token="
					+ data.get("UserIdToken").toString();
			String[][] tableData = { { "<b>UserID</b>", "<b>Role</b>", "<b>UserIDLoginUrl</b>", "<b>ClientGroup</b>" },
					{ data.get("EmailID").toString().trim(), data.get("Role").toString().trim(),
							"<a href='" + userUrl + "' target='_blank'>UserLogin</a>",
							data.get("ClientGroup").toString().trim() } };
			Markup marktableData = MarkupHelper.createTable(tableData);
			((ExtentTest) logger.get()).info(marktableData);
			if (!PropertyLoader.getProperties("isItApiExecution").equals("true")) {

				((ExtentTest) logger.get()).fail(
						"<details><summary><b><font color=red>Exception Occured:Click to see</font></b ></summary>"
								+ result

										.getThrowable().toString().replaceAll(",", "<br>")
								+ "</details> \n");
				((ExtentTest) logger.get()).assignCategory(new String[] { "Fail" })
						.fail(MarkupHelper.createLabel(result.getMethod().getMethodName() + " FAILED ",
								ExtentColor.RED))

						.addScreenCaptureFromBase64String(
								((SeleniumWrapper) seleniumWrapperManager.get()).takeScreenshot());
			} else {
				((ExtentTest) logger.get()).fail(
						"<details><summary><b><font color=red>Exception Occured:Click to see</font></b ></summary>"
								+ result

										.getThrowable().toString().replaceAll(",", "<br>")
								+ "</details> \n");
				((ExtentTest) logger.get()).assignCategory(new String[] { "Fail" }).fail(
						MarkupHelper.createLabel(result.getMethod().getMethodName() + " FAILED ", ExtentColor.RED));
			}
			((ExtentTest) logger.get()).getModel().setEndTime(getTime(result.getEndMillis()));
		}
	}

	public void passTest(String validationMessage) {
		if (!PropertyLoader.getProperties("isItApiExecution").equals("true")) {

			if (PropertyLoader.isPassedScreenShotRequired.equalsIgnoreCase("true")) {

				((ExtentTest) logger.get()).assignCategory(new String[] { "Pass" })
						.pass(MarkupHelper.createLabel(validationMessage + " PASSED ", ExtentColor.GREEN))
						.addScreenCaptureFromBase64String(
								((SeleniumWrapper) seleniumWrapperManager.get()).takeScreenshot());
			} else {

				((ExtentTest) logger.get()).assignCategory(new String[] { "Pass" })
						.pass(MarkupHelper.createLabel(validationMessage + " PASSED ", ExtentColor.GREEN));
			}

		} else {
			((ExtentTest) logger.get()).assignCategory(new String[] { "Pass" })
					.pass(MarkupHelper.createLabel(validationMessage + " PASSED ", ExtentColor.GREEN));
		}
	}

	public void failTest(String validationMessage, Hashtable<String, Object> data) {
		throw new RuntimeException(validationMessage);
	}

	public void skipTest(String validationMessage) {
		((ExtentTest) logger.get()).skip(MarkupHelper.createLabel(validationMessage + " SKIPPED ", ExtentColor.ORANGE))
				.addScreenCaptureFromBase64String(((SeleniumWrapper) seleniumWrapperManager.get()).takeScreenshot());
	}

	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	@AfterMethod
	public void tearDown(ITestResult result) {
		Object[] params = result.getParameters();
		Object data = params[0];
		testReportNew(result, (Hashtable<String, Object>) data);
		if (!PropertyLoader.getProperties("isItApiExecution").equals("true"))
			seleniumWrapperManager.get().closeBrowser();
	}

	@AfterSuite
	public void suite() {
		Reporter.extentReport.flush();
	}
}

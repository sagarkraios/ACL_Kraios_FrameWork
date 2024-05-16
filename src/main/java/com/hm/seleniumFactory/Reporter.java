package com.hm.seleniumFactory;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.observer.ExtentObserver;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reporter {
  public static ExtentReports extentReport = null;
  
  public static void setReport(String folderPath, String filepath) {
    Path path = Paths.get(folderPath, new String[0]);
    if (!Files.exists(path, new java.nio.file.LinkOption[0]))
      try {
        Files.createDirectories(path, (FileAttribute<?>[])new FileAttribute[0]);
      } catch (IOException e) {
        e.printStackTrace();
      }  
    extentReport = new ExtentReports();
    System.out.println("Intialised Extent Reports");
    ExtentSparkReporter reporter = new ExtentSparkReporter(folderPath + filepath + (new SimpleDateFormat("HH mm ss")).format(new Date()) + ".html");
    reporter.config().setReportName("Kraios-ACL");
    extentReport.attachReporter(new ExtentObserver[] { (ExtentObserver)reporter });
    extentReport.setSystemInfo("System", "Windows");
    extentReport.setSystemInfo("Author", "Sagar");
    extentReport.setSystemInfo("Build#", "1.1");
  }
}

package com.exmple.utils.logs;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Reporter;


public class PrintLogs {

    Class className;
    Logger logger;

    public PrintLogs(Class clazz) {
        this.className = clazz;
        logger = LogManager.getLogger(clazz);
    }

    //constructor to initialize the report name and initialize the logger
    //call this only from the Base Test of your automation suit
    public PrintLogs(Class clazz, String reportName) {
        ExtentManager.reportName = reportName;
        this.className = clazz;
        logger = LogManager.getLogger(clazz);
    }

    public synchronized void info(String message) {
        try {
            ExtentTestManager.getTest().info(className.getName() + "--" + message);
        } catch (Exception exp) {
//            String testType = getTestType();
//            ExtentTest test = ExtentTestManager.getTest(testType);
//            if (test != null)
//                test.info(className.getName() + "--" + message);
        }
        logger.info(message);
    }

    public synchronized void info(String message, boolean isExtent) {
        if(isExtent) {
            try {
                ExtentTestManager.getTest().info(className.getName() + "--" + message);
            } catch (Exception exp) {
//            String testType = getTestType();
//            ExtentTest test = ExtentTestManager.getTest(testType);
//            if (test != null)
//                test.info(className.getName() + "--" + message);
            }
        }
        logger.info(message);
    }

    public synchronized void info(String message, boolean isExtent,boolean printclassname) {
        try {
            if(isExtent) {
                if (printclassname)
                    ExtentTestManager.getTest().info(className.getName() + "--" + message);
                else
                    ExtentTestManager.getTest().info(message);
            }
        } catch (Exception exp) {
//            String testType = getTestType();
//            ExtentTest test = ExtentTestManager.getTest(testType);
//            if (test != null)
//                test.info(className.getName() + "--" + message);
        }
        logger.info(message);
    }

    public synchronized void pass(String message) {
        try {
            ExtentTestManager.getTest().pass(className.getName() + "--" + message);
        } catch (Exception exp) {
//            String testType = getTestType();
//            ExtentTest test = ExtentTestManager.getTest(testType);
//            if (test != null)
//                test.pass(className.getName() + "--" + message);
        }
        logger.info(message);
    }

    public synchronized void error(String message) {
        try {
            ExtentTestManager.getTest().error("Failed......." + className.getName() + "--" + message);
        } catch (Exception exp) {
//            String testType = getTestType();
//            ExtentTest test = ExtentTestManager.getTest(testType);
//            if (test != null)
//                test.error("Failed......." + className.getName() + "--" + message);
        }
        logger.error("Failed..." + message);
    }

    public synchronized void warn(String message) {
        try {
            ExtentTestManager.getTest().warning(className.getName() + "--" + message);
        } catch (Exception exp) {
////            logger.error("Not able to print using extent manager, message " + exp.getMessage());
        }
        logger.warn(message);
    }

    public synchronized void debug(String message) {
        try {
            ExtentTestManager.getTest().debug(className.getName() + "--" + message);
        } catch (Exception exp) {
////            logger.error("Not able to print using extent manager, message " + exp.getMessage());
        }
        logger.debug(message);
    }

    public synchronized void fail(String message) {
        try {
            ExtentTestManager.getTest().fail(className.getName() + "--" + message);
        } catch (Exception exp) {
////            logger.error("Not able to print using extent manager, message " + exp.getMessage());
        }
        logger.error(message);
    }

    public synchronized String getTestType() {
        try {
            String test = (String) Reporter.getCurrentTestResult().getParameters()[0];
            return test;
        } catch (Exception exp) {
            return null;
        }
    }

    public synchronized void addScreenshot(String details,String path) {
        try {
            //ExtentTestManager.getTest().addScreenCaptureFromPath(path);
            ExtentTestManager.getTest().info(details, MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        } catch (Exception exp) {
        }
    }

    public synchronized void addTable(String [][]data) {
        try {
            ExtentTestManager.getTest().info(MarkupHelper.createTable(data));
            logger.info(data.toString());
        } catch (Exception exp) {
        }
    }

    public synchronized void infolabel(String message, String color) {
        try {
            ExtentTestManager.getTest().info(MarkupHelper.createLabel(message, ExtentColor.valueOf(color)));
            logger.info(message);
        } catch (Exception exp) {
        }
    }
}
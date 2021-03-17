package com.exmple.utils.logs;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.util.HashMap;
import java.util.Map;

public class ExtentTestManager {
    static Map<Integer, ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();

    private static ExtentReports extent = ExtentManager.getInstance();

    public static synchronized ExtentTest getTest() {
        return extentTestMap.get((int) (long) (Thread.currentThread().getId()));
    }

    public static synchronized void endTest() {
        extent.removeTest(extentTestMap.get((int) (long) (Thread.currentThread().getId())));
    }

    public static synchronized ExtentTest startTest(String testName) {
        return startTest(testName, "");
    }

    public static synchronized ExtentTest startTest(String testName, String desc) {
//        ExtentTest test = extent.startTest(testName +Thread.currentThread().getId(), desc);
        ExtentTest test = extent.createTest(testName, desc);
        extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);

        return test;
    }

//    public static synchronized ExtentTest getTest(String testType) {
//        Set set = extentTestMap.keySet();
//        Iterator<Integer> iterator = set.iterator();
//        while(iterator.hasNext()){
//            ExtentTest test = extentTestMap.get(iterator.next());
//            test.getName
//            if(test.getTest().getName().equalsIgnoreCase(testType))
//                return test;
//        }
//        return null;
//    }
}

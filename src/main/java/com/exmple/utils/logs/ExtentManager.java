package com.exmple.utils.logs;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
    private static ExtentReports instance;
    public static String reportName = "ApiAutomationReport.html";

    public static synchronized ExtentReports getInstance() {
        if (instance == null) {
            System.out.println(System.getProperty("user.dir"));

            instance = new ExtentReports();
            ExtentHtmlReporter htmlReporter  = new ExtentHtmlReporter(System.getProperty("user.dir") + "/" + reportName);
            System.out.println(System.getProperty("user.dir") + "/" + reportName);
//            htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
            htmlReporter.config().setTheme(Theme.DARK);
            htmlReporter.config().setDocumentTitle("Automation Report");
            htmlReporter.config().setReportName("Automated Test Cycle");
            instance.setSystemInfo("OS", System.getProperty("os.name"));
            instance.setSystemInfo("OS version", System.getProperty("os.version"));
            instance.setSystemInfo("OS Arch", System.getProperty("os.arch"));
            instance.setSystemInfo("User", System.getProperty("user.home"));
            instance.setSystemInfo("Java version", System.getProperty("java.version"));
            instance.setSystemInfo("Total memory " , String.valueOf(Runtime.getRuntime().totalMemory()/(1024)));
            instance.setSystemInfo("Used memory " , String.valueOf(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()/1024));
            instance.setSystemInfo("Free memory " , String.valueOf(Runtime.getRuntime().freeMemory()/(1024)));



//            htmlReporter.config().sesetTestViewChartLocation(ChartLocation.BOTTOM);
//            htmlReporter.config().setChartVisibilityOnOpen(true);
            htmlReporter.config().setEncoding("utf-8");
            instance.attachReporter(htmlReporter);
        }
        return instance;
    }
}


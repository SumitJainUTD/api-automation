package com.exmple.utils.resultsToEKS;

import com.exmple.utils.logs.PrintLogs;
import org.testng.ITestResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public class ELKHelper {
    ResultSender resultSender = new ResultSender();
    String summaryCSV = "src/S3Reports/Summary/SummaryCSV.csv";
    String build_number = System.getenv("BUILD_NUMBER");
    private String project_name = System.getenv("JOB_BASE_NAME");
    PrintLogs printLogs  = new PrintLogs(getClass());

    public String sendStatus(ITestResult iTestResult, TestStatus testStatus) {
        testStatus.setTestClass(iTestResult.getTestClass().getName());
        testStatus.setProjectName(project_name);
        System.out.println("Project Name : " + project_name);
        testStatus.setDescription(iTestResult.getMethod().getDescription());
        testStatus.setStatus("RUNNING");
        testStatus.setBuildNumber(Integer.parseInt(build_number));
//          testStatus.setBuildNumber(1);
        return resultSender.create(testStatus);
    }

    public synchronized void updateStatus(String status, HashMap<Long, TestStatus> map) {
        //   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TestStatus testStatus = map.get(Thread.currentThread().getId());
        testStatus.setStatus(status);
        LocalDateTime endTime = LocalDateTime.now().withNano(0);
        testStatus.setEndTime(endTime.toString());
        LocalDateTime startTime = LocalDateTime.parse(testStatus.getStartTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime temp = LocalDateTime.from(startTime);
        long seconds = temp.until(endTime, ChronoUnit.SECONDS);
        testStatus.setExecutionDuration(seconds);
        resultSender.update(testStatus, testStatus.getElastic_search_document());
    }
}

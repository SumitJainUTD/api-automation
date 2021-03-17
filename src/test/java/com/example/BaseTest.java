package com.example;

import com.exmple.utils.APIHelper;
import com.exmple.utils.Properties;
import com.exmple.utils.logs.ExtentManager;
import com.exmple.utils.logs.ExtentTestManager;
import com.exmple.utils.logs.PrintLogs;
import com.exmple.utils.resultsToEKS.ELKHelper;
import com.exmple.utils.resultsToEKS.TestStatus;
import com.exmple.utils.validators.ObjectValidator;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.time.LocalDateTime;
import java.util.HashMap;

public class BaseTest implements ITestListener {

    Properties properties = Properties.getInstance();
    public PrintLogs printLogs = new PrintLogs(getClass(), "Automation.html");
    public String AWS_KEY = properties.getAWS_KEY();
    public String AWS_SECRET = properties.getAWS_SECRET();
    public JSONObject testCase;
    public String testId;
    public APIHelper apiHelper = new APIHelper();
    public ExampleHelper exampleHelper = new ExampleHelper();
    public ObjectValidator objectValidator = new ObjectValidator();
    private String build_number = System.getenv("BUILD_NUMBER");
    private HashMap<Long, TestStatus> testStatusHashMap = new HashMap<>();
    private ELKHelper elkHelper = new ELKHelper();

    @Override
    public void onTestStart(ITestResult iTestResult) {
        try {
            testCase = (JSONObject) iTestResult.getParameters()[0];
            testId = (String) testCase.get("id");
        } catch (Exception exp) {
            try {
                testId = iTestResult.getParameters()[0].toString();
            } catch (Exception e) {
                testId = iTestResult.getMethod().getMethodName();
            }
        }
        TestStatus testStatus = new TestStatus();

        testStatus.setTestCase(testId);
        testStatus.setStartTime(LocalDateTime.now().withNano(0).toString());
        if (build_number != null)
            testStatus.setElastic_search_document(elkHelper.sendStatus(iTestResult, testStatus));
        testStatusHashMap.put(Thread.currentThread().getId(), testStatus);
        ExtentTestManager.startTest(testId);
        printLogs.pass("**************************  Start Execution of Test Case - " + testId + "  **************************");
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        if (build_number != null)
            elkHelper.updateStatus("PASS", testStatusHashMap);
        printLogs.pass(testId + " ---test passed");
        printLogs.pass("***********  End Execution of Test Case **************************");
        ExtentManager.getInstance().flush();
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        if (build_number != null)
            elkHelper.updateStatus("FAIL", testStatusHashMap);
        printLogs.fail(testId + " ---test failed");
        printLogs.fail("***********  End Execution of Test Case  **************************");
        ExtentManager.getInstance().flush();
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        if (build_number != null)
            elkHelper.updateStatus("SKIPPED", testStatusHashMap);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {

    }

    @Override
    public void onFinish(ITestContext iTestContext) {

    }
}
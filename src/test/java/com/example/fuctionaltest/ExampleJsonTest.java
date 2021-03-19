package com.example.fuctionaltest;

import com.example.BaseTest;
import com.exmple.utils.dataprovider.ExampleJSONDataProvider;
import com.exmple.utils.models.getemployees.Employee;
import com.exmple.utils.models.testdata.TestCase;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Listeners(BaseTest.class)
public class ExampleJsonTest extends BaseTest {

    @DataProvider(name="employeedata")
    public Iterator<Object[]> getEmployeeFromJSON(){
        ExampleJSONDataProvider odp = new ExampleJSONDataProvider();
        List<Object> testCases = odp.getTestCasesFromJSON(jsonTestPlan);
        List<Object[]> data = new ArrayList<>();
        for (int i = 0; i <testCases.size() ; i++) {
            TestCase testCase = (TestCase) testCases.get(i);
            data.add(new Object[]{testCase.getTest_name(), testCase});
        }
        return data.iterator();
    }

    @Test(dataProvider = "employeedata")
    public void createEmployeeJSONPositiveTest(String test_name, TestCase test_case){

        Employee employee = test_case.getEmployee();

        boolean result = true;
        Employee actual_response = null;
        ArrayList<String> ignoreFields = new ArrayList<>();
        ignoreFields.add("profile_image");
        result &= exampleHelper.createEmployee(employee)
                && (actual_response =  exampleHelper.getEmployee(employee))!=null;
//                    && objectValidator.validateObjects(employee, actual_response,ignoreFields);

        Assert.assertTrue(result, test_name + "failed");
    }
}

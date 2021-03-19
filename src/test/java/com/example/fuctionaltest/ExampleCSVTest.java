package com.example.fuctionaltest;


import com.example.BaseTest;
import com.exmple.utils.dataprovider.ExampleCSVDataProvider;
import com.exmple.utils.models.getemployees.Employee;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;

@Listeners(BaseTest.class)

public class ExampleCSVTest extends BaseTest{

    @DataProvider(name = "employeeCsvPositiveData")
    public Iterator<Object[]> employeeCsvPositiveDataDP() {
        ExampleCSVDataProvider edp = new ExampleCSVDataProvider();
        return edp.getTestCases(getClass(), Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test(dataProvider = "employeeCsvPositiveData")
    public void createEmployeeCSVPositiveTest(String test_name, String test_id, String employee_name,
                                      String employee_age, String employee_salary){
        int age = Integer.parseInt(employee_age);
        long salary = Long.parseLong(employee_salary);
        Employee employee = exampleHelper.getEmployeeObject(employee_name, age, salary);

        boolean result = true;
        Employee actual_response = null;
        ArrayList<String> ignoreFields = new ArrayList<>();
        ignoreFields.add("profile_image");
        result &= exampleHelper.createEmployee(employee)
                    && (actual_response =  exampleHelper.getEmployee(employee))!=null;
//                    && objectValidator.validateObjects(employee, actual_response,ignoreFields);

        Assert.assertTrue(result, test_name + "failed");
    }

    @DataProvider(name = "employeeCsvNegativeData")
    public Iterator<Object[]> employeeCsvNegativeDataDP() {
        ExampleCSVDataProvider edp = new ExampleCSVDataProvider();
        return edp.getTestCases(getClass(), Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Test(dataProvider = "employeeCsvNegativeData")
    public void createEmployeeCSVNegativeTest(String test_id, String test_name, String employee_name,
                                              String employee_age, String employee_salary){
        int age = Integer.parseInt(employee_age);
        long salary = Long.parseLong(employee_salary);
        Employee employee = exampleHelper.getEmployeeObject(employee_name, age, salary);

        boolean result = true;
        Employee actual_response = null;
        ArrayList<String> ignoreFields = new ArrayList<>();
        ignoreFields.add("profile_image");
        result &= !exampleHelper.createEmployee(employee);

        Assert.assertTrue(result, test_name + "failed");
    }
}

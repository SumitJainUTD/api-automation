package com.example;

import com.exmple.utils.APIHelper;
import com.exmple.utils.exceptions.ResponseValidationException;
import com.exmple.utils.models.createemployeeresponse.EmployeeResponse;
import com.exmple.utils.models.getemployees.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ExampleHelper {

    APIHelper apiHelper = new APIHelper();
    ObjectMapper objectMapper = new ObjectMapper();

    String getRandomString() {
        SimpleDateFormat sdf = new SimpleDateFormat("ddhhmmss");
        String id = sdf.format(new Date()) + new Random().nextInt(10000) + "";
        return id;
    }

    void deleteOldLogs(){
        ///api-automation/api-automation-logs-2020-03-09.log
        String fileName = "buyer-demand-logs";
        String path = "./";
        File dir = new File(path);
        for (File file: dir.listFiles()) {
            if (file.isDirectory())
                continue;
            if(file.getName().contains(fileName) && file.getName().contains(".log")) {
                System.out.println("Deleting log file: " + file.getName());
                file.delete();
            }
        }
    }

    public Employee getEmployeeObject(String emp_Name, int age, long salary){
        Employee employee = new Employee();
        employee.setEmployee_name(emp_Name + "_" + getRandomString());
        employee.setEmployee_age(age);
        employee.setEmployee_salary(salary);
        return employee;
    }

    public boolean createEmployee(Employee employee) {
        try {
            Response response = apiHelper.createEmployee(employee);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            EmployeeResponse emp_response = objectMapper.readValue(response.asString(), EmployeeResponse.class);
            employee.setId(emp_response.getData().getId());
            return true;
        } catch (ResponseValidationException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Employee getEmployee(Employee employee){
        try {
            Response response = apiHelper.getEmployee(employee.getId());
//            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Employee actual_emp = objectMapper.readValue(response.asString(), Employee.class);
            return actual_emp;
        } catch (ResponseValidationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}

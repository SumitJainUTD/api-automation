package com.exmple.utils;
import com.exmple.utils.logs.PrintLogs;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class APIHelper {

    Properties prop = Properties.getInstance();
    private String environment =System.getProperty("environment", "qa");
    private String EXAMPLE_V1_BASE_URL ="http://dummy.restapiexample.com/api/v1/";
    private String GET_ALL_EMPLOYEES = "/employees";
    private String GET_EMPLOYEE = "/employee";
    private String CREATE_EMPLOYEE ="/create";
    private String UPDATE_EMPLOYEE = "/update/";
    private String DELETE = "/delete/";
    PrintLogs printLogs = new PrintLogs(getClass());
    Response res;

}

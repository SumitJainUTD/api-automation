package com.exmple.utils;
import com.exmple.utils.exceptions.ResponseValidationException;
import com.exmple.utils.logs.PrintLogs;
import com.exmple.utils.models.getemployees.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class APIHelper {

    Properties prop = Properties.getInstance();
    private String environment =System.getProperty("environment", "qa");
    private String EXAMPLE_V1_BASE_URL ="http://dummy.restapiexample.com/api/v1";
    private String GET_ALL_EMPLOYEES = "/employees";
    private String GET_EMPLOYEE = "/employee";
    private String CREATE_EMPLOYEE ="/create";
    private String UPDATE_EMPLOYEE = "/update/";
    private String DELETE = "/delete/";
    PrintLogs printLogs = new PrintLogs(getClass());
    Response res;


    public Response getAllEmployees() throws ResponseValidationException {
        printLogs.info("Making API call " + EXAMPLE_V1_BASE_URL + GET_ALL_EMPLOYEES);
        Response response= RestAssured.given()
                .contentType(ContentType.JSON)
//                .header("Authorization", API_KEY)
                .get(EXAMPLE_V1_BASE_URL + GET_ALL_EMPLOYEES);
        printLogs.info("Response Code : "+response.getStatusCode());
        printLogs.info("Response : "+response.asString());
        if(response.getStatusCode()!=200)
            throw new ResponseValidationException("Response code validation failed !! Response received : "+response.asString());
        return response;
    }

    public Response getEmployee(int emp_id) throws ResponseValidationException {
        String URL = EXAMPLE_V1_BASE_URL + GET_EMPLOYEE+"/"+emp_id;
        printLogs.info("Making API call " + URL);
        Response response= RestAssured.given()
                .contentType(ContentType.JSON)
                .get(URL);
        printLogs.info("Response Code : "+response.getStatusCode());
        printLogs.info("Response : "+response.asString());
        if(response.getStatusCode()!=200)
            throw new ResponseValidationException("Response code validation failed !! Response received : "+response.asString());
        return response;
    }

    public Response createEmployee(Employee employee) throws ResponseValidationException, JsonProcessingException {
        String URL = EXAMPLE_V1_BASE_URL + CREATE_EMPLOYEE;
        printLogs.info("Making API call " + URL);

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(employee);
        System.out.println(body);
        Response response= RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .post(URL);
        printLogs.info("Response Code : "+response.getStatusCode());
        printLogs.info("Response : "+response.asString());
        if(response.getStatusCode()!=200 && response.getStatusCode()!=201)
            throw new ResponseValidationException("Response code validation failed !! Response received : "+response.asString());
        return response;
    }
}

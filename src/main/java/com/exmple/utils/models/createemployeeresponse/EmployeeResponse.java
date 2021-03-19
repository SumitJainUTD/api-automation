package com.exmple.utils.models.createemployeeresponse;

import com.exmple.utils.models.getemployees.Employee;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeResponse {

    @JsonProperty("status")
    private String status;
    @JsonProperty("data")
    private Employee data;
    @JsonProperty("message")
    private String message;

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("data")
    public Employee getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(Employee data) {
        this.data = data;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }
}

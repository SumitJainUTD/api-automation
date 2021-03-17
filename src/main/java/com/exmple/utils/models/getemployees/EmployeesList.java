package com.exmple.utils.models.getemployees;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EmployeesList {

    @JsonProperty("status")
    private String status;
    @JsonProperty("data")
    private List<Employee> data;
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
    public List<Employee> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<Employee> data) {
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

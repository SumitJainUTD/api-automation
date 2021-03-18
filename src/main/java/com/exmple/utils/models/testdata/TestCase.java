package com.exmple.utils.models.testdata;

import com.exmple.utils.models.getemployees.Employee;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TestCase {

    @JsonProperty("test_id")
    private Integer test_id;
    @JsonProperty("test_name")
    private String test_name;
    @JsonProperty("employee")
    private Employee employee;

    @JsonProperty("test_id")
    public Integer getTest_id() {
        return test_id;
    }

    @JsonProperty("test_id")
    public void setTest_id(Integer test_id) {
        this.test_id = test_id;
    }

    @JsonProperty("test_name")
    public String getTest_name() {
        return test_name;
    }

    @JsonProperty("test_name")
    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    @JsonProperty("employee")
    public Employee getEmployee() {
        return employee;
    }

    @JsonProperty("employee")
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}

package com.exmple.utils.models.getemployees;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Employee {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("employee_name")
    private String employee_name;
    @JsonProperty("employee_salary")
    private Long employee_salary;
    @JsonProperty("employee_age")
    private Integer employee_age;
    @JsonProperty("profile_image")
    private String profile_image;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("employee_name")
    public String getEmployee_name() {
        return employee_name;
    }

    @JsonProperty("employee_name")
    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    @JsonProperty("employee_salary")
    public Long getEmployee_salary() {
        return employee_salary;
    }

    @JsonProperty("employee_salary")
    public void setEmployee_salary(Long employee_salary) {
        this.employee_salary = employee_salary;
    }

    @JsonProperty("employee_age")
    public Integer getEmployee_age() {
        return employee_age;
    }

    @JsonProperty("employee_age")
    public void setEmployee_age(Integer employee_age) {
        this.employee_age = employee_age;
    }

    @JsonProperty("profile_image")
    public String getProfile_image() {
        return profile_image;
    }

    @JsonProperty("profile_image")
    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
}

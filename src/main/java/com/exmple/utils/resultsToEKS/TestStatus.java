package com.exmple.utils.resultsToEKS;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestStatus {
    @JsonProperty("testClass")
    private String testClass;

    @JsonProperty("projectName")
    private String projectName;

    @JsonProperty("testCase")
    private String testCase;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private String status;

    @JsonProperty("startTime")
    private String startTime;

    @JsonProperty("endTime")
    private String endTime;

    @JsonProperty("executionDuration")
    private Long executionDuration;

    @JsonProperty("buildNumber")
    private int buildNumber;

    private String elastic_search_document;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getExecutionDuration() {
        return executionDuration;
    }

    public void setExecutionDuration(Long executionDuration) {
        this.executionDuration = executionDuration;
    }

    public void setTestClass(String testClass) {
        this.testClass = testClass;
    }

    public void setBuildNumber(int buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getElastic_search_document() {
        return elastic_search_document;
    }

    public void setElastic_search_document(String elastic_search_document) {
        this.elastic_search_document = elastic_search_document;
    }

    public void setTestCase(String testCase) {
        this.testCase = testCase;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}

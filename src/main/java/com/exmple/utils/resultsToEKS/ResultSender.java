package com.exmple.utils.resultsToEKS;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;

public class ResultSender {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String ELASTIC_SEARCH_URL = "https://vpc-sdet-dash-aaaa.us-east-1.es.amazonaws.com";
    private static final String INDEX = "/automation_stats/api_automation"; // "http://localhost:9200"
    private String CONTENT_TYPE_VALUE = "application/json";

    public String create(final TestStatus testStatus) {
        String document_id = null;
        try {
            RestAssured.baseURI = ELASTIC_SEARCH_URL;
            Response response = RestAssured.given()
                    .body(mapper.writeValueAsString(testStatus))
                    .contentType(CONTENT_TYPE_VALUE)
                    .post(INDEX);
            System.out.println("Create : " + response.getBody().asString());
            document_id = new JSONObject(response.asString()).getString("_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document_id;
    }

    public void update(final TestStatus testStatus, String document_id) {
        try {
            String json = "{\n" +
                    "  \"doc\": { \"status\": \"" + testStatus.getStatus() + "\",\"endTime\":\"" + testStatus.getEndTime() + "\",\"executionDuration\":" + testStatus.getExecutionDuration() + " }\n" +
                    "}";
            System.out.println("Update json : " + json);
            RestAssured.baseURI = ELASTIC_SEARCH_URL;
            Response response = RestAssured.given()
                    .body(json)
                    .contentType(CONTENT_TYPE_VALUE)
                    .post(INDEX + "/" + document_id + "/_update");
            System.out.println("Update : " + response.getBody().asString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

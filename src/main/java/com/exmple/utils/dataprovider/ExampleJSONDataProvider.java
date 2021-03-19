package com.exmple.utils.dataprovider;

import com.exmple.utils.Properties;
import com.exmple.utils.logs.PrintLogs;
import com.exmple.utils.models.testdata.TestCase;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExampleJSONDataProvider {

    String testSuite = "src/test/resources/{{{plan}}}.json";
    ObjectMapper objectMapper = new ObjectMapper();
    PrintLogs printLogs = new PrintLogs(getClass());
    Properties properties = Properties.getInstance();

    public List<Object> getTestCasesFromJSON(String plan) {
        List<Object> testDataList = new ArrayList<>();
        try {
            String tempSuite = testSuite.replace("{{{plan}}}", plan);

            JSONArray jsonArray = null;
            try {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(tempSuite));
                jsonArray = (JSONArray) obj;

            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if (Boolean.parseBoolean(String.valueOf(jsonObject.get("execute")))) {
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    TestCase testCase = objectMapper.readValue(String.valueOf(jsonObject), TestCase.class);
                    testDataList.add(testCase);
                }
            }
            return testDataList;
        } catch (Exception exp){
            printLogs.error("Exception: " + exp.getMessage());
            return testDataList;
        }
    }
}

package com.exmple.utils.dataprovider;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ExampleCSVDataProvider {

    String TEST_DATA_LOCATION = "src/test/resources/test-data.csv";

    public Iterator<Object[]> getTestCases(Class clazz, String method) {
        String className = clazz.getSimpleName();
        List<Object[]> testCases = new ArrayList<>();
        try {
            CSVReader csvReader = new CSVReader(new FileReader(TEST_DATA_LOCATION));
            List<String[]> csvList = csvReader.readAll();

            for (int i = 0; i < csvList.size(); i++) {
                String[] rowData = csvList.get(i);
                if (rowData[0].equalsIgnoreCase("true") && rowData[1].equalsIgnoreCase(className)
                        && rowData[2].equalsIgnoreCase(method)) {
                    int range = getRange(rowData);
                    rowData = Arrays.copyOfRange(rowData, 3, range);
                    testCases.add(rowData);
                }
            }
        } catch (IOException exp) {
            exp.printStackTrace();
            return testCases.iterator();
        }
        return testCases.iterator();
    }

    private int getRange(String[] rowData) {
        for (int i = 0; i < rowData.length; i++) {
            if (rowData[i].trim().equals(""))
                return i;
        }
        return rowData.length;
    }
}

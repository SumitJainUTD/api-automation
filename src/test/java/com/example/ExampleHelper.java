package com.example;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ExampleHelper {

    String getRandomString() {
        SimpleDateFormat sdf = new SimpleDateFormat("ddhhmmss");
        String id = sdf.format(new Date()) + new Random().nextInt(10000) + "";
        return id;
    }

    void deleteOldLogs(){
        ///api-automation/api-automation-logs-2020-03-09.log
        String fileName = "buyer-demand-logs";
        String path = "./";
        File dir = new File(path);
        for (File file: dir.listFiles()) {
            if (file.isDirectory())
                continue;
            if(file.getName().contains(fileName) && file.getName().contains(".log")) {
                System.out.println("Deleting log file: " + file.getName());
                file.delete();
            }
        }
    }
}

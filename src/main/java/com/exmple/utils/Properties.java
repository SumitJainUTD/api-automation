package com.exmple.utils;


import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;

import java.io.File;
import java.io.IOException;

public class Properties {

    private static Properties properties = null;
    private String environment;
    private String mssql_host;
    private String mssql_user;
    private String mssql_password;
    private String AWS_KEY;
    private String AWS_SECRET;


    public static Properties getInstance() {
        String propertiesFileName;
        String environment = System.getProperty("env", "qa");
        propertiesFileName = "src/main/resources/example-staging-config.properties";
        if (environment != null) {
            switch (environment.toLowerCase()) {
                case "qa":
                    propertiesFileName = "src/main/resources/example-qa-config.properties";
                    break;
                case "stg":
                    propertiesFileName = "src/main/resources/example-staging-config.properties";
                    break;
                case "prod":
                    propertiesFileName = "src/main/resources/example-prod-config.properties";
                    break;
            }
        }

        if (properties == null) {
            JavaPropsMapper mapper = new JavaPropsMapper();
            try {
                properties = mapper.readValue(new File(propertiesFileName), Properties.class);
                properties.mssql_user = System.getProperty("mssql_user","");
                properties.mssql_password = System.getProperty("mssql_password","");
                properties.AWS_KEY = System.getProperty("AWS_KEY","");
                properties.AWS_SECRET = System.getProperty("AWS_SECRET","");
                properties.environment = environment;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return properties;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getMssql_host() {
        return mssql_host;
    }

    public void setMssql_host(String mssql_host) {
        this.mssql_host = mssql_host;
    }

    public String getMssql_user() {
        return mssql_user;
    }

    public void setMssql_user(String mssql_user) {
        this.mssql_user = mssql_user;
    }

    public String getMssql_password() {
        return mssql_password;
    }

    public void setMssql_password(String mssql_password) {
        this.mssql_password = mssql_password;
    }

    public String getAWS_KEY() {
        return AWS_KEY;
    }

    public void setAWS_KEY(String AWS_KEY) {
        this.AWS_KEY = AWS_KEY;
    }

    public String getAWS_SECRET() {
        return AWS_SECRET;
    }

    public void setAWS_SECRET(String AWS_SECRET) {
        this.AWS_SECRET = AWS_SECRET;
    }
}

package com.qa.providers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private Properties properties;

    public ConfigReader() throws IOException{
        properties=new Properties();
        InputStream ip=new FileInputStream("/Users/pallavik/gafg-selenium/src/main/resources/config.properties");
        properties.load(ip);
    }
    public String getAppurl()
    {
        return properties.getProperty("url");
    }
}

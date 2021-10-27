package com.qa.managers;

import com.qa.providers.ConfigReader;

import java.io.IOException;

public class FileReaderManager {

    private static FileReaderManager instance=new FileReaderManager();
    private static ConfigReader config;

    private FileReaderManager() {}

    public static FileReaderManager getInstance()
    {
        return instance;
    }

    public ConfigReader getConfig()
    {
        if(config==null)
        {
            try{
                config=new ConfigReader();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        return config;
    }
}

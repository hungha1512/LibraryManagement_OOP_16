package com.hunghq.librarymanagement.Global;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for managing application properties.
 * Loads properties from the application.properties file located in the resources directory
 * and provides methods to get and set properties dynamically.
 */
public class AppProperties {
    private static final Properties properties = new Properties();
    private static final String PROPERTIES_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/application.properties";

    static {
        try {
            InputStream inputStream = AppProperties.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_PATH);
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the value of a specified property key from the application properties.
     *
     * @param key the property key to retrieve
     * @return the value associated with the specified key, or null if the key is not found
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Sets a property value for the specified key and saves the change to the properties file.
     *
     * @param key   the property key to set
     * @param value the new value to associate with the specified key
     * @throws RuntimeException if there is an error writing to the properties file
     */
    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(PROPERTIES_FILE_PATH);
            properties.store(fileOutputStream, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

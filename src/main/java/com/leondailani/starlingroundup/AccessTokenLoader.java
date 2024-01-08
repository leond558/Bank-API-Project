package com.leondailani.starlingroundup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AccessTokenLoader {
    public static String loadAccessToken() {
        try (InputStream input = StarlingClient.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();

            if (input == null) {
                throw new IOException("Unable to find config.properties");
            }

            // Load a properties file from class path
            prop.load(input);

            // Get the property value
            return prop.getProperty("starling.access.token");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

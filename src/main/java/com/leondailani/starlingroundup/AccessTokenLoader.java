package com.leondailani.starlingroundup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * A utility class to handle use of the access token so that it remains secure.
 */
public class AccessTokenLoader {
    /**
     * A method for accessing the access token for a Starling Bank client from a secure
     * config.properties file.
     * @return The access token as a string.
     */
    public static String loadAccessToken() {
//        Get the access token from the config.properties file
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

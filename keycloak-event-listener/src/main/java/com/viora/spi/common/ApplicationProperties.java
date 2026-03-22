package com.viora.spi.common;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class ApplicationProperties {

    private static final String DEFAULT_PROPERTIES_PATH = "spi.properties";
    private static Properties properties;


    public Properties getProperties() {
        if (properties == null) {
            properties = readConfiguration();
        }
        return properties;
    }

    private Properties readConfiguration() {
        Properties properties = new Properties();

        try (InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream(DEFAULT_PROPERTIES_PATH)) {

            if (is == null) {
                throw new RuntimeException("spi.properties not found in classpath");
            }

            properties.load(is);
            log.info("Properties: {}", properties.entrySet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return properties;
    }


}

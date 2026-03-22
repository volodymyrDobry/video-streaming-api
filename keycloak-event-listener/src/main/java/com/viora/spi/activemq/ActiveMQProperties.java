package com.viora.spi.activemq;

import com.viora.spi.common.ApplicationProperties;

import java.util.Properties;

public class ActiveMQProperties {

    public static final String ACTIVEMQ_BROKER_URL;
    public static final String ACTIVEMQ_USERNAME;
    public static final String ACTIVEMQ_PASSWORD;
    public static final String ACTIVEMQ_QUEUE_USERS;

    static {
        Properties properties = new ApplicationProperties().getProperties();
        ACTIVEMQ_BROKER_URL = properties.getProperty("activemq.url");
        ACTIVEMQ_USERNAME = properties.getProperty("activemq.username");
        ACTIVEMQ_PASSWORD = properties.getProperty("activemq.password");
        ACTIVEMQ_QUEUE_USERS = properties.getProperty("activemq.users.queue");
    }
}

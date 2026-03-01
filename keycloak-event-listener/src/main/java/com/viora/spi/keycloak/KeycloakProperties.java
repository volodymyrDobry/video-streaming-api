package com.viora.spi.keycloak;

import com.viora.spi.common.ApplicationProperties;

import java.util.Properties;

public class KeycloakProperties {

    public static final String KEYCLOAK_REALM;
    public static final String KEYCLOAK_CLIENT_ID;
    public static final String KEYCLOAK_CLIENT_KEY;
    public static final String KEYCLOAK_URL;

    static {
        Properties properties = new ApplicationProperties().getProperties();
        KEYCLOAK_REALM = properties.getProperty("keycloak.realm");
        KEYCLOAK_CLIENT_ID = properties.getProperty("keycloak.client.id");
        KEYCLOAK_CLIENT_KEY = properties.getProperty("keycloak.client.key");
        KEYCLOAK_URL = properties.getProperty("keycloak.url");
    }

}

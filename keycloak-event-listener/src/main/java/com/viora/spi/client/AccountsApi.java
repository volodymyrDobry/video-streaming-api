package com.viora.spi.client;

import com.viora.spi.common.ApplicationProperties;
import lombok.Getter;

import java.util.Properties;

@Getter
public class AccountsApi {

    public static final String BASE_URL;
    public static final String REGISTER_ACCOUNT;

    static {
        Properties properties = new ApplicationProperties().getProperties();
        BASE_URL = properties.getProperty("application.accounts.url");
        REGISTER_ACCOUNT = properties.getProperty("application.accounts.register");
    }

}

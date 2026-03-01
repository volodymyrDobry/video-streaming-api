package com.viora.identityaccess.infrastructure.configs;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.viora.*")
@EntityScan(basePackages = "com.viora.*")
@PropertySource("classpath:application.yaml")
public class RepositoryConfigs {
}

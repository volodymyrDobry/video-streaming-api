package org.example.viorastreaminggateway.infrastructure.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "services")
@Data
public class ServiceUrlsProperties {
    private UserHistory userHistory = new UserHistory();
    private Content content = new Content();

    @Data
    public static class UserHistory {
        private String baseUrl = "http://localhost:8085";
    }

    @Data
    public static class Content {
        private String baseUrl = "http://localhost:8080";
        private String movieByImdbPath = "/api/v1/movies/by-imdb";
    }
}


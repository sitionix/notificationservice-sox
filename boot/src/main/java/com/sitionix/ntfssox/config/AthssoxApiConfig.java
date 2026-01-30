package com.sitionix.ntfssox.config;

import com.app_afesox.athssox.client.api.AuthApi;
import com.app_afesox.athssox.client.api.UserApi;
import com.app_afesox.athssox.client.invoker.ApiClient;
import lombok.Data;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Data
@Configuration
@ConfigurationProperties(prefix = "api.rest.client.athssox")
public class AthssoxApiConfig {

    private String basePath;

    @Bean("athssoxClient")
    public ApiClient athssoxClient(@Qualifier("athssoxRestTemplate") final RestTemplate restTemplate) {
        final ApiClient apiClient = new ApiClient(restTemplate);

        if (this.basePath != null && !this.basePath.isBlank()) {
            apiClient.setBasePath(this.basePath);
        }
        apiClient.addDefaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        apiClient.addDefaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return apiClient;
    }

    @Bean("athssoxRestTemplate")
    public RestTemplate athssoxRestTemplate(final RestTemplateBuilder restTemplateBuilder) {
        final HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(HttpClients.createDefault());
        return restTemplateBuilder
                .requestFactory(() -> new BufferingClientHttpRequestFactory(requestFactory))
                .build();
    }

    @Bean("athssoxAuthApi")
    public AuthApi authApi(@Qualifier("athssoxClient") final ApiClient apiClient) {
        return new AuthApi(apiClient);
    }

    @Bean("athssoxUserApi")
    public UserApi userApi(@Qualifier("athssoxClient") final ApiClient apiClient) {
        return new UserApi(apiClient);
    }
}

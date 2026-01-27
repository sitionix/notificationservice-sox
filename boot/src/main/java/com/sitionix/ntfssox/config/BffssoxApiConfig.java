package com.sitionix.ntfssox.config;

import com.app_afesox.bffssox.client.api.AuthApi;
import com.app_afesox.bffssox.client.api.UserApi;
import com.app_afesox.bffssox.client.invoker.ApiClient;
import lombok.Data;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Qualifier;
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
@ConfigurationProperties(prefix = "api.rest.client.bffssox")
public class BffssoxApiConfig {

    private String basePath;

    @Bean("bffssoxClient")
    public ApiClient bffssoxClient(@Qualifier("bffssoxRestTemplate") final RestTemplate restTemplate) {
        final ApiClient apiClient = new ApiClient(restTemplate);

        if (this.basePath != null && !this.basePath.isBlank()) {
            apiClient.setBasePath(this.basePath);
        }
        apiClient.addDefaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        apiClient.addDefaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return apiClient;
    }

    @Bean("bffssoxRestTemplate")
    public RestTemplate bffssoxRestTemplate() {
        final HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(
                        HttpClients.custom()
                                .disableAutomaticRetries()
                                .build()
                );
        return new RestTemplate(new BufferingClientHttpRequestFactory(requestFactory));
    }

    @Bean("bffssoxAuthApi")
    public AuthApi authApi(@Qualifier("bffssoxClient") final ApiClient apiClient) {
        return new AuthApi(apiClient);
    }

    @Bean("bffssoxUserApi")
    public UserApi userApi(@Qualifier("bffssoxClient") final ApiClient apiClient) {
        return new UserApi(apiClient);
    }
}

package com.makingsense.sap.purchase.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriTemplateHandler;

import java.time.Duration;

/**
 * Configure the {@link RestTemplate} bean along the application.
 */
@Configuration
public class RestTemplateConfiguration {

    @Value("${rest.config.connect.timeout:3000}")
    private long connectTimeout;

    @Value("${rest.config.connect.timeout:3000}")
    private long readTimeout;

    @Value("${sap.url.base:https://161.47.111.86:50000/b1s/v1/}")
    private String sapBaseUrl;

    /**
     * Creates a new {@link Bean} of {@link RestTemplate} to perform
     * REST http calls.
     *
     * @param builder   the {@link RestTemplateBuilder} to build the instance.
     * @return  a configured instance of {@link RestTemplate}.
     */
    @Bean(name = "restTemplate")
    public RestTemplate restTemplate(final RestTemplateBuilder builder,
                                     final ResponseErrorHandler restTemplateHandlerError) {

        final  UriTemplateHandler rootUriHandler = new DefaultUriBuilderFactory(sapBaseUrl);

        return builder
                .setConnectTimeout(Duration.ofMillis(connectTimeout))
                .setReadTimeout(Duration.ofMillis(readTimeout))
                .uriTemplateHandler(rootUriHandler)
                .errorHandler(restTemplateHandlerError)
                .build();
    }

}

package com.makingsense.sap.purchase.configuration;

import com.makingsense.sap.purchase.errors.InvalidCredentialsException;
import com.makingsense.sap.purchase.errors.SAPBadRequestException;
import com.makingsense.sap.purchase.errors.SAPInternalServerException;
import com.makingsense.sap.purchase.repositories.RestTemplateHandlerError;

import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriTemplateHandler;

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

    @Value("${sap.retry.max.retries:3}")
    private int maxRetries;

    @Value("${sap.retry.wait.between.retries.ms:500}")
    private long waitBetweenRetries;

    @Value("${sap.retry.backoff.multiplier:2}")
    private int backOffMultiplier;

    /**
     * Creates a new {@link Bean} of {@link RestTemplate} to perform
     * REST http calls.
     *
     * @param builder   the {@link RestTemplateBuilder} to build the instance.
     * @return  a configured instance of {@link RestTemplate}.
     */
    @Bean
    public RestTemplate restTemplate(final RestTemplateBuilder builder,
                                     final RestTemplateHandlerError restTemplateHandlerError) {

        final  UriTemplateHandler rootUriHandler = new DefaultUriBuilderFactory(sapBaseUrl);


        return builder
                .setConnectTimeout(Duration.ofMillis(connectTimeout))
                .setReadTimeout(Duration.ofMillis(readTimeout))
                .uriTemplateHandler(rootUriHandler)
                .errorHandler(restTemplateHandlerError)
                .build();
    }


    @Bean("sapRetry")
    public Retry sapRetry() {
        final IntervalFunction intervalFn =
                IntervalFunction.ofExponentialBackoff(waitBetweenRetries, backOffMultiplier);

        final RetryConfig config = RetryConfig.custom()
                .maxAttempts(maxRetries)
                .waitDuration(Duration.ofMillis(waitBetweenRetries))
                .retryExceptions(SAPInternalServerException.class,
                        SAPBadRequestException.class,
                        TimeoutException.class,
                        Exception.class)
                .ignoreExceptions(InvalidCredentialsException.class)
                .intervalFunction(intervalFn)
                .build();

        final RetryRegistry registry = RetryRegistry.of(config);

        final Retry retryWithDefaultConfig = registry.retry("sapRetry");

        return retryWithDefaultConfig;
    }

}

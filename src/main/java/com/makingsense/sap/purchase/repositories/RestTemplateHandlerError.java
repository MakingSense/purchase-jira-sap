package com.makingsense.sap.purchase.repositories;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

import java.io.IOException;

/**
 *
 */
@Component
public class RestTemplateHandlerError implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return (
                clientHttpResponse.getStatusCode().series() == CLIENT_ERROR
                        || clientHttpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        HttpStatus statusCode = clientHttpResponse.getStatusCode();
        if (statusCode == HttpStatus.UNAUTHORIZED) {
        }


    }
}

package com.makingsense.sap.purchase.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.makingsense.sap.purchase.errors.InvalidCredentialsException;
import com.makingsense.sap.purchase.errors.SAPBadRequestException;
import com.makingsense.sap.purchase.errors.SAPInternalServerException;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

/**
 * Handler error for the REST errors when communicating to an external service.
 */
@Component
public class RestTemplateHandlerError extends DefaultResponseErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateHandlerError.class);

    private final ObjectMapper mapper;

    public RestTemplateHandlerError(final ObjectMapper mapper) {
        this.mapper = mapper;
        this.mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    public void handleError(final ClientHttpResponse clientHttpResponse) throws IOException {
        final HttpStatus statusCode = clientHttpResponse.getStatusCode();
        final String errorMessage = mapper.readValue(clientHttpResponse.getBody(), Map.class).toString();

        if (statusCode.series() == HttpStatus.Series.SERVER_ERROR) {
            LOGGER.error("SAP service responded with an internal error. Error = [{}].",
                    errorMessage);
            throw new SAPInternalServerException("Internal Server Error returned by SAP.");
        } else if (statusCode.series() == HttpStatus.Series.CLIENT_ERROR) {
            handleClientErrors(statusCode, errorMessage);
        }

    }

    private void handleClientErrors(final HttpStatus statusCode ,
                                    final String errorMessage) throws IOException {
        if (statusCode == HttpStatus.UNAUTHORIZED) {
            LOGGER.error("There was an issue when authenticating to SAP services. Error = [{}].",
                    errorMessage);
            throw new InvalidCredentialsException("The credentials provided are invalid.");
        } else {
            LOGGER.error("The request to SAP is malformed. Error = [{}].", errorMessage);
            throw new SAPBadRequestException("Bad request when communicating with SAP service.");
        }
    }

}

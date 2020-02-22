package com.money.transfer.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BusinessException extends Exception implements ExceptionMapper<BusinessException> {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessException.class);

    private ErrorResponse errorResponse;

    public BusinessException() {
    }

    public BusinessException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }

    @Override
    public Response toResponse(BusinessException exception) {
        LOGGER.info(exception.getErrorResponse().getMessage());
        return Response.status(exception.errorResponse.getStatusCode())
                .entity(exception.errorResponse)
                .type("application/json").build();
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}

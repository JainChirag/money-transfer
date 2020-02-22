package com.money.tansfer.exception;

import com.money.transfer.exception.BusinessException;
import com.money.transfer.exception.ErrorResponse;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class BusinessExceptionTest {

    @Test
    public void toResponse_GivenAnException_ShouldReturnResponseCorrectly() {
        int testStatusCode = 500;
        String testMessage = "Error occurred during processing";
        ErrorResponse testFailureResponse = new ErrorResponse(testStatusCode, testMessage);

        BusinessException businessException = new BusinessException(testFailureResponse);

        Response response = businessException.toResponse(businessException);

        assertEquals("application", response.getMediaType().getType());
        assertEquals("json", response.getMediaType().getSubtype());
        assertEquals(testFailureResponse, response.getEntity());
        assertEquals(testStatusCode, response.getStatus());

    }
}

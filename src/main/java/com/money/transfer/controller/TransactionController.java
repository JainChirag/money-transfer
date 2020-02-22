package com.money.transfer.controller;

import com.money.transfer.dto.TransactionDTO;
import com.money.transfer.exception.BusinessException;
import com.money.transfer.service.TransactionService;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/transfer")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @POST
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public TransactionDTO transact(@Valid TransactionDTO transactionDTO) throws BusinessException {
        LOGGER.info("Received transfer request: {}", transactionDTO.toString());
        return transactionService.processTransaction(transactionDTO);
    }
}

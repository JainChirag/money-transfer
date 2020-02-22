package com.money.transfer.controller;

import com.money.transfer.dto.AccountDTO;
import com.money.transfer.exception.BusinessException;
import com.money.transfer.service.AccountService;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Path("/{accountId}")
    @GET
    @UnitOfWork
    public AccountDTO getAccount(/*@UnwrapValidatedValue*/ @NotNull @Min(0) @PathParam("accountId") long accountId) throws BusinessException {
        return accountService.get(accountId);
    }

    @POST
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public AccountDTO createAccount(@Valid AccountDTO account) {
        return accountService.create(account);
    }
}

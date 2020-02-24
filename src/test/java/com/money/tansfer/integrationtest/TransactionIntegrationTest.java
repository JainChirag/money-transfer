package com.money.tansfer.integrationtest;

import com.money.transfer.ApplicationConfiguration;
import com.money.transfer.MoneyTransferApplication;
import com.money.transfer.dto.AccountDTO;
import com.money.transfer.dto.TransactionDTO;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.client.HttpClientConfiguration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.dropwizard.util.Duration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertEquals;


public class TransactionIntegrationTest {

    private static Client testJerseyClient;
    private static String transferResourcePath;
    private static String accountResourcePath;

    @Rule
    public final DropwizardAppRule<ApplicationConfiguration> appRule = new DropwizardAppRule<>(MoneyTransferApplication.class,
            ResourceHelpers.resourceFilePath("config_test.yml"));

    @Before
    public void setup() {
        HttpClientConfiguration httpClientConfiguration = new HttpClientConfiguration();
        httpClientConfiguration.setTimeout(Duration.milliseconds(4000));

        HttpClientBuilder clientBuilder = new HttpClientBuilder(appRule.getEnvironment());
        clientBuilder.using(httpClientConfiguration);

        JerseyClientBuilder builder = new JerseyClientBuilder(appRule.getEnvironment());
        builder.setApacheHttpClientBuilder(clientBuilder);
        testJerseyClient = builder.build("test client");

        String baseURL = String.format("http://localhost:%d/", appRule.getLocalPort());
        transferResourcePath = baseURL + "transfer/";
        accountResourcePath = baseURL + "account/";
    }

    @Test
    public void invalidSourceAccountTest() {
        AccountDTO destinationAccountDTO = new AccountDTO("Destination Account Name", BigDecimal.valueOf(1000));

        Response destinationAccountCreationResponse = createAccount(destinationAccountDTO);

        AccountDTO destinationAccountCreated = destinationAccountCreationResponse.readEntity(AccountDTO.class);

        TransactionDTO transferRequest = new TransactionDTO(1234, destinationAccountCreated.getAccountNumber(), BigDecimal.valueOf(200));

        Response transactionResponse = performTransaction(transferRequest);
        assertThat(transactionResponse.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void invalidDestinationAccountTest() {
        AccountDTO sourceAccountDTO = new AccountDTO("Source Account Name", BigDecimal.valueOf(1000));

        Response sourceAccountCreationResponse = createAccount(sourceAccountDTO);

        AccountDTO sourceAccountCreated = sourceAccountCreationResponse.readEntity(AccountDTO.class);

        TransactionDTO transactionRequestDTO = new TransactionDTO(sourceAccountCreated.getAccountNumber(), 1234, BigDecimal.valueOf(200));

        Response transferResponse = performTransaction(transactionRequestDTO);
        assertThat(transferResponse.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void insufficientSourceAccountBalanceTest() {
        AccountDTO testSourceAccount = new AccountDTO("Source Account Name", BigDecimal.valueOf(100));
        AccountDTO testDestinationAccount = new AccountDTO("Destination Account Name", BigDecimal.valueOf(200));

        Response sourceAccountCreationResponse = createAccount(testSourceAccount);
        Response destinationAccountCreationResponse = createAccount(testDestinationAccount);

        AccountDTO sourceAccountCreated = sourceAccountCreationResponse.readEntity(AccountDTO.class);
        AccountDTO destinationAccountCreated = destinationAccountCreationResponse.readEntity(AccountDTO.class);

        TransactionDTO transactionRequestDTO = new TransactionDTO(sourceAccountCreated.getAccountNumber(), destinationAccountCreated.getAccountNumber(), BigDecimal.valueOf(150));

        Response transactionResponse = performTransaction(transactionRequestDTO);
        assertThat(transactionResponse.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void validTransferRequestTest() {
        AccountDTO testSourceAccount = new AccountDTO("Source Account Name", BigDecimal.valueOf(100));
        AccountDTO testDestinationAccount = new AccountDTO("Destination Account Name", BigDecimal.valueOf(200));

        Response sourceAccountCreationResponse = createAccount(testSourceAccount);
        Response destinationAccountCreationResponse = createAccount(testDestinationAccount);

        AccountDTO sourceAccountCreated = sourceAccountCreationResponse.readEntity(AccountDTO.class);
        AccountDTO destinationAccountCreated = destinationAccountCreationResponse.readEntity(AccountDTO.class);

        TransactionDTO transactionRequestDTO = new TransactionDTO(sourceAccountCreated.getAccountNumber(), destinationAccountCreated.getAccountNumber(), BigDecimal.valueOf(50));

        Response transactionResponse = performTransaction(transactionRequestDTO);
        assertThat(transactionResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        Response updatedSourceAccountResponse = fetchAccount(sourceAccountCreated.getAccountNumber());
        Response updatedDestinationAccountResponse = fetchAccount(destinationAccountCreated.getAccountNumber());

        AccountDTO updatedSourceAccount = updatedSourceAccountResponse.readEntity(AccountDTO.class);
        AccountDTO updatedDestinationAccount = updatedDestinationAccountResponse.readEntity(AccountDTO.class);

        BigDecimal expectedSourceAccountBalance = testSourceAccount.getBalance().subtract(transactionRequestDTO.getAmount());
        BigDecimal expectedDestinationAccountBalance = testDestinationAccount.getBalance().add(transactionRequestDTO.getAmount());

        assertEquals(0, expectedSourceAccountBalance.compareTo(updatedSourceAccount.getBalance()));
        assertEquals(0, expectedDestinationAccountBalance.compareTo(updatedDestinationAccount.getBalance()));
    }


    private Response performTransaction(TransactionDTO transferDTO) {
        return testJerseyClient.target(transferResourcePath)
                .request()
                .post(Entity.json(transferDTO));
    }

    private Response createAccount(AccountDTO accountDTO) {
        return testJerseyClient.target(accountResourcePath)
                .request()
                .post(Entity.json(accountDTO));
    }

    private Response fetchAccount(long accountId) {
        return testJerseyClient.target(accountResourcePath + accountId)
                .request()
                .get();
    }
}
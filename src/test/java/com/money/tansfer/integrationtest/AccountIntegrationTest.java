package com.money.tansfer.integrationtest;

import com.money.transfer.ApplicationConfiguration;
import com.money.transfer.MoneyTransferApplication;
import com.money.transfer.dto.AccountDTO;
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


public class AccountIntegrationTest {

    private static Client testJerseyClient;
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
        accountResourcePath = baseURL + "account/";
    }


    @Test
    public void validAccountCreationRequestTest() {
        AccountDTO testAccountDTO = new AccountDTO("Test Account Name", BigDecimal.valueOf(1001.32));
        AccountDTO testAccountCreated = new AccountDTO("Test Account Name", BigDecimal.valueOf(1001.32));
        testAccountCreated.setAccountNumber(1);

        Response accountCreationResponse = createAccount(testAccountDTO);

        assertThat(accountCreationResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(accountCreationResponse.readEntity(AccountDTO.class)).isEqualToComparingFieldByField(testAccountCreated);
    }

    @Test
    public void invalidAccountCreationRequestTest() {
        AccountDTO testAccount = new AccountDTO("Test Account Name", BigDecimal.valueOf(-100.02));

        Response accountCreationResponse = createAccount(testAccount);

        assertThat(accountCreationResponse.getStatus()).isEqualTo(422);
    }


    @Test
    public void accountFetchRequestTest() {
        AccountDTO testAccount = new AccountDTO("Test Account Name", BigDecimal.valueOf(100.02));

        Response accountCreationResponse = createAccount(testAccount);

        AccountDTO createdAccount = accountCreationResponse.readEntity(AccountDTO.class);

        long createdAccountID = 1;

        Response fetchedAccountResponse = testJerseyClient.target(accountResourcePath + createdAccountID)
                .request()
                .get();

        assertThat(fetchedAccountResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(fetchedAccountResponse.readEntity(AccountDTO.class)).isEqualTo(createdAccount);
    }

    @Test
    public void fetchNonExistingAccountTest() {

        long nonExistingAccountId = 1;

        Response fetchAccountResponse = testJerseyClient.target(accountResourcePath + nonExistingAccountId)
                .request()
                .get();

        assertThat(fetchAccountResponse.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }


    private Response createAccount(AccountDTO accountDTO) {
        return testJerseyClient.target(accountResourcePath)
                .request()
                .post(Entity.json(accountDTO));
    }
}

package com.money.tansfer.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.money.transfer.dto.AccountDTO;
import io.dropwizard.jackson.Jackson;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class AccountDTOTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private static AccountDTO testAccountDto;
    private static String accountDTOJson;


    @BeforeClass
    public static void setup() throws IOException {
        String testName = "Chirag Jain";
        BigDecimal testAmount = BigDecimal.valueOf(1010.35);
        testAccountDto = new AccountDTO(testName, testAmount);
        File file = new File(Objects.requireNonNull(AccountDTOTest.class.getClassLoader().getResource("data/accountDto.json")).getFile());
        accountDTOJson = new String(Files.readAllBytes(file.toPath()));
    }

    @Test
    public void jsonSerializationTest() throws Exception {
        AccountDTO deserializedAccountDTO = MAPPER.readValue(accountDTOJson, AccountDTO.class);
        final String expectedJsonString = MAPPER.writeValueAsString(deserializedAccountDTO);
        final String actualSerializedJsonString = MAPPER.writeValueAsString(testAccountDto);
        assertEquals(expectedJsonString, actualSerializedJsonString);
    }

    @Test
    public void jsonDeserializationTest() throws Exception {
        AccountDTO deserializedAccountDTO = MAPPER.readValue(accountDTOJson, AccountDTO.class);
        assertEquals(testAccountDto, deserializedAccountDTO);
    }
}
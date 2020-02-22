package com.money.tansfer.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.money.transfer.dto.TransactionDTO;
import io.dropwizard.jackson.Jackson;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TransactionDTOTest {
    private static final ObjectMapper OBJECT_MAPPER = Jackson.newObjectMapper();

    private static TransactionDTO testTransactionDTO;
    private static String transactionDTOJson;

    @BeforeClass
    public static void setup() throws IOException {
        long sourceAccountNumber = 29L;
        long destinationAccountNumber = 39L;
        BigDecimal testAmount = BigDecimal.valueOf(100.01);
        testTransactionDTO = new TransactionDTO(sourceAccountNumber, destinationAccountNumber, testAmount);
        File file = new File(Objects.requireNonNull(TransactionDTOTest.class.getClassLoader().getResource("data/transactionDto.json")).getFile());
        transactionDTOJson = new String(Files.readAllBytes(file.toPath()));
    }

    @Test
    public void jsonSerializationTest() throws Exception {
        TransactionDTO deserializedTransactionDTO = OBJECT_MAPPER.readValue(transactionDTOJson, TransactionDTO.class);
        final String expectedJsonString = OBJECT_MAPPER.writeValueAsString(deserializedTransactionDTO);
        final String actualSerializedJsonString = OBJECT_MAPPER.writeValueAsString(testTransactionDTO);
        assertEquals(expectedJsonString, actualSerializedJsonString);
    }

    @Test
    public void jsonDeserializationTest() throws Exception {
        TransactionDTO deserializedTransactionDTO = OBJECT_MAPPER.readValue(transactionDTOJson, TransactionDTO.class);
        assertEquals(testTransactionDTO, deserializedTransactionDTO);
    }

}
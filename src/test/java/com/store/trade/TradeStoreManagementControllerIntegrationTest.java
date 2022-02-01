package com.store.trade;

import com.store.trade.constant.TradeMessagesConstants;
import com.store.trade.dto.*;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = TradeStoreApplication.class)
@RunWith(SpringRunner.class)
public class TradeStoreManagementControllerIntegrationTest {

    final private static int port = 8080;
    final private static String baseUrl = "http://localhost:";

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public void setUp(){
        TradeStoreRequest tradeStoreRequest = new TradeStoreRequest(new TradeStore("T1",1,"CP-4","B3","2022-05-20","2022-05-20","N"));

        ResponseEntity<TradeStoreResponse> responseEntity = this.restTemplate.withBasicAuth("test", "test")
                .postForEntity(baseUrl + port + "/trades",tradeStoreRequest, TradeStoreResponse.class);
    }

    @Test
    public void getTrades() {

        ResponseEntity<TradeStoreResponse> responseEntity = this.restTemplate.withBasicAuth("test", "test")
                .getForEntity(baseUrl + port + "/trades", TradeStoreResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Status.SUCCESS, responseEntity.getBody().getResponseStatus().getStatus());
        assertNotNull(responseEntity.getBody().getTradeStore());
    }

    @Test
    public void getTradeById() {

        ResponseEntity<TradeStoreResponse> responseEntity = this.restTemplate.withBasicAuth("test", "test")
                .getForEntity(baseUrl + port + "/trades/T1", TradeStoreResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Status.SUCCESS, responseEntity.getBody().getResponseStatus().getStatus());
        assertNotNull(responseEntity.getBody().getTradeStore());
    }

    @Test
    public void getTradeByIdNotFoundException() {

        ResponseEntity<TradeStoreResponse> responseEntity = this.restTemplate.withBasicAuth("test", "test")
                .getForEntity(baseUrl + port + "/trades/T2", TradeStoreResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Status.FAILURE, responseEntity.getBody().getResponseStatus().getStatus());
        assertNull(responseEntity.getBody().getTradeStore());
        assertEquals(TradeMessagesConstants.TRADE_NOT_FOUND_MSG, responseEntity.getBody().getResponseStatus().getMessageList().get(0).getMessage());

    }

    @Test
    public void saveTrade() {

        TradeStoreRequest tradeStoreRequest = new TradeStoreRequest(new TradeStore("T1",1,"CP-4","B3","2022-05-20","2022-05-20","N"));

        ResponseEntity<TradeStoreResponse> responseEntity = this.restTemplate.withBasicAuth("test", "test")
                .postForEntity(baseUrl + port + "/trades",tradeStoreRequest, TradeStoreResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Status.SUCCESS, responseEntity.getBody().getResponseStatus().getStatus());
        assertNotNull(responseEntity.getBody().getTradeStore());
    }



}

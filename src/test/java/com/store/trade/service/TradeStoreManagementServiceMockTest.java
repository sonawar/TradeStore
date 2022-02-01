package com.store.trade.service;

import com.store.trade.constant.TradeMessagesConstants;
import com.store.trade.dto.Status;
import com.store.trade.dto.TradeStore;
import com.store.trade.dto.TradeStoreRequest;
import com.store.trade.entity.Trade;
import com.store.trade.repository.TradeStoreManagementRepository;
import com.store.trade.service.TradeStoreManagementService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeStoreManagementServiceMockTest {

    @Autowired
    private TradeStoreManagementService tradeStoreManagementService;

    @MockBean
    private TradeStoreManagementRepository tradeStoreManagementRepository;

    @Test
    public void getTradesTest() {
        when(tradeStoreManagementRepository.findAll()).thenReturn(Stream
                .of(new Trade("T1",2,"CP-4","B3","2022-05-20","2022-05-20","N")).collect(Collectors.toList()));
        assertEquals(1, tradeStoreManagementService.getTrades().getTradeStore().size());
    }

    @Test
    public void getTradeByIdTest() {
        when(tradeStoreManagementRepository.findById("T1")).thenReturn(Optional
                .of(new Trade("T1",2,"CP-4","B3","2022-05-20","2022-05-20","N")));
        assertEquals(1, tradeStoreManagementService.getTrade("T1").getTradeStore().size());
    }

    @Test
    public void getTradeByIdExceptionTest() {
        when(tradeStoreManagementRepository.findById("T1")).thenReturn(Optional.empty());
        assertNull(tradeStoreManagementService.getTrade("T1").getTradeStore());
        assertEquals(TradeMessagesConstants.TRADE_NOT_FOUND_MSG, tradeStoreManagementService.getTrade("T1").getResponseStatus().getMessageList().get(0).getMessage());

    }

    @Test
    public void saveTradeLowerVersionTradeTest() throws ParseException {

        when(tradeStoreManagementRepository.findById("T1")).thenReturn(Optional
                .of(new Trade("T1",2,"CP-4","B3","2022-05-20","2022-05-20","N")));

        TradeStoreRequest tradeStoreRequest = new TradeStoreRequest(new TradeStore("T1",1,"CP-4","B3","2022-05-20","2022-05-20","N"));
        assertEquals(TradeMessagesConstants.LOWER_VERSION_MSG,tradeStoreManagementService.saveTrade(tradeStoreRequest).getResponseStatus().getMessageList().get(0).getMessage());
    }

    @Test
    public void saveTradeMaturityDateTest() throws ParseException {

        when(tradeStoreManagementRepository.findById("T1")).thenReturn(Optional
                .of(new Trade("T1",2,"CP-4","B3","2022-05-20","2022-05-20","N")));

        TradeStoreRequest tradeStoreRequest = new TradeStoreRequest(new TradeStore("T1",2,"CP-4","B3","2021-05-20","2022-05-20","N"));
        assertEquals(TradeMessagesConstants.MATURITY_DATE_MSG,tradeStoreManagementService.saveTrade(tradeStoreRequest).getResponseStatus().getMessageList().get(0).getMessage());
    }

    @Test
    public void saveTradeTest() throws ParseException {

        when(tradeStoreManagementRepository.findById("T1")).thenReturn(Optional.empty());
        TradeStoreRequest tradeStoreRequest = new TradeStoreRequest(new TradeStore("T1",2,"CP-4","B3","2021-05-20","2022-05-20","N"));
        assertEquals(Status.SUCCESS,tradeStoreManagementService.saveTrade(tradeStoreRequest).getResponseStatus().getStatus());
    }
}

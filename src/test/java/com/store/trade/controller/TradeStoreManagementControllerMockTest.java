package com.store.trade.controller;

import com.store.trade.constant.TradeMessagesConstants;
import com.store.trade.dto.*;
import com.store.trade.service.TradeStoreManagementService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class TradeStoreManagementControllerMockTest {
    private MockMvc mockMvc;

    @Mock
    private TradeStoreManagementService tradeStoreManagementService;

    @InjectMocks
    private TradeStoreManagementController tradeStoreManagementController;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(tradeStoreManagementController)
                .build();
    }


    @Test
    public void getTradesTest() throws Exception {
        List<TradeStore> tradeStoreList = Stream.of(new TradeStore("T1",2,"CP-4","B3","2022-05-20","2022-05-20","N")).collect(Collectors.toList());
        List<Message> messageList = new ArrayList<>();
        Mockito.when(tradeStoreManagementService.getTrades()).thenReturn(
                new TradeStoreResponse(tradeStoreList,new ResponseStatus(Status.SUCCESS,messageList)));

        mockMvc.perform(MockMvcRequestBuilders.get("/trades").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


    }

    @Test
    public void getTradeByIdTest() throws Exception {
        List<TradeStore> tradeStoreList = Stream.of(new TradeStore("T1",2,"CP-4","B3","2022-05-20","2022-05-20","N")).collect(Collectors.toList());
        List<Message> messageList = new ArrayList<>();
        Mockito.when(tradeStoreManagementService.getTrade("T1")).thenReturn(
                new TradeStoreResponse(tradeStoreList,new ResponseStatus(Status.SUCCESS,messageList)));

        mockMvc.perform(MockMvcRequestBuilders.get("/trades/T1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


    }

    @Test
    public void saveTradeTest() throws Exception {
        TradeStoreRequest tradeStoreRequest = new TradeStoreRequest(new TradeStore("T1",1,"CP-4","B3","2022-05-20","2022-05-20","N"));


        List<TradeStore> tradeStoreList = Stream.of(new TradeStore("T1",2,"CP-4","B3","2022-05-20","2022-05-20","N")).collect(Collectors.toList());

        List<Message> messageList = new ArrayList<>();
        messageList.add(new Message(TradeMessagesConstants.TRADE_UPDATE_SUCCESS_MSG,"200"));

        String content = "{\n" +
                "  \"tradeStore\":\n" +
                "    {\n" +
                "    \"tradeId\": \"T1\",\n" +
                "    \"version\": 3,\n" +
                "    \"counterPartyId\": \"CP-4\",\n" +
                "    \"bookId\": \"B3\",\n" +
                "    \"maturityDate\": \"2022-05-20\",\n" +
                "    \"createdDate\": \"2020-05-18\",\n" +
                "    \"expired\": \"N\"\n" +
                "    }\n" +
                "}";

        Mockito.when(tradeStoreManagementService.saveTrade(tradeStoreRequest)).thenReturn(
                new TradeStoreResponse(tradeStoreList,new ResponseStatus(Status.SUCCESS,messageList)));

        mockMvc.perform(MockMvcRequestBuilders.post("/trades").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


    }

}

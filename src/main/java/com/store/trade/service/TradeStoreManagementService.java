package com.store.trade.service;

import com.store.trade.dto.TradeStoreRequest;
import com.store.trade.dto.TradeStoreResponse;

import java.text.ParseException;


public interface TradeStoreManagementService {

    TradeStoreResponse getTrade(String tradeId);

    TradeStoreResponse getTrades();

    TradeStoreResponse saveTrade(TradeStoreRequest tradeStoreRequest) throws ParseException;

    int autoUpdateExpiredFlag();

}

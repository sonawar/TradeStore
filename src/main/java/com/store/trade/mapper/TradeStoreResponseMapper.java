package com.store.trade.mapper;

import com.store.trade.dto.ResponseStatus;
import com.store.trade.dto.Status;
import com.store.trade.dto.TradeStore;
import com.store.trade.dto.TradeStoreResponse;
import com.store.trade.entity.Trade;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public interface TradeStoreResponseMapper {

    static TradeStore mapTrade(Trade trade){
        TradeStore tradeStore = new TradeStore();
        tradeStore.setTradeId(trade.getTradeId());
        tradeStore.setVersion(trade.getVersion());
        tradeStore.setBookId(trade.getBookId());
        tradeStore.setCounterPartyId(trade.getCounterPartyId());
        tradeStore.setCreatedDate(trade.getCreatedDate());
        tradeStore.setMaturityDate(trade.getMaturityDate());
        tradeStore.setExpired(trade.getExpired());

        return  tradeStore;
    }

    static TradeStoreResponse mapTradeResponse(Trade trade){

        TradeStoreResponse tradeStoreResponse = null;

        TradeStore tradeStore = mapTrade(trade);

        List<TradeStore> tradeStoreList = new ArrayList<>();
        tradeStoreList.add(tradeStore);

        tradeStoreResponse = new TradeStoreResponse(tradeStoreList,new ResponseStatus(Status.SUCCESS,null));
        return  tradeStoreResponse;
    }

    static TradeStoreResponse mapTradesResponse(List<Trade> tradeList){

        TradeStoreResponse tradeStoreResponse = null;

        List<TradeStore> tradeStoreList = new ArrayList<>();

        tradeList.forEach(trade -> {
            tradeStoreList.add(mapTrade(trade));
        });

        tradeStoreResponse = new TradeStoreResponse(tradeStoreList,new ResponseStatus(Status.SUCCESS,null));
        return  tradeStoreResponse;
    }
}

package com.store.trade.mapper;

import com.store.trade.dto.TradeStore;
import com.store.trade.entity.Trade;

public class TradeRequestMapper {


    public  static Trade mapTradeStore(TradeStore tradeStore){
        return new Trade(tradeStore.getTradeId(),tradeStore.getVersion(),
                tradeStore.getCounterPartyId(),tradeStore.getBookId(),tradeStore.getMaturityDate(),
                tradeStore.getCreatedDate(),tradeStore.getExpired());

    }


    public  static Trade mapUpdateTrade(TradeStore tradeStore,Trade trade){

            trade.setBookId(tradeStore.getBookId());
            trade.setVersion(tradeStore.getVersion());
            trade.setCounterPartyId(tradeStore.getCounterPartyId());
            trade.setMaturityDate(tradeStore.getMaturityDate());
            trade.setCreatedDate(tradeStore.getCreatedDate());
            trade.setExpired(tradeStore.getExpired());
            return  trade;
    }


}

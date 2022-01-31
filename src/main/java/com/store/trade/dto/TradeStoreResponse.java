package com.store.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeStoreResponse implements Serializable {

    private List<TradeStore> tradeStore;

    private ResponseStatus responseStatus;

}

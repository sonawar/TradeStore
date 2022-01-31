package com.store.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeStore implements Serializable {

    private String tradeId;
    private int version;
    private String counterPartyId;
    private  String bookId;
    private String maturityDate;
    private String createdDate;
    private String expired;
}

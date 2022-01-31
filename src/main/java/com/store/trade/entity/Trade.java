package com.store.trade.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TRADE_STORE")
public class Trade {

    @Id
    private String tradeId;
    private int version;
    private String counterPartyId;
    private  String bookId;
    private String maturityDate;
    private String createdDate;
    private String expired;

}

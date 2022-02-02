package com.store.trade.repository;

import com.store.trade.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeStoreManagementRepository extends JpaRepository<Trade,String> {


    String updateQuery = "update TRADE_STORE set EXPIRED='Y' where MATURITY_DATE < CURRENT_DATE()";

    @Modifying
    @Query(value = updateQuery,nativeQuery = true)
    int autoUpdateExpiredFlag();

}

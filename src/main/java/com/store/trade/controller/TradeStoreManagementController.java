package com.store.trade.controller;

import com.store.trade.dto.TradeStoreRequest;
import com.store.trade.dto.TradeStoreResponse;
import com.store.trade.service.TradeStoreManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@CrossOrigin(origins = "*")
@Slf4j
/**
 * This is used to manage trades.
 */
public class TradeStoreManagementController {

    @Autowired
    private TradeStoreManagementService tradeStoreManagementService;

    /**
     * It returns all trades available in database.
     * @return
     */
    @GetMapping("/trades")
    public TradeStoreResponse getTrades(){

        TradeStoreResponse tradeStoreResponse = tradeStoreManagementService.getTrades();

        return tradeStoreResponse;
    }


    @GetMapping("/trades/{id}")
    public TradeStoreResponse getTrade(@PathVariable("id") String id){
        TradeStoreResponse tradeStoreResponse = tradeStoreManagementService.getTrade(id);

        return tradeStoreResponse;
    }

    @PostMapping("/trades")
    public TradeStoreResponse createTrade(@RequestBody TradeStoreRequest tradeStoreRequest) throws ParseException {
        TradeStoreResponse tradeStoreResponse = tradeStoreManagementService.saveTrade(tradeStoreRequest);
        return tradeStoreResponse;
    }

    @Scheduled(cron = "0 1 0 1/1 * ?")
    void runScheduledJobForAutoUpdateExpiredFlag() {
        log.info("Auto Update scheduler started....");
        try {
            int rowsUpdated = tradeStoreManagementService.autoUpdateExpiredFlag();
            log.info("Scheduler Job Ended: "+rowsUpdated+" rows updated for Expired. ");
        }catch (Exception e) {
            log.error("Exception occurred.. " + e.getMessage());
        }
    }
}

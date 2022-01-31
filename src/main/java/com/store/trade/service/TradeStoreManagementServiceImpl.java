package com.store.trade.service;

import com.store.trade.dto.*;
import com.store.trade.dto.Message;
import com.store.trade.entity.Trade;
import com.store.trade.exception.LowerVersionTradeException;
import com.store.trade.mapper.TradeRequestMapper;
import com.store.trade.mapper.TradeStoreResponseMapper;
import com.store.trade.repository.TradeStoreManagementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@Slf4j
@Service
public class TradeStoreManagementServiceImpl implements  TradeStoreManagementService {

    @Autowired
    private TradeStoreManagementRepository tradeStoreManagementRepository;

    private final Function<TradeStore,Trade> mapTrade = TradeRequestMapper::mapTradeStore;

    private final BiFunction<TradeStore,Trade,Trade> updateTrade = TradeRequestMapper::mapUpdateTrade;

    @Override
    public TradeStoreResponse getTrade(String tradeId) {

        Optional<Trade> trade = tradeStoreManagementRepository.findById(tradeId);
        TradeStoreResponse tradeStoreResponse;
        List<Message> messageList;

        if(trade.isPresent()) {
            tradeStoreResponse =  TradeStoreResponseMapper.mapTradeResponse(trade.get());
        }else{
            messageList = new ArrayList<>();
            messageList.add(new Message("Cannot find trade with given id.","400"));
            tradeStoreResponse = new TradeStoreResponse(null,new ResponseStatus(Status.FAILURE,messageList));
        }
        return tradeStoreResponse;
    }

    @Override
    public TradeStoreResponse getTrades() {
        List<Trade> tradeList = tradeStoreManagementRepository.findAll();
        TradeStoreResponse tradeStoreResponse;
        List<Message> messageList;

        if(!CollectionUtils.isEmpty(tradeList)){
            tradeStoreResponse =   TradeStoreResponseMapper.mapTradesResponse(tradeList);
        }else{
            messageList = new ArrayList<>();
            messageList.add(new Message("Cannot find trades.","400"));
            tradeStoreResponse = new TradeStoreResponse(null,new ResponseStatus(Status.FAILURE,messageList));
        }

        return tradeStoreResponse;
    }

    @Override
    public TradeStoreResponse saveTrade(TradeStoreRequest tradeStoreRequest) throws ParseException {

        TradeStoreResponse tradeStoreResponse = null;

        List<Message> messageList = new ArrayList<>();

        ResponseStatus responseStatus;

        Optional<Trade> trade = tradeStoreManagementRepository.findById(tradeStoreRequest.getTradeStore().getTradeId());

        if(trade.isPresent()) {

            if(trade.get().getVersion() > tradeStoreRequest.getTradeStore().getVersion()){
                try {
                    throw new LowerVersionTradeException("Invalid Request. Trade Version is lower than current.");

                } catch (LowerVersionTradeException e) {
                    messageList.add(new Message(e.getMessage(),"400"));
                    responseStatus = new ResponseStatus(Status.FAILURE,messageList);
                    tradeStoreResponse = new TradeStoreResponse(null,responseStatus);
                }


            }

            if(!validateMaturityDate(tradeStoreRequest.getTradeStore())){
                Message error = new Message("Invalid Request. Trade Maturity Date is earlier than current.","400");
                messageList.add(error);
                responseStatus = new ResponseStatus(Status.FAILURE,messageList);
                tradeStoreResponse = new TradeStoreResponse(null,responseStatus);
            }

            if(CollectionUtils.isEmpty(messageList)){
                Trade trade1 = updateTrade.apply(tradeStoreRequest.getTradeStore(),trade.get());
                trade1 = tradeStoreManagementRepository.save(trade1);
                messageList.add(new Message("Trade updated successfully.","200"));
                responseStatus = new ResponseStatus(Status.SUCCESS,messageList);
                List<TradeStore> tradeStoreList = new ArrayList<>();
                tradeStoreList.add(tradeStoreRequest.getTradeStore());
                tradeStoreResponse = new TradeStoreResponse(tradeStoreList,responseStatus);
            }
        }else{
            Trade trade1 = mapTrade.apply(tradeStoreRequest.getTradeStore());

            trade1 = tradeStoreManagementRepository.save(trade1);

            messageList.add(new Message("Trade saved successfully.","200"));
            responseStatus = new ResponseStatus(Status.SUCCESS,messageList);

            List<TradeStore> tradeStoreList = new ArrayList<>();
            tradeStoreList.add(tradeStoreRequest.getTradeStore());
            tradeStoreResponse = new TradeStoreResponse(tradeStoreList,responseStatus);
        }

        return tradeStoreResponse;
    }

    private boolean validateMaturityDate(TradeStore trade) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = simpleDateFormat.format(new Date());
        Date requestDate = simpleDateFormat.parse(trade.getMaturityDate());
        Date currentDate = simpleDateFormat.parse(formattedDate);

        if (requestDate.after(currentDate) || requestDate.equals(currentDate)) {
            return true;
        }
        return false;

    }

    @Override
    public int autoUpdateExpiredFlag() {
        log.info("Job is in progress....");
        return tradeStoreManagementRepository.autoUpdateExpiredFlag();
    }

}

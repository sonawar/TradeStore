package com.store.trade.service;

import com.store.trade.constant.TradeMessagesConstants;
import com.store.trade.dto.*;
import com.store.trade.dto.Message;
import com.store.trade.entity.Trade;
import com.store.trade.exception.LowerVersionTradeException;
import com.store.trade.mapper.TradeRequestMapper;
import com.store.trade.mapper.TradeStoreResponseMapper;
import com.store.trade.repository.TradeStoreManagementRepository;
import com.store.trade.utils.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
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
            messageList.add(new Message(TradeMessagesConstants.TRADE_NOT_FOUND_MSG,"400"));
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
                    throw new LowerVersionTradeException(TradeMessagesConstants.LOWER_VERSION_MSG);

                } catch (LowerVersionTradeException e) {
                    messageList.add(new Message(e.getMessage(),"400"));
                    responseStatus = new ResponseStatus(Status.FAILURE,messageList);
                    tradeStoreResponse = new TradeStoreResponse(null,responseStatus);
                }


            }

            if(!ValidationUtils.validateMaturityDate(tradeStoreRequest.getTradeStore())){
                Message error = new Message(TradeMessagesConstants.MATURITY_DATE_MSG,"400");
                messageList.add(error);
                responseStatus = new ResponseStatus(Status.FAILURE,messageList);
                tradeStoreResponse = new TradeStoreResponse(null,responseStatus);
            }

            if(CollectionUtils.isEmpty(messageList)){
                Trade trade1 = updateTrade.apply(tradeStoreRequest.getTradeStore(),trade.get());
                trade1 = tradeStoreManagementRepository.save(trade1);
                messageList.add(new Message(TradeMessagesConstants.TRADE_UPDATE_SUCCESS_MSG,"200"));
                responseStatus = new ResponseStatus(Status.SUCCESS,messageList);
                List<TradeStore> tradeStoreList = new ArrayList<>();
                tradeStoreList.add(tradeStoreRequest.getTradeStore());
                tradeStoreResponse = new TradeStoreResponse(tradeStoreList,responseStatus);
            }
        }else{

            if(!ValidationUtils.validateMaturityDate(tradeStoreRequest.getTradeStore())){
                Message error = new Message(TradeMessagesConstants.MATURITY_DATE_MSG,"400");
                messageList.add(error);
                responseStatus = new ResponseStatus(Status.FAILURE,messageList);
                tradeStoreResponse = new TradeStoreResponse(null,responseStatus);
            }

            if(CollectionUtils.isEmpty(messageList)) {
                Trade trade1 = mapTrade.apply(tradeStoreRequest.getTradeStore());

                trade1 = tradeStoreManagementRepository.save(trade1);

                messageList.add(new Message(TradeMessagesConstants.TRADE_SAVE_SUCCESS_MSG, "200"));
                responseStatus = new ResponseStatus(Status.SUCCESS, messageList);

                List<TradeStore> tradeStoreList = new ArrayList<>();
                tradeStoreList.add(tradeStoreRequest.getTradeStore());
                tradeStoreResponse = new TradeStoreResponse(tradeStoreList, responseStatus);
            }
        }

        return tradeStoreResponse;
    }



    @Override
    @Transactional
    public int autoUpdateExpiredFlag() {
        log.info("Job is in progress....");
        return tradeStoreManagementRepository.autoUpdateExpiredFlag();
    }

}

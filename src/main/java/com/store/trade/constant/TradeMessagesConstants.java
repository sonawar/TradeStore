package com.store.trade.constant;

public interface TradeMessagesConstants {

    String LOWER_VERSION_MSG = "Invalid Request. Trade Version is lower than current.";

    String MATURITY_DATE_MSG = "Invalid Request. Trade Maturity Date is earlier than current.";

    String TRADE_UPDATE_SUCCESS_MSG = "Trade updated successfully.";

    String TRADE_SAVE_SUCCESS_MSG = "Trade saved successfully.";

    String TRADE_NOT_FOUND_MSG = "Cannot find trade with given id.";
}

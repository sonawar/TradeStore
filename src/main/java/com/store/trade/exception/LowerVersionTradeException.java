package com.store.trade.exception;

public class LowerVersionTradeException extends Exception {

    private static final long serialVersionUID = 1L;

    public LowerVersionTradeException(String message) {
        super(message);
    }

}

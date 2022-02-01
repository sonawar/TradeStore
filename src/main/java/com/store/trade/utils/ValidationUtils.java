package com.store.trade.utils;

import com.store.trade.dto.TradeStore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidationUtils {

    public static boolean validateMaturityDate(TradeStore trade) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = simpleDateFormat.format(new Date());
        Date requestDate = simpleDateFormat.parse(trade.getMaturityDate());
        Date currentDate = simpleDateFormat.parse(formattedDate);

        if (requestDate.after(currentDate) || requestDate.equals(currentDate)) {
            return true;
        }
        return false;

    }
}

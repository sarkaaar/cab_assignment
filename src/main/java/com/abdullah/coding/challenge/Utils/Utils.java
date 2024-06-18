package com.abdullah.coding.challenge.Utils;

import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@UtilityClass
public class Utils {
    private static final Integer DAYS_RANGE = 10;
    public static Boolean validateFutureTimeStamp(Timestamp timestamp){

        if (timestamp == null) {
            return false;
        }

        LocalDateTime timestampTime = timestamp.toLocalDateTime();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime DaysRange = LocalDateTime.now().plusDays(DAYS_RANGE);

        return !timestampTime.isBefore(now) && !timestampTime.isAfter(DaysRange);
    }
}

package com.puipui.tomato.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    /**
     * 日付フォーマット
     * 
     * @param localDateTime
     * @return
     */
    static public String DateTimeformat(LocalDateTime localDateTime) {
        return localDateTime.format(
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }
}

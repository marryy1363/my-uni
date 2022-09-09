package com.fara.demo.Utils;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtils {
    public static Date dateOf(String date) throws ParseException {
        java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        return new Date(utilDate.getTime());

    }
}

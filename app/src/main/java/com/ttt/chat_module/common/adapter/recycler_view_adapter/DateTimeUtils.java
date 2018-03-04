package com.ttt.chat_module.common.adapter.recycler_view_adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by TranThanhTung on 20/02/2018.
 */

public class DateTimeUtils {
    public static final String FORMAT = "dd/MM/yyyy 'at' hh:mm";

    public static String getDateTimeString(Date date) {
        return new SimpleDateFormat(FORMAT, Locale.US).format(date);
    }
}

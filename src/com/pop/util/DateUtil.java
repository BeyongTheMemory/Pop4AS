package com.pop.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xugang on 16/10/12.
 */
public class DateUtil {
    public static String getDate(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}

package com.kingdom.system.util;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 描述：编号工具
 * Created by jay on 2017-11-13.
 */
public class NOUtils {

    private static final String GENERATOR = "10";

    private static final String NUMBER = "00000";

    public static String getGeneratorNO(Long id) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
        Date date = new Date();
        String dateStr = simpleDateFormat.format(date);
        String number = NUMBER + id;
        number = number.substring(number.length() -  NUMBER.length());
        return dateStr + number;
    }

}

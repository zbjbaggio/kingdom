package com.kingdom.system.util;

import java.math.BigDecimal;

public class BigDecimalUtils {

    public static BigDecimal multiply(BigDecimal param, Integer paramI) {
       return param.multiply(new BigDecimal(Integer.toString(paramI)));
    }
}

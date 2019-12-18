package com.kingdom.system.util.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Function:
 * Created by longlongago on 2019-10-30.
 */
@Slf4j
public class ExcelColumnIndexUtil {

    public static Integer getColumnIndex(String columnStr) throws Exception {
        if (StringUtils.isBlank(columnStr) || columnStr.length() > 3) {
            throw new Exception("列表为空，或大于3个字母");
        }
        columnStr = columnStr.toUpperCase();
        int columnIndex = 0;
        char[] charArry = columnStr.toCharArray();
        for (int i = charArry.length, j = 0; i > 0; i--, j++) {
            columnIndex += (Integer.valueOf(charArry[j]) - 64) * (Math.pow(26, i - 1));
        }
        return --columnIndex;
    }

}

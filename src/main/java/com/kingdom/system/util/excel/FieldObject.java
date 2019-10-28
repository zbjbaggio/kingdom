package com.kingdom.system.util.excel;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 描述：excel 字段属性
 * Created by jay on 2017-9-18.
 */
@Data
public class FieldObject {

    private int index;

    private String name;

    private String title;

    private boolean lockBoolean;

    private List<String> selects;

    private String pointOut;

    private int width;

    private Map<String, List<String>> map;

    private String parentName;

    public boolean getLockBoolean() {
        return lockBoolean;
    }

    private ExcelDataEnums format;

    private Class<? extends ExcelSelectInterface> returnSelectDataClass;

    private Class<? extends ExcelSelectMapInterface> returnSelectMapClass;

}

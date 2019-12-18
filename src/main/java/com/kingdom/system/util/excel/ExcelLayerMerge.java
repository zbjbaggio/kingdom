package com.kingdom.system.util.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：excel 每层合并信息
 * Created by longlongago on 2019-10-30.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelLayerMerge {

    //递归实际处理的列号
    private List<Integer> columnList = new ArrayList<>();

    //k-该列合并序号，v-合并行标号
    private Map<Integer, RowMerge> mergeMap = new HashMap<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RowMerge {

        private int firstRow = -1;

        private int lastRow = -1;

        private String sourceName;
    }

}

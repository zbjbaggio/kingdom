package com.kingdom.system.data.vo;

import com.kingdom.system.data.entity.ProductPackage;
import com.kingdom.system.util.excel.ExcelExport;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class ProductPackageVO extends ProductPackage {

    @ExcelExport(name = "名字")
    private String name;

    @ExcelExport(name = "编号")
    private String code;

}

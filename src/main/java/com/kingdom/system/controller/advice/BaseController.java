package com.kingdom.system.controller.advice;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kingdom.system.data.base.PageDomain;
import com.kingdom.system.data.base.TableDataInfo;
import com.kingdom.system.data.base.TableSupport;
import com.kingdom.system.util.SqlUtil;

import java.sql.SQLOutput;
import java.util.List;

public class BaseController {

    protected void startPage() {
        PageDomain pageDomain = TableSupport.getPageDomain();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (pageNum != null && pageSize != null) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }
}

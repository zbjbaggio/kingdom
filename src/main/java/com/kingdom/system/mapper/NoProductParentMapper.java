package com.kingdom.system.mapper;


import com.kingdom.system.data.entity.NoProductParent;

import java.util.List;

/**
 * 欠货总 数据层
 *
 * @author kingdom
 * @date 2019-12-12
 */
public interface NoProductParentMapper {
    /**
     * 查询欠货总信息
     *
     * @param id 欠货总ID
     * @return 欠货总信息
     */
    public NoProductParent selectNoProductParentById(Long id);

    /**
     * 查询欠货总列表
     *
     * @param noProductParent 欠货总信息
     * @return 欠货总集合
     */
    public List<NoProductParent> selectNoProductParentList(NoProductParent noProductParent);

    /**
     * 新增欠货总
     *
     * @param noProductParent 欠货总信息
     * @return 结果
     */
    public int insertNoProductParent(NoProductParent noProductParent);

    /**
     * 修改欠货总
     *
     * @param noProductParent 欠货总信息
     * @return 结果
     */
    public int updateNoProductParent(NoProductParent noProductParent);

    /**
     * 删除欠货总
     *
     * @param id 欠货总ID
     * @return 结果
     */
    public int deleteNoProductParentById(Long id);

    /**
     * 批量删除欠货总
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteNoProductParentByIds(String[] ids);

}
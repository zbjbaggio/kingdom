package com.kingdom.system.mapper;

import com.kingdom.system.data.entity.ProductPackage;
import com.kingdom.system.data.vo.ProductPackageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品包 数据层
 *
 * @author kingdom
 * @date 2019-10-19
 */
public interface ProductPackageMapper {
    /**
     * 查询产品包信息
     *
     * @param id 产品包ID
     * @return 产品包信息
     */
    public ProductPackage selectProductPackageById(Long id);

    /**
     * 查询产品包列表
     *
     * @param productPackage 产品包信息
     * @return 产品包集合
     */
    public List<ProductPackage> selectProductPackageList(ProductPackage productPackage);

    int insertProductPackage(ProductPackage productPackage);

    /**
     * 修改产品包
     *
     * @param productPackage 产品包信息
     * @return 结果
     */
    public int updateProductPackage(ProductPackage productPackage);

    /**
     * 删除产品包
     *
     * @param id 产品包ID
     * @return 结果
     */
    public int deleteProductPackageById(Long id);

    /**
     * 批量删除产品包
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteProductPackageByIds(String[] ids);

    List<ProductPackageVO> listByProductIds(List<Long> ids);

    int insertProductPackages(List<ProductPackage> productPackageList);

    int deleteProductPackageByProductId(@Param(value = "productId") Long productId);

}
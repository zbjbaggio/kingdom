package com.kingdom.system.mapper;

import com.kingdom.system.data.entity.Product;
import com.kingdom.system.data.vo.ProductVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品 数据层
 *
 * @author kingdom
 * @date 2019-10-19
 */
public interface ProductMapper {
    /**
     * 查询产品信息
     *
     * @param id 产品ID
     * @return 产品信息
     */
    public Product selectProductById(Long id);

    /**
     * 查询产品列表
     *
     * @param product 产品信息
     * @return 产品集合
     */
    public List<Product> selectProductList(Product product);

    /**
     * 新增产品
     *
     * @param product 产品信息
     * @return 结果
     */
    public int insertProduct(Product product);

    /**
     * 修改产品
     *
     * @param product 产品信息
     * @return 结果
     */
    public int updateProduct(Product product);

    /**
     * 删除产品
     *
     * @param id 产品ID
     * @return 结果
     */
    public int deleteProductById(Long id);

    /**
     * 批量删除产品
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteProductByIds(String[] ids);

    List<Product> listAll(String name);

    List<ProductVO> listProductPackage(String search);

    List<ProductVO> listProduct(@Param(value = "search") String search, @Param(value = "sendDateStart") String sendDateStart,
                                @Param(value = "sendDateEnd") String sendDateEnd);

    int updateStatus(@Param(value = "id") String id, @Param(value = "status") int status);

    int listByCode(Product product);
}
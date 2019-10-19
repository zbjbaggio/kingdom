package com.kingdom.system.mapper;

import com.kingdom.system.data.entity.ExchangeRateRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;	

/**
 * 汇率记录 数据层
 * 
 * @author kingdom
 * @date 2019-10-19
 */
public interface ExchangeRateRecordMapper 
{
	/**
     * 查询汇率记录信息
     * 
     * @param id 汇率记录ID
     * @return 汇率记录信息
     */
	public ExchangeRateRecord selectExchangeRateRecordById(Long id);
	
	/**
     * 查询汇率记录列表
     * 
     * @param exchangeRateRecord 汇率记录信息
     * @return 汇率记录集合
     */
	public List<ExchangeRateRecord> selectExchangeRateRecordList(ExchangeRateRecord exchangeRateRecord);
	
	/**
     * 新增汇率记录
     * 
     * @param exchangeRateRecord 汇率记录信息
     * @return 结果
     */
	public int insertExchangeRateRecord(ExchangeRateRecord exchangeRateRecord);
	
	/**
     * 修改汇率记录
     * 
     * @param exchangeRateRecord 汇率记录信息
     * @return 结果
     */
	public int updateExchangeRateRecord(ExchangeRateRecord exchangeRateRecord);
	
	/**
     * 删除汇率记录
     * 
     * @param id 汇率记录ID
     * @return 结果
     */
	public int deleteExchangeRateRecordById(Long id);
	
	/**
     * 批量删除汇率记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteExchangeRateRecordByIds(String[] ids);

	List<ExchangeRateRecord> list(@Param(value = "date") String date);

	int updateStatus(@Param(value = "status") int status);
}
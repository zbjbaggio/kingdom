package com.kingdom.system.service.impl;

import com.kingdom.system.data.enmus.ErrorInfo;
import com.kingdom.system.data.entity.ExchangeRateRecord;
import com.kingdom.system.data.exception.PrivateException;
import com.kingdom.system.mapper.ExchangeRateRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ExchangeRateRecordServiceImpl {

    @Inject
    private ExchangeRateRecordMapper exchangeRateRecordMapper;

    public List<ExchangeRateRecord> list(String date) {
        ExchangeRateRecord exchangeRateRecord = exchangeRateRecordMapper.selectExchangeRateRecordById(1L);
        return exchangeRateRecordMapper.list(date);
    }

    @Transactional
    public ExchangeRateRecord add(ExchangeRateRecord exchangeRateRecord) {
        exchangeRateRecordMapper.updateStatus(1);
        exchangeRateRecord.setStatus(0);
        int count = exchangeRateRecordMapper.insertExchangeRateRecord(exchangeRateRecord);
        if (count != 1) {
            log.error("港币汇率保存失败！exchangeRateRecord：{}", exchangeRateRecord);
            throw new PrivateException(ErrorInfo.SAVE_ERROR);
        }
        return exchangeRateRecord;
    }

    public ExchangeRateRecord selectDefault() {
        return exchangeRateRecordMapper.selectDefault();
    }
}

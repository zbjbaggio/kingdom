package com.kingdom.system.service.impl;

import com.kingdom.system.data.enmus.ErrorInfo;
import com.kingdom.system.data.entity.Notice;
import com.kingdom.system.data.exception.PrivateException;
import com.kingdom.system.mapper.NoticeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NoticeServiceImpl {

    @Autowired
    private NoticeMapper noProductMapper;

    public List<Notice> list(String title) {
        return noProductMapper.listNotice(title);
    }

    public Notice getDetail(Long id) {
        return noProductMapper.selectNotice(id);
    }

    public Notice update(Notice notice) {
        int count = noProductMapper.updateNotice(notice);
        if (count != 1) {
            throw new PrivateException(ErrorInfo.UPDATE_ERROR);
        }
        return notice;
    }

    public Notice insert(Notice notice) {
        int count = noProductMapper.insertNotice(notice);
        if (count != 1) {
            throw new PrivateException(ErrorInfo.SAVE_ERROR);
        }
        return notice;
    }

    public int delete(Long id) {
        return noProductMapper.deleteNoticeById(id);
    }

}

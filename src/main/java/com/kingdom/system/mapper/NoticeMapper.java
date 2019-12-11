package com.kingdom.system.mapper;

import com.kingdom.system.data.entity.Notice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NoticeMapper {

    public List<Notice> listNotice(@Param(value = "search") String title);

    public Notice selectNotice(@Param(value = "id")Long id);

    public int insertNotice(Notice notice);

    public int updateNotice(Notice notice);

    public int deleteNoticeById(@Param(value = "id")Long id);
}

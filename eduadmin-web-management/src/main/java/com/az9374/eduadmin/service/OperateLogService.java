package com.az9374.eduadmin.service;

import com.az9374.eduadmin.pojo.OperateLog;
import com.az9374.eduadmin.pojo.PageResult;

public interface OperateLogService {
    PageResult<OperateLog> searchPage(Integer page, Integer pageSize);
}

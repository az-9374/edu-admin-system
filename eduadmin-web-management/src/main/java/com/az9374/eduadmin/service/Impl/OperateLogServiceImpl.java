package com.az9374.eduadmin.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.az9374.eduadmin.mapper.OperateLogMapper;
import com.az9374.eduadmin.pojo.OperateLog;
import com.az9374.eduadmin.pojo.PageResult;
import com.az9374.eduadmin.service.OperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperateLogServiceImpl implements OperateLogService {

    @Autowired
    private OperateLogMapper operateLogMapper;
    @Override
    public PageResult<OperateLog> searchPage(Integer page, Integer pageSize) {
        //1.分页
        PageHelper.startPage(page,pageSize);
        //2.得到List<OperateLog>
        List<OperateLog> operateLogs = operateLogMapper.searchPage();
        //3.封装PageResult
        Page<OperateLog> p=(Page<OperateLog>) operateLogs;
        PageResult<OperateLog> pageResult = new PageResult<>(p.getTotal(),operateLogs);

        return pageResult;
    }
}

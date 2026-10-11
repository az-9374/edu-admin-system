package com.az9374.eduadmin.controller;

import com.az9374.eduadmin.pojo.OperateLog;
import com.az9374.eduadmin.pojo.PageResult;
import com.az9374.eduadmin.pojo.Result;
import com.az9374.eduadmin.service.OperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/log")
public class OperateLogController {
    @Autowired
    private OperateLogService operateLogService;
    /**
     * 日志列表查询
     */
    @GetMapping("/page")
    public Result searchPage(Integer page, Integer pageSize){
        log.info("日志列表查询...");
        PageResult<OperateLog> pageResult = operateLogService.searchPage(page,pageSize);
        return Result.success(pageResult);
    }

}

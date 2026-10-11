package com.az9374.eduadmin.controller;

import com.az9374.eduadmin.pojo.Emp;
import com.az9374.eduadmin.pojo.LoginOption;
import com.az9374.eduadmin.pojo.Result;
import com.az9374.eduadmin.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private EmpService empService;
    /**
     * 登录
     */
    @PostMapping
    public Result login(@RequestBody Emp emp){
        log.info("员工登录：{}",emp);
        //1.得到返回响应
        LoginOption loginOption=empService.login(emp);
        //2.判断是否登录成功
        if (loginOption==null){
            return Result.error("用户名或密码错误");
        }
        return Result.success(loginOption);
    }

}

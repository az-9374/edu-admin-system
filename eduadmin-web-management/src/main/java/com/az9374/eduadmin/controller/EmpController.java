package com.az9374.eduadmin.controller;

import com.az9374.eduadmin.anno.Log;
import com.az9374.eduadmin.pojo.Emp;
import com.az9374.eduadmin.pojo.EmpQueryParam;
import com.az9374.eduadmin.pojo.PageResult;
import com.az9374.eduadmin.pojo.Result;
import com.az9374.eduadmin.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {

    @Autowired
    private EmpService empService;

    /**
     * 员工列表查询-分页-初版
     *
     */
    /*@GetMapping
    public Result searchPage(@RequestParam(defaultValue = "1") Integer page ,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             String name, Integer gender,
                             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                             @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate end){
        log.info("员工列表-查询:{},{},{},{},{},{}",page,pageSize,name,gender,begin
        ,end);
        PageResult pageResult = empService.searchPage(page,pageSize,name,gender,begin
                ,end);
        return Result.success(pageResult);
    }*/

    /**
     * 员工列表查询-分页-优化
     *
     */
    @GetMapping
    public Result searchPage(EmpQueryParam empQueryParam){
        log.info("员工列表-查询:{}",empQueryParam);
        PageResult pageResult = empService.searchPage(empQueryParam);
        return Result.success(pageResult);
    }

    /**
     * 员工列表-新增员工
     *
     */
    @Log
    @PostMapping
    public Result save(@RequestBody Emp emp) throws Exception {
        empService.save(emp);
        return Result.success();
    }

    /**
     * 员工列表-删除员工
     *
     */
    @Log
    @DeleteMapping
//    public Result delete(Integer[] ids){
    public Result delete(@RequestParam List<Integer> ids){//数组、集合（@Re...Param不能省）[推荐]两种方式
        log.info("删除员工：{}",ids);
        empService.delete(ids);
        return Result.success();
    }

    /**
     * 修改员工--查询回显
     */
    @GetMapping("/{id}")
    public Result searchById(@PathVariable("id") Integer id){
        log.info("员工查询回显：{}",id );
        Emp emp= empService.searchById(id);
        return Result.success(emp);
    }

    /**
     * 修改员工--修改
     */
    @Log
    @PutMapping
    public Result update(@RequestBody Emp emp){
        log.info("修改员工: {}", emp);
        empService.update(emp);
        return Result.success();
    }

    /**
     * 班级--员工查询
     */
    @GetMapping("/list")
    public Result getMasterNames(){
        log.info("查询所有员工： {}");
//        List<String> masterNames=empService.getMasterNames();
        List<Emp> masterNames=empService.getMasterNames();
        return  Result.success(masterNames);
    }
}

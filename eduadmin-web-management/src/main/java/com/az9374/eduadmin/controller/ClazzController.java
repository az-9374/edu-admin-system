package com.az9374.eduadmin.controller;

import com.az9374.eduadmin.anno.Log;
import com.az9374.eduadmin.pojo.Clazz;
import com.az9374.eduadmin.pojo.ClazzQueryParam;
import com.az9374.eduadmin.pojo.PageResult;
import com.az9374.eduadmin.pojo.Result;
import com.az9374.eduadmin.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/clazzs")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    /**
     * 班级条件分页查询接口
     * 1.先做不带条件的分页查询
     */
/*    @GetMapping
    public Result searchAll(Integer page,Integer pageSize){
        log.info("查询所有班级...");
//        List<Clazz> clazzList=clazzService.searchAll(page,pageSize);
        PageResult<Clazz> clazzList=clazzService.searchAll(page,pageSize);
        return Result.success(clazzList);
    }*/
    /**
     *班级条件分页查询接口
     *2.带条件的分页查询,并使用对象接收参数
     */
    @GetMapping
    public Result searchPage(ClazzQueryParam clazzQueryParam){
        log.info("查询满足要求的班级: {}",clazzQueryParam);
        PageResult<Clazz> clazzList=clazzService.searchAll(clazzQueryParam);
        return Result.success(clazzList);
    }
    /**
     * 查询所有班级--/list实现
     */
    @GetMapping("/list")
    public Result getMasterNames(){
        log.info("查询所有班级： {}");
        List<Clazz> clazzes=clazzService.getAllClazz();
        return  Result.success(clazzes);
    }

    /**
     * 3.新增班级
     */
//    @Log
    @PostMapping
    public Result addClazz(@RequestBody Clazz clazz){
        log.info("新增班级: {}",clazz);
        clazzService.addClazz(clazz);
        return  Result.success();
    }

    /**
     * 4.修改班级
     * 先根据id查班级
     */

    @GetMapping("/{id}")
    public Result getClazzById( @PathVariable("id") Integer id){
        log.info("根据id查询班级：{}",id);
        Clazz clazz=clazzService.getClazzById(id);
        return Result.success(clazz);
    }
    /**
     * 4.修改班级
     * 修改班级
     */
//    @Log
    @PutMapping
    public Result updateClazz(@RequestBody Clazz clazz){
        log.info("修改班级：{}",clazz);
        clazzService.updateClazz(clazz);
        return Result.success();
    }

    /**
     * 5.删除班级
     */
//    @Log
    @DeleteMapping("/{id}")
    public Result deleteClazz(@PathVariable("id") Integer id){
        log.info("根据id删除班级：{}",id);
        clazzService.deleteClazz(id);
        return Result.success();
    }

}

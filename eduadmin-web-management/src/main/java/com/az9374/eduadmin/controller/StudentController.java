package com.az9374.eduadmin.controller;

import com.az9374.eduadmin.anno.Log;
import com.az9374.eduadmin.pojo.PageResult;
import com.az9374.eduadmin.pojo.Result;
import com.az9374.eduadmin.pojo.Student;
import com.az9374.eduadmin.pojo.StudentQueryParam;
import com.az9374.eduadmin.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    /**
     * 分页条件查询学员列表
     */
    @GetMapping
    public Result searchStu(StudentQueryParam studentQueryParam){
        log.info("条件查询学员列表： {}",studentQueryParam);
        PageResult<Student> pageResult=studentService.searchStu(studentQueryParam);
        return Result.success(pageResult);
    }

    /**
     * 查询所有班级接口--班级管理实现
     */

    /**
     * 新增学生信息接口
     */
//    @Log
    @PostMapping
    public Result addStu(@RequestBody Student student){
        log.info("新增学生信息： {}",student);
        studentService.addStu(student);
        return Result.success();
    }

    /**
     * 根据id查询学生(响应里面比接口要求的多一个clazzName属性（null）)
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") Integer id){
        log.info("根据id查询学生: {}",id);
        Student student=studentService.getById(id);
        return Result.success(student);
    }


    /**
     * 修改学员
     */
//    @Log
    @PutMapping
    public Result update(@RequestBody Student student){
        log.info("修改学员: {}",student);
        studentService.update(student);
        return Result.success();
    }
    /**
     * 批量删除学员
     */
//    @Log
    @DeleteMapping("/{ids}")
    public Result deleteByIds(@PathVariable("ids") List<Integer> ids){
        log.info("批量删除学员：{}",ids);
        studentService.deleteByIds(ids);
        return Result.success();
    }

    /**
     * 违纪处理
     */
    @Log
    @PutMapping("/violation/{id}/{score}")
    public Result violation(@PathVariable("id") Integer id,
                            @PathVariable("score") Short score){
        log.info("违纪扣分处理：id:{}，score:{}",id,score);
        studentService.violation(id,score);
        return Result.success();
    }
}

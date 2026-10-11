package com.az9374.eduadmin.controller;

import com.az9374.eduadmin.anno.Log;
import com.az9374.eduadmin.pojo.Dept;
import com.az9374.eduadmin.pojo.Result;
import com.az9374.eduadmin.service.DeptService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RequestMapping("/depts")
@RestController
public class DeptController {
    //定义日志记录对象
//    public static final Logger log= LoggerFactory.getLogger(DeptController.class);

//    数据响应处理
    @Autowired
    private DeptService deptService;
//    @RequestMapping(value = "/depts",method = RequestMethod.GET)
//    @GetMapping("/depts")
    @GetMapping
    public Result findAll(){
//        System.out.println("执行查询...");
        log.info("查询所有部门...");
        List<Dept> deptList=deptService.findAll();
        return Result.success(deptList);
    }

    //需要去获取前端传来的参数,方式*3
//    @DeleteMapping("/depts")
//    public Result deleteById(HttpServletRequest request){
//        String idStr=request.getParameter("id");
//        Integer id=Integer.parseInt(idStr);
//
//        deptService.deleteById(id);
//        return Result.success();
//    }
//    @DeleteMapping("/depts")
//    public Result deleteById(@RequestParam("id") Integer id){
//        deptService.deleteById(id);
//        return Result.success();
//    }

//    @DeleteMapping("/depts")
    @Log
    @DeleteMapping
    public Result deleteById(Integer id){
        log.info("根据Id删除部门: id-{}",id);
        deptService.deleteById(id);
        return Result.success();
    }

//    @PostMapping("/depts")//注意POST方式参数接收问题（json格式的参数，同上面有所不同）
//    public Result insertByName(String name){//这样跑得通但是不规范
//        deptService.insertByName(name);
//        return Result.success();
//    }

//    @PostMapping("/depts")
    @Log
    @PostMapping
    public Result insertByName(@RequestBody Dept dept ){
        log.info("新增部门：dept-{}",dept);
        deptService.insertByName(dept);
        return Result.success();
    }

//    @GetMapping("/depts/{id}")
    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") Integer id){
        log.info("根据Id查询部门: id-{}",id);
        Dept dept=deptService.getById(id);
        return Result.success(dept);
    }

//    @PutMapping("/depts")
    @Log
    @PutMapping
    public Result update(@RequestBody Dept dept){
        log.info("修改部门: dept-{}",dept);
        deptService.update(dept);
        return Result.success(dept);
    }

}

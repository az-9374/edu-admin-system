package com.az9374.eduadmin.controller;

import com.az9374.eduadmin.pojo.JobOption;
import com.az9374.eduadmin.pojo.Result;
import com.az9374.eduadmin.pojo.StudentCountOption;
import com.az9374.eduadmin.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    /**
     *员工Job统计
     * @return
     */
    @GetMapping("/empJobData")
    public Result getEmpJobData(){
        log.info("统计各个职位的员工人数...");
        JobOption jobOption=reportService.getEmpJobData();
        return Result.success(jobOption);
    }

    /**
     * 员工性别统计
     */
    @GetMapping("/empGenderData")
    public Result getEmpGenderData(){
        log.info("统计员工性别人数...");
        List<Map> genderData=reportService.getEmpGenderData();
        return  Result.success(genderData);
    }
    /**
     * 班级人数统计
     */
    @GetMapping("/studentCountData")
    public Result getStudentCountData(){
        log.info("统计班级人数...");
        StudentCountOption studentCountOption=reportService.getStudentCountData();
        return Result.success(studentCountOption);
    }

    /**
     *学员学历统计
     */
    @GetMapping("/studentDegreeData")
    public Result getStudentDegreeData(){
        log.info("学员学历统计...");
        List<Map> studentDegreeData=reportService.getStudentDegreeData();
        return Result.success(studentDegreeData);
    }

}

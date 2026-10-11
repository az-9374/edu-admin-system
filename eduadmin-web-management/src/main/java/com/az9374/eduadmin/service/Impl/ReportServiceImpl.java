package com.az9374.eduadmin.service.Impl;

import com.az9374.eduadmin.mapper.EmpMapper;
import com.az9374.eduadmin.mapper.StudentMapper;
import com.az9374.eduadmin.pojo.Clazz;
import com.az9374.eduadmin.pojo.JobOption;
import com.az9374.eduadmin.pojo.StudentCountOption;
import com.az9374.eduadmin.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public JobOption getEmpJobData() {
        //1.行select方法执
        List<Map<String,Object>> list = empMapper.getEmpJobData();
        //2.把数据格式封装成JobOption
        List<Object> jobList=list.stream().map(dataMap ->dataMap.get("pos")).toList();
        List<Object> dataList=list.stream().map(dataMap->dataMap.get("total")).toList();
        return new JobOption(jobList,dataList);
    }


    @Override
    public List<Map> getEmpGenderData() {
        //1.行select方法，，不需要封装
        List<Map> genderData=empMapper.getEmpGenderData();
        return genderData;
    }

    @Override
    public StudentCountOption getStudentCountData() {
        //1.得到数据
        List<Map<String,Object>> list=studentMapper.getStudentCountData();
        //2.分装
        List<Object> clazzList=list.stream().map(dataMap->dataMap.get("clazz")).toList();
        List<Object> dataList=list.stream().map(dataMap->dataMap.get("data")).toList();


        return new StudentCountOption(clazzList,dataList);
    }

    @Override
    public List<Map> getStudentDegreeData() {
        List<Map> degreeData=studentMapper.getStudentDegreeData();
        return degreeData;
    }
}

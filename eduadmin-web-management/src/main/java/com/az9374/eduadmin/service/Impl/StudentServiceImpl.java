package com.az9374.eduadmin.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.az9374.eduadmin.mapper.StudentMapper;
import com.az9374.eduadmin.pojo.PageResult;
import com.az9374.eduadmin.pojo.Student;
import com.az9374.eduadmin.pojo.StudentQueryParam;
import com.az9374.eduadmin.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    /**
     * 分页条件查询学员列表
     */
    @Override
    public PageResult<Student> searchStu(StudentQueryParam studentQueryParam) {
        //1.分页查询
        PageHelper.startPage(studentQueryParam.getPage(),studentQueryParam.getPageSize());
        List<Student> studentList= studentMapper.searchStu(studentQueryParam);
        //2.封装total和data
        Page<Student> p=(Page<Student>) studentList;
        PageResult<Student> pageResult=new PageResult<>(p.getTotal(),studentList);
        return pageResult;
    }

    /**
     * 新增学生信息接口
     */
    @Override
    public void addStu(Student student) {
        //1.补全信息
        student.setViolationCount((short) 0);
        student.setViolationScore((short) 0);

        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());

        //2.方法执行
        studentMapper.addStu(student);

    }

    /**
     * 根据id查询学生
     */
    @Override
    public Student getById(Integer id) {
        Student student= studentMapper.getById(id);
        return student;
    }

    /**
     * 修改学员
     */
    @Override
    public void update(Student student) {
        //1.补充updateTime
        student.setUpdateTime(LocalDateTime.now());
        //2.
        studentMapper.update(student);
    }

    /**
     * 批量删除学员
     */
    @Override
    public void deleteByIds(List<Integer> ids) {
        studentMapper.deleteByIds(ids);
    }

    /**
     * 违纪扣分处理
     */
    @Override
    public void violation(Integer id, Short score) {

        Student student=studentMapper.getById(id);
//        System.out.println(student);
        student.setUpdateTime(LocalDateTime.now());
        student.setViolationScore((short) (student.getViolationScore()+score));
        student.setViolationCount((short) (student.getViolationCount()+1));
        studentMapper.update(student);
    }
}



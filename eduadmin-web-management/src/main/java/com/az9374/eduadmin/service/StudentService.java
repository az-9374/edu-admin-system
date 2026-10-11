package com.az9374.eduadmin.service;

import com.az9374.eduadmin.pojo.PageResult;
import com.az9374.eduadmin.pojo.Student;
import com.az9374.eduadmin.pojo.StudentQueryParam;

import java.util.List;

public interface StudentService {

    PageResult<Student> searchStu(StudentQueryParam studentQueryParam);

    void addStu(Student student);

    Student getById(Integer id);

    void update(Student student);

    void deleteByIds(List<Integer> ids);

    void violation(Integer id, Short score);
}

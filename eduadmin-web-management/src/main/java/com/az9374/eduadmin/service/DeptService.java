package com.az9374.eduadmin.service;

import com.az9374.eduadmin.pojo.Dept;
import com.az9374.eduadmin.pojo.Result;

import java.util.List;

public interface DeptService {

    List<Dept> findAll();

    void deleteById(Integer id);

    void insertByName(Dept dept);

    void update(Dept dept);

    Dept getById(Integer id);
}

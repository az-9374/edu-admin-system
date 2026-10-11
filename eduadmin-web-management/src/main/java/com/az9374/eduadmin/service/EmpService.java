package com.az9374.eduadmin.service;

import com.az9374.eduadmin.pojo.Emp;
import com.az9374.eduadmin.pojo.EmpQueryParam;
import com.az9374.eduadmin.pojo.LoginOption;
import com.az9374.eduadmin.pojo.PageResult;

import java.time.LocalDate;
import java.util.List;

public interface EmpService {
    PageResult searchPage(EmpQueryParam empQueryParam);

    void save(Emp emp) throws Exception;

    void delete(List<Integer> ids);

    Emp searchById(Integer id);

    void update(Emp emp);

    List<Emp> getMasterNames();

    LoginOption login(Emp emp);
//    PageResult searchPage(Integer page, Integer pageSize, String name, Integer gender, LocalDate begin
//            ,LocalDate end);

}

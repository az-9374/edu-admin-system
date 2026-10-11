package com.az9374.eduadmin.service;

import com.az9374.eduadmin.pojo.Clazz;
import com.az9374.eduadmin.pojo.ClazzQueryParam;
import com.az9374.eduadmin.pojo.PageResult;

import java.util.List;

public interface ClazzService {

    PageResult<Clazz> searchAll(ClazzQueryParam clazzQueryParam);



    void addClazz(Clazz clazz);

    Clazz getClazzById(Integer id);

    void updateClazz(Clazz clazz);

    void deleteClazz(Integer id);

    List<Clazz> getAllClazz();

//    PageResult<Clazz> searchAll(Integer page, Integer pageSize);

}

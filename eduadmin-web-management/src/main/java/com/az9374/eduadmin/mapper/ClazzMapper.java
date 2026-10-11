package com.az9374.eduadmin.mapper;

import com.az9374.eduadmin.pojo.Clazz;
import com.az9374.eduadmin.pojo.ClazzQueryParam;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@Mapper
public interface ClazzMapper {

//    List<Clazz> searchAll();

    List<Clazz> searchBy(ClazzQueryParam clazzQueryParam);

//    @Select("select name from emp")
//    List<String> getMasterNames();


    void addClazz(Clazz clazz);

    @Select("select * from clazz where id=#{id}")
    Clazz getClazzById(Integer id);

    void updateClazz(Clazz clazz);

    @Select("select count(*) from student where clazz_id=#{id}")
    Integer getStudentsNum(Integer id);

    @Delete("delete from clazz where id =#{id}")
    void deleteClazz(Integer id);

    @Select("select * from clazz")
    List<Clazz> getAllClazz();
}

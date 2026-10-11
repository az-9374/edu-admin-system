package com.az9374.eduadmin.mapper;

import com.az9374.eduadmin.pojo.Clazz;
import com.az9374.eduadmin.pojo.Student;
import com.az9374.eduadmin.pojo.StudentQueryParam;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {

    List<Student> searchStu(StudentQueryParam studentQueryParam);

    void addStu(Student student);

    @Select("select * from student where id=#{id}")
    Student getById(Integer id);

    void update(Student student);

    void deleteByIds(List<Integer> ids);

    @MapKey("clazz")
    List<Map<String, Object>> getStudentCountData();

//    @Select("select degree,count(*) data from student group by degree  order by data")
    List<Map> getStudentDegreeData();
}

package com.az9374.eduadmin.mapper;

import com.az9374.eduadmin.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeptMapper {

//    @Results({
//            @Result(column = "create_time",property = "createTime"),
//            @Result(column = "update_time",property = "updateTime")
//    })
//    @Select("select id, name, create_time createTime, update_time updateTime from dept order by update_time desc ")
@Select("select id, name, create_time, update_time from dept order by update_time desc ")
    List<Dept> findAll();

//    优化--如果部门下有员工就不允许删--且抛异常（对不起，当前部门下有员工，不能直接删除！）
    @Delete("delete from dept where id=#{id}")
    void deleteById(Integer id);

    @Insert("insert into dept (name,create_time, update_time)" +
            " values (#{name},#{createTime},#{updateTime})")
    void insertByName(Dept dept);

    @Update("update dept set name=#{name},update_time=#{updateTime} where id=#{id}")
    void update(Dept dept);

    @Select("select id, name, create_time, update_time from dept where id=#{id}")
    Dept getById(Integer id);
}

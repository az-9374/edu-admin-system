package com.az9374.eduadmin.mapper;

import com.az9374.eduadmin.pojo.OperateLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OperateLogMapper {

    //插入日志数据
    @Insert("insert into operate_log (operate_emp_id, operate_time, class_name, method_name, method_params, return_value, cost_time) " +
            "values (#{operateEmpId}, #{operateTime}, #{className}, #{methodName}, #{methodParams}, #{returnValue}, #{costTime});")
    public void insert(OperateLog log);

    //查询日志数据
    @Select("select o.*,e.name operate_emp_name from operate_log o left join emp e on e.id=o.operate_emp_id\n" +
            "order by o.operate_time desc")
    List<OperateLog> searchPage();
}

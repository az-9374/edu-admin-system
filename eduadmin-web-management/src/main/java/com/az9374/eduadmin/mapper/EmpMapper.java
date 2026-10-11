package com.az9374.eduadmin.mapper;

import com.az9374.eduadmin.pojo.Emp;
import com.az9374.eduadmin.pojo.EmpExpr;
import com.az9374.eduadmin.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {

    /**
     * 分页查询-返回总数量+查询结果
     * 且结果emp和dept外连接
     * 原始方式
     * */
//    @Select("select count(*) from emp left join dept on emp.dept_id = dept.id")
//    public Long count();
//
//    @Select("select e.*,d.name deptName from emp e left join dept d on e.dept_id = d.id " +
//            "order by e.update_time desc limit #{start},#{pageSize}")
//    public List<Emp> searchPage(Integer start,Integer pageSize);

    /**
     * 使用PageHelper来实现员工列表查询
     * */
//    @Select("select e.*,d.name deptName from emp e left join dept d on e.dept_id = d.id " +
//            "order by e.update_time desc")
//    public List<Emp> searchPage();

    /**
     * 员工列表查询完整实现（2分页参数+4条件参数）
     * */

//    public List<Emp> searchPage(String name, Integer gender, LocalDate begin, LocalDate end);
    List<Emp> searchPage(EmpQueryParam empQueryParam);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into emp(username, name, gender, phone, job, salary," +
            "image, entry_date, dept_id, create_time, update_time)" +
            "values (#{username},#{name},#{gender},#{phone} ,#{job},#{salary}," +
            "#{image},#{entryDate},#{deptId},#{createTime},#{updateTime})")
    void saveEmp(Emp emp);


    void deleteById(List<Integer> ids);

    Emp searchById(Integer id);

    void update(Emp emp);

    @MapKey("pos")
    List<Map<String, Object>> getEmpJobData();

    @MapKey("name")
    List<Map> getEmpGenderData();

    @Select("select * from emp")
    List<Emp> getMasterNames();

    @Select("select count(*) from emp where dept_id=#{id}")
    Integer getNumberByDeptId(Integer id);

    @Select("select id,username,name from emp where username=#{username} and password=#{password}")
    Emp searchByUsernameAndPassword(Emp emp);
}

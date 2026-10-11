package com.az9374.eduadmin.service.Impl;

import com.az9374.eduadmin.exception.BusinessException;
import com.az9374.eduadmin.mapper.DeptMapper;
import com.az9374.eduadmin.mapper.EmpMapper;
import com.az9374.eduadmin.pojo.Dept;
import com.az9374.eduadmin.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private EmpMapper empMapper;

    @Override
    public List<Dept> findAll() {
        return deptMapper.findAll();
    }

    @Override
    public void deleteById(Integer id) {
//        deptMapper.deleteById(id);
//        deptMapper.findAll();//多余了

        //    优化--如果部门下有员工就不允许删--且抛异常（对不起，当前部门下有员工，不能直接删除！）
        //1.查该Id下的员工人数--emp表
        Integer num=empMapper.getNumberByDeptId(id);
        //2.判断执行,不为0,或者为null?
        if(num>0 ){
            throw new BusinessException("对不起，当前部门下有员工，不能直接删除！");
        }
        deptMapper.deleteById(id);
    }

    @Override
    public void insertByName(Dept dept) {
        //包装成对象然后插入
         dept.setCreateTime(LocalDateTime.now());
         dept.setUpdateTime(LocalDateTime.now());

        deptMapper.insertByName(dept);
    }

    @Override
    public void update(Dept dept) {
        dept.setUpdateTime(LocalDateTime.now());

        deptMapper.update(dept);
    }

    @Override
    public Dept getById(Integer id) {
        return deptMapper.getById(id);
    }
}


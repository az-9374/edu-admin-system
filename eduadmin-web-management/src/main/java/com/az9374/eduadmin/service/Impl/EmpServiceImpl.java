package com.az9374.eduadmin.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.az9374.eduadmin.mapper.EmpExprMapper;
import com.az9374.eduadmin.mapper.EmpMapper;
import com.az9374.eduadmin.pojo.*;
import com.az9374.eduadmin.service.EmpLogService;
import com.az9374.eduadmin.service.EmpService;
import com.az9374.eduadmin.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;

    @Autowired
    private EmpLogService empLogService;

    /*@Override
    public PageResult searchPage(Integer page, Integer pageSize) {
        //1.得到count
        Long total = empMapper.count();
        //2.得到rows
        Integer start = (page-1)*pageSize;
        List<Emp> rows=empMapper.searchPage(start,pageSize);

        //3.传入pageResult
        PageResult<Emp> pageResult=new PageResult<>(total,rows);

        return pageResult;
    }*/

    /*@Override
    public PageResult searchPage(Integer page, Integer pageSize,
                                 String name, Integer gender, LocalDate begin,LocalDate end) {
        //1.PageHelper分页参数
        PageHelper.startPage(page,pageSize);
        //2.执行要分页的查询语句
        List<Emp> empList=empMapper.searchPage( name, gender,  begin, end);
        //3.把类型转为Page<?>以便后续的操作
        Page<Emp> p=(Page<Emp>) empList;
        //4.写入PageResult
        PageResult<Emp> pageResult=new PageResult<>(p.getTotal(),p.getResult());
        return pageResult;
    }*/


    @Override
    public PageResult searchPage(EmpQueryParam empQueryParam) {
        //1.PageHelper分页参数
        PageHelper.startPage(empQueryParam.getPage(),empQueryParam.getPageSize());
        //2.执行要分页的查询语句
        List<Emp> empList=empMapper.searchPage(empQueryParam);
        //3.把类型转为Page<?>以便后续的操作
        Page<Emp> p=(Page<Emp>) empList;
        //4.写入PageResult
        PageResult<Emp> pageResult=new PageResult<>(p.getTotal(),p.getResult());
        return pageResult;
    }

//    @Transactional//仅RuntimeException(运行时异常)才会回滚事务
    @Transactional(rollbackFor = Exception.class)//对所有类型异常管用
    @Override
    public void save(Emp emp) throws Exception {
        try {
            //补全信息，插入员工基本信息
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            empMapper.saveEmp(emp);

//        Integer num=1/0;
//        if(true){
//            throw new Exception("出现异常了...");
//        }

            //先获取新插入员工的id
            List<EmpExpr> exprList=emp.getExprList();
            //不为空时
            if(!CollectionUtils.isEmpty(exprList)){
                exprList.forEach(empExpr -> {
                    empExpr.setEmpId(emp.getId());
                });
                //插入员工经历
                empExprMapper.saveExpr(exprList);
            }
        } finally {
            EmpLog empLog=new EmpLog(null,LocalDateTime.now(),"添加的员工："+emp);
            empLogService.insertLog(empLog);
        }


    }

    @Transactional(rollbackFor = Exception.class)//一个功能统一一下
    @Override
    public void delete(List<Integer> ids) {
        //1.删除emp表里的
        empMapper.deleteById(ids);
        //2.删除对应empExpr表里的
        empExprMapper.deleteByEmpId(ids);
    }

    /**
     * 员工修改--查询回显
     *
     */
    @Override
    public Emp searchById(Integer id) {
        Emp emp= empMapper.searchById(id);
        return emp;
    }

    /**
     * 员工修改
     *
     */
    @Override
    public void update(Emp emp) {
        //1.更新emp表里的
        emp.setUpdateTime(LocalDateTime.now());

        empMapper.update(emp);
        //2.更新expr表里的（先删后加）
        List<EmpExpr> exprList=emp.getExprList();
        empExprMapper.deleteByEmpId(Arrays.asList(emp.getId()));

        if(!CollectionUtils.isEmpty(exprList) && exprList!=null){
            exprList.forEach(empExpr -> {
                empExpr.setEmpId(emp.getId());
            });
            empExprMapper.saveExpr(exprList);
        }
    }

    /**
     * 班级--员工查询
     */
    @Override
    public List<Emp> getMasterNames() {
        List<Emp> masterNames=empMapper.getMasterNames();
        return masterNames;
    }

    /**
     * 登录
     */
    @Override
    public LoginOption login(Emp emp) {
        //1.查询员工信息
        Emp e=empMapper.searchByUsernameAndPassword(emp);
        //2.判断，封装否
        if (e!=null){
            //补充token,生成jwt令牌
            Map<String,Object> dataMap=new HashMap<>();
            dataMap.put("id",e.getId());
            dataMap.put("username",e.getUsername());
            String jwt= JwtUtils.generateJwt(dataMap);
            return new LoginOption(e.getId(),e.getUsername(),e.getName(),jwt);
        }
        return null;
    }
}

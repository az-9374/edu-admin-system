package com.az9374.eduadmin.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.az9374.eduadmin.exception.BusinessException;
import com.az9374.eduadmin.mapper.ClazzMapper;
import com.az9374.eduadmin.pojo.Clazz;
import com.az9374.eduadmin.pojo.ClazzQueryParam;
import com.az9374.eduadmin.pojo.PageResult;
import com.az9374.eduadmin.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {
    @Autowired
    private ClazzMapper clazzMapper;
/*    @Override
    public PageResult<Clazz> searchAll(Integer page, Integer pageSize) {

        //先得到并补全List<Clazz>
        //1.调用search分页查询方法--PageHelper
        PageHelper.startPage(page,pageSize);//分页参数
        List<Clazz> rows=clazzMapper.searchAll();

        //2.查完还要补字段（status-->对比当前时间和开课/节课时间：已结课、未开班、在读中）
        rows.forEach(row -> {
            String status;
            if(row.getEndDate().isBefore( LocalDate.now())){
                status="已结课";
            }else if(row.getBeginDate().isAfter(LocalDate.now())){
                status="未开班";
            }else {
                status="在读";
            }
            row.setStatus(status);
        });

        //3.得到total
        Page<Clazz> p=(Page<Clazz>) rows;
        //4.封装组合PageResult
        PageResult<Clazz> pageResult=new PageResult<>(p.getTotal(),rows);
        return pageResult;
    }*/

    @Override
    public PageResult<Clazz> searchAll(ClazzQueryParam clazzQueryParam) {

        //先得到并补全List<Clazz>
        //1.调用search分页查询方法--PageHelper
        PageHelper.startPage(clazzQueryParam.getPage(),clazzQueryParam.getPageSize());//分页参数
//        List<Clazz> rows=clazzMapper.searchAll();
        List<Clazz> rows=clazzMapper.searchBy(clazzQueryParam);

        //2.查完还要补字段（status-->对比当前时间和开课/节课时间：已结课、未开班、在读中）
        rows.forEach(row -> {
            String status;
            if(row.getEndDate().isBefore( LocalDate.now())){
                status="已结课";
            }else if(row.getBeginDate().isAfter(LocalDate.now())){
                status="未开班";
            }else {
                status="在读";
            }
            row.setStatus(status);
        });

        //3.得到total
        Page<Clazz> p=(Page<Clazz>) rows;
        //4.封装组合PageResult
        PageResult<Clazz> pageResult=new PageResult<>(p.getTotal(),rows);
        return pageResult;
    }

    /**
     * 查询所有班级
     */
    @Override
    public List<Clazz> getAllClazz() {
        List<Clazz> clazzes=clazzMapper.getAllClazz();
        return clazzes;
    }
    /**
     * 新增班级
     */
    @Override
    public void addClazz(Clazz clazz) {
        //1.补全属性（status不用补，masterName也不用补？不用考虑存不存在-->下拉列表必定是存在的）
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        //2.SQL语句
        clazzMapper.addClazz(clazz);
    }

    /**
     * 4.修改班级
     * 先根据id查班级
     */
    @Override
    public Clazz getClazzById(Integer id) {
        Clazz clazz=clazzMapper.getClazzById(id);
        return clazz;
    }

    /**
     * 4.修改班级
     * 修改班级
     */
    @Override
    public void updateClazz(Clazz clazz) {
        //1.补全updateTime
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.updateClazz(clazz);
    }

    /**
     * 5.删除班级
     */
    @Override
    public void deleteClazz(Integer id) {
        //1.查询该班级下还有没有学生
        Integer num=clazzMapper.getStudentsNum(id);
        //2.删除操作
        if(num !=null && num>0){
            throw new BusinessException("对不起, 该班级下有学生, 不能直接删除");
        }
        clazzMapper.deleteClazz(id);

    }
}

package com.az9374.eduadmin.mapper;

import com.az9374.eduadmin.pojo.EmpExpr;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface EmpExprMapper {
    void saveExpr(List<EmpExpr> exprList);

    void deleteByEmpId(List<Integer> empIds);
}

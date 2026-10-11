package com.az9374.eduadmin.service;

import com.az9374.eduadmin.pojo.JobOption;
import com.az9374.eduadmin.pojo.StudentCountOption;

import java.util.List;
import java.util.Map;

public interface ReportService {

    JobOption getEmpJobData();

    List<Map> getEmpGenderData();

    StudentCountOption getStudentCountData();

    List<Map> getStudentDegreeData();
}
